package com.dater.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dater.model.PasswordResetEntity;
import com.dater.model.UserEntity;
import com.dater.service.PasswordResetService;

@RestController
@RequestMapping("/passwords")
public class PasswordResetController {
	
	private final PasswordResetService passResetSvc;

	@Autowired
	public PasswordResetController(PasswordResetService passResetSvc) {
		this.passResetSvc = passResetSvc;
	}
	
	@GetMapping
	public PasswordResetEntity getPasswordReset(@RequestParam(name = "id") String id) {
		return passResetSvc.getPasswordReset(id);
	}

	@PostMapping
	public ResponseEntity<String> createPasswordResetLink(@RequestBody UserEntity user) {
		return passResetSvc.requestPasswordReset(user.getEmail());
	}
	
	@PostMapping(value = "/reset")
	public void resetUserPassword(@RequestBody UserEntity user) {
		passResetSvc.resetUserPassword(user.getEmail(), user.getPassword());
	}

}
