package com.dater.message;

public final class PasswordResetMessages {
	
	public static final String ALREADY_REQUESTED = "You have already requested password reset. Please check your e-mail and spam folder for a message from Dater.";
	
	public static final String RESET_LINK_RESEND = "Reset link will be send again shortly. There have been problems sending previous reset link, "
			+ "please make sure that your e-mail address is still valid.";
	
	public static final String NOT_FOUND_BY_ID = "Password reset request with id: %s has not been found.";
	
	public static final String NOT_FOUND_BY_EMAIL = "Password reset request for email: %s "
			+ "has not been found. It might have timed out, please request password reset again.";
	
	private PasswordResetMessages() {}

}
