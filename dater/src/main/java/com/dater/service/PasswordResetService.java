package com.dater.service;

import org.springframework.http.ResponseEntity;

import com.dater.model.PasswordResetEntity;

public interface PasswordResetService {
	
	void resetUserPassword(String email, String newPassword);
	
	void setEmailSuccessful(String requestId);
	
	ResponseEntity<String> requestPasswordReset(String email);
	
	PasswordResetEntity getPasswordReset(String passResetId);

}
