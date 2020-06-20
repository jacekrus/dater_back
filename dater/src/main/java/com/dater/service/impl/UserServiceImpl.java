package com.dater.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dater.exception.UserNotAuthenticatedException;
import com.dater.exception.UserNotFoundException;
import com.dater.model.Gender;
import com.dater.model.UserEntity;
import com.dater.repository.UserRepository;
import com.dater.service.UserService;
import com.dater.utils.ConversionUtil;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void addUser(UserEntity userEntity) {
		userEntity.setId(userEntity.generateId());
		userEntity.setPassword(passwordEncoder().encode(userEntity.getPassword()));
		if(userEntity.getRole() == null) {
			userEntity.setRole("USER");
		}
		userRepository.save(userEntity);
	}
	
	@Override
	public void updateUser(UserEntity user) {
		UserEntity loggedInUser = getLoggedInUser();
		if(!loggedInUser.getRole().equals("ADMIN") && loggedInUser.getId().equals(user.getId())) {
			throw new IllegalArgumentException("Update is not permitted for other user than currently logged in.");
		}
	}
	
	@Override
	public List<UserEntity> findRecommendedForCurrentUser(String genderString) {
		Optional<Gender> preferredGender = ConversionUtil.convertStringToGender(genderString);
		return userRepository.findRecommendedForCurrentUser(getLoggedInUser().getGender(), preferredGender);
	}

	@Override
	@PreAuthorize("hasAuthority('ADMIN')")
	public void removeUser(String userId) {
		userRepository.deleteById(userId);
	}

	@Override
	public List<UserEntity> findAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public UserEntity findUserById(String id) throws UserNotFoundException {
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id [" + id + "] has not been found."));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username);
	}

	@Override
	public UserEntity getLoggedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null) {
			throw new UserNotAuthenticatedException("No authentication found in security context.");
		}
		Object user = auth.getPrincipal();
		if(user == null || !(user instanceof UserEntity)) {
			throw new UserNotAuthenticatedException("No logged in user found or authentication data is incorrect.");
		}
		return (UserEntity) user;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
