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
	
	public static final String USER_DATA_EMPTY = "Provided user data is empty.";
	
	public static final String USERNAME_EMPTY = "Username cannot be empty.";
	
	public static final String USERNAME_TOO_SHORT = "Username is too short, minimum length is 5 characters.";
	
	public static final String USERNAME_TOO_LONG = "Username is too long, maximum length allowed is 18 characters.";
	
	public static final String USERNAME_INVALID_CHARS = "Username contains invalid characters, only letters and digits are allowed.";
	
	public static final String EMAIL_EMPTY = "E-mail address cannot be empty.";
	
	public static final String EMAIL_INVALID = "Provided e-mail address is invalid.";
	
	public static final String PASSWORD_EMPTY = "Password cannot be empty.";
	
	public static final String PASSWORD_TOO_SHORT = "Password is too short, minimum length is 5 characters.";
	
	public static final String PASSWORD_TOO_LONG = "Password is too long, maximum length allowed is 24 characters.";
	
	public static final String PASSWORD_INVALID_CHARS = "Password contains invalid characters, only letters and digits are allowed.";
	
	public static final String DATE_EMPTY = "Date of birth cannot be empty.";
	
	public static final String DATE_TOO_OLD = "Date of birth cannot be from more than 100 years ago.";
	
	public static final String DATE_TOO_YOUNG = "You have to be older than 18 years to create an account.";
	
	public static final String LOCATION_EMPTY = "Location cannot be empty.";
	
	public static final String LOCATION_INVALID_CHARS = "Location contains invalid characters. Do not use spaces at the beginning or non location name characters ($, %, *, etc.).";
	
	public static final String GENDER_EMPTY = "Gender has not been selected.";
	
	public static final String DESCRIPTION_TOO_LONG = "Description is too long, maximum length is 500 characters.";
	
	public static final String PHOTOS_EMPTY = "At least one photo is required.";
	
	public static final String TOO_MANY_PHOTOS = "Maximum of 5 photos is allowed.";
	
	private UserMessages() {}

}
