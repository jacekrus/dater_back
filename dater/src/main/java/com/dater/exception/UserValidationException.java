package com.dater.exception;

public class UserValidationException extends RuntimeException {

	private static final long serialVersionUID = 6563319908955267225L;

	public UserValidationException(String message) {
		super(message);
	}

}
