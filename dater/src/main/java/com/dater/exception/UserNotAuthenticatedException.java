package com.dater.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UserNotAuthenticatedException extends BaseDaterException {

	private static final long serialVersionUID = -7926610261856044181L;

	public UserNotAuthenticatedException(String message) {
		super(message);
	}

}
