package com.dater.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AuthorizationException extends BaseDaterException {

	private static final long serialVersionUID = 5315259398055789594L;

	public AuthorizationException(String message) {
		super(message);
	}
	
}
