package com.dater.service.impl;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dater.event.PasswordResetRequestedEvent;
import com.dater.exception.PasswordResetNotFoundException;
import com.dater.model.PasswordResetEntity;
import com.dater.model.UserEntity;
import com.dater.repository.PasswordResetRepository;
import com.dater.service.PasswordResetService;
import com.dater.service.UserService;

import static com.dater.message.PasswordResetMessages.*;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {
	
	private final UserService userSvc;
	private final PasswordResetRepository passResetRepo;
	private final ApplicationEventPublisher eventPublisher;
	
	@Autowired
	public PasswordResetServiceImpl(UserService userSvc, PasswordResetRepository passResetRepo, ApplicationEventPublisher eventPublisher) {
		this.passResetRepo = passResetRepo;
		this.userSvc = userSvc;
		this.eventPublisher = eventPublisher;
	}
	
	@Override
	@Transactional
	public void setEmailSuccessful(String requestId) {
		PasswordResetEntity passReset = passResetRepo.findById(requestId)
				.orElseThrow(() -> new PasswordResetNotFoundException(String.format(NOT_FOUND_BY_ID, requestId)));
		passReset.setEmailSuccessful(true);
	}

	@Override
	@Transactional
	public ResponseEntity<String> requestPasswordReset(String email) {
		UserEntity user = userSvc.findUserByEmail(email);
		return passResetRepo.findByEmail(email).map(req -> {
			if(req.isEmailSuccessful()) {
				return new ResponseEntity<String>(ALREADY_REQUESTED, HttpStatus.OK);
			}
			else {
				eventPublisher.publishEvent(new PasswordResetRequestedEvent(this, user.getEmail(), req));
				return new ResponseEntity<String>(RESET_LINK_RESEND, HttpStatus.OK);
			}
		}).orElseGet(() -> {
			PasswordResetEntity passReset = passResetRepo.save(new PasswordResetEntity(email, LocalDateTime.now()));
			eventPublisher.publishEvent(new PasswordResetRequestedEvent(this, user.getEmail(), passReset));
			return new ResponseEntity<String>(HttpStatus.CREATED);
		});
	}

	@Override
	public PasswordResetEntity getPasswordReset(String passResetId) {
		return passResetRepo.findById(passResetId)
				.orElseThrow(() -> new PasswordResetNotFoundException(String.format(NOT_FOUND_BY_ID, passResetId)));
	}

	@Override
	@Transactional
	public UserEntity resetUserPassword(String email, String newPassword) {
		PasswordResetEntity passReset = passResetRepo.findByEmail(email)
				.orElseThrow(() -> new PasswordResetNotFoundException(String.format(NOT_FOUND_BY_EMAIL, email)));
		UserEntity user = userSvc.updateUserPassword(email, newPassword);
		passResetRepo.deleteRequestById(passReset.getId());
		return user;
	}

}
