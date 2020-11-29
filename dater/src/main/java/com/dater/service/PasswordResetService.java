package com.dater.service;

import org.springframework.http.ResponseEntity;

import com.dater.model.PasswordResetEntity;
import com.dater.model.UserEntity;

public interface PasswordResetService {
	
	void setEmailSuccessful(String requestId);
	
	UserEntity resetUserPassword(String email, String newPassword);
	
	ResponseEntity<String> requestPasswordReset(String email);
	
	PasswordResetEntity getPasswordReset(String passResetId);

}
