package com.dater.service.impl;

public final class UserMessages {
	
	public static final String USER_ALREADY_EXISTS = "User with such username or e-mail already exists. ";
	
	public static final String USER_NOT_FOUND_BY_ID = "User with id [%s] has not been found. ";
	
	public static final String USER_NOT_FOUND_BY_USERNAME = "User with username [%s] has not been found. ";
	
	public static final String ERROR_ADDING_PHOTOS = "Unexpected error occured while adding photos. ";
	
	public static final String ERROR_SETTING_PROFILE_PHOTO = "Error occured while setting profile photo. ";
	
	public static final String ERROR_REMOVING_PHOTO = "Error occured while removing photo. ";
	
	public static final String PHOTO_NOT_FOUND = "Photo not found in user's gallery. ";
	
	public static final String PHOTO_REQUIRED = "At least one photo is required. ";
	
	public static final String TRY_AGAIN_OR_CONTACT = "Please try again later or contact site's administrator. ";
	
	public static final String MAX_PHOTOS_EXECEEDED = "Only 4 photos are allowed per user. ";
	
	public static final String NOT_AUTHORIZED = "Action is not permitted for other user than currently logged in. ";
	
	public static final String AUTH_FAILED = "Authentication failed. ";
	
	public static final String NO_LOGGED_IN_USER_FOUND = "No logged in user found or authentication data is incorrect. ";
	
	private UserMessages() {}

}
