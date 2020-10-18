package com.dater.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ConversationNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1341434093628696378L;

	public ConversationNotFoundException(String message) {
		super(message);
	}
	
}
