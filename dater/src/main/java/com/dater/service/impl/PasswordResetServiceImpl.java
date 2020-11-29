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

@Service
public class PasswordResetServiceImpl implements PasswordResetService {
	
	private final UserService userSvc;
	private final PasswordResetRepository passResetRepo;
	private final ApplicationEventPublisher eventPublisher;
	
	private static final String ALREADY_REQUESTED = "You have already requested password reset. Please check your e-mail and spam folder for a message from Dater.";
	private static final String RESET_LINK_RESEND = "Reset link will be send again shortly. There have been problems sending previous reset link, "
			+ "please make sure that your e-mail address is still valid.";

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
				.orElseThrow(() -> new PasswordResetNotFoundException("Password reset request with id: " + requestId + " has not been found."));
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
				.orElseThrow(() -> new PasswordResetNotFoundException("Password reset request with id: " + passResetId + " has not been found."));
	}

	@Override
	@Transactional
	public UserEntity resetUserPassword(String email, String newPassword) {
		PasswordResetEntity passReset = passResetRepo.findByEmail(email)
				.orElseThrow(() -> new PasswordResetNotFoundException("Password reset request for email: " + email + " has not been found. It might have timed out, please request password reset again."));
		UserEntity user = userSvc.updateUserPassword(email, newPassword);
		passResetRepo.deleteRequestById(passReset.getId());
		return user;
	}

}
