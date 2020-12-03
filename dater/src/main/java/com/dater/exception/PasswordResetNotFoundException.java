package com.dater.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PasswordResetNotFoundException extends BaseDaterException {
	
	private static final long serialVersionUID = -6821051054596366584L;

	public PasswordResetNotFoundException(String message) {
		super(message);
	}

}
