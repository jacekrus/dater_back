package com.dater.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dater.exception.UserValidationException;
import com.dater.model.Gender;
import com.dater.model.UserEntity;

public class UserValidator {
	
	private static final String USER_DATA_EMPTY = "Provided user data is empty.";
	private static final String USERNAME_EMPTY = "Username cannot be empty.";
	private static final String USERNAME_TOO_SHORT = "Username is too short, minimum length is 5 characters.";
	private static final String USERNAME_TOO_LONG = "Username is too long, maximum length allowed is 18 characters.";
	private static final String USERNAME_INVALID_CHARS = "Username contains invalid characters, only letters and digits are allowed.";
	private static final String EMAIL_EMPTY = "E-mail address cannot be empty.";
	private static final String EMAIL_INVALID = "Provided e-mail address is invalid.";
	private static final String PASSWORD_EMPTY = "Password cannot be empty.";
	private static final String PASSWORD_TOO_SHORT = "Password is too short, minimum length is 5 characters.";
	private static final String PASSWORD_TOO_LONG = "Password is too long, maximum length allowed is 24 characters.";
	private static final String PASSWORD_INVALID_CHARS = "Password contains invalid characters, only letters and digits are allowed.";
	private static final String DATE_EMPTY = "Date of birth cannot be empty.";
	private static final String DATE_TOO_OLD = "Date of birth cannot be from more than 100 years ago.";
	private static final String DATE_TOO_YOUNG = "You have to be older than 18 years to create an account.";
	private static final String LOCATION_EMPTY = "Location cannot be empty.";
	private static final String LOCATION_INVALID_CHARS = "Location contains invalid characters, only letters are allowed separated by a space [country city].";
	private static final String GENDER_EMPTY = "Gender has not been selected.";
	private static final String DESCRIPTION_TOO_LONG = "Description is too long, maximum length is 500 characters.";
	private static final String PHOTOS_EMPTY = "At least one photo is required.";
	private static final String TOO_MANY_PHOTOS = "Maximum of 5 photos is allowed.";
	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	private UserValidator() {}
	
	private static class InstanceHolder {
		private static final UserValidator instance = new UserValidator(); 
	}
	
	public static UserValidator getInstance() {
		return InstanceHolder.instance;
	}
	
	public void validate(UserEntity user) {
		if(user == null) {
			throw new UserValidationException(USER_DATA_EMPTY);
		}
		validateUsername(user);
		validateEmail(user);
		validatePassword(user);
		validateDateOfBirth(user);
		validateLocation(user);
		validateGender(user);
		validateDescription(user);
	}
	
	public void validateWithPhotos(UserEntity user) {
		validate(user);
		validatePhotos(user);
	}
	
	private void validateUsername(UserEntity user) {
		String username = user.getUsername();
		if(isNullOrBlank(username)) {
			throw new UserValidationException(USERNAME_EMPTY);
		}
		if(username.length() < 5) {
			throw new UserValidationException(USERNAME_TOO_SHORT);
		}
		if(username.length() > 18) {
			throw new UserValidationException(USERNAME_TOO_LONG);
		}
		if(hasInvalidCharacters(username)) {
			throw new UserValidationException(USERNAME_INVALID_CHARS);
		}
	}
	
	private void validateEmail(UserEntity user) {
		String email = user.getEmail();
		if(isNullOrBlank(email)) {
			throw new UserValidationException(EMAIL_EMPTY);
		}
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if(!matcher.find()) {
        	throw new UserValidationException(EMAIL_INVALID);
        }
	}
	
	private void validatePassword(UserEntity user) {
		String password = user.getPassword();
		if(isNullOrBlank(password)) {
			throw new UserValidationException(PASSWORD_EMPTY);
		}
		if(password.length() < 5) {
			throw new UserValidationException(PASSWORD_TOO_SHORT);
		}
		if(password.length() > 24) {
			throw new UserValidationException(PASSWORD_TOO_LONG);
		}
		if(hasInvalidCharacters(password)) {
			throw new UserValidationException(PASSWORD_INVALID_CHARS);
		}
	}
	
	private void validateDateOfBirth(UserEntity user) {
		LocalDate date = user.getDateOfBirth();
		if(date == null) {
			throw new UserValidationException(DATE_EMPTY);
		}
		if(date.isBefore(LocalDate.now().minusYears(100))) {
			throw new UserValidationException(DATE_TOO_OLD);
		}
		if(date.isAfter(LocalDate.now().minusYears(18))) {
			throw new UserValidationException(DATE_TOO_YOUNG);
		}
	}
	
	private void validateLocation(UserEntity user) {
		String location = user.getLocation();
		if(isNullOrBlank(location)) {
			throw new UserValidationException(LOCATION_EMPTY);
		}
		if(!containsOnlyLetters(location)) {
			throw new UserValidationException(LOCATION_INVALID_CHARS);
		}
	}
	
	private void validateGender(UserEntity user) {
		Gender gender = user.getGender();
		if(gender == null) {
			throw new UserValidationException(GENDER_EMPTY);
		}
	}
	
	private void validateDescription(UserEntity user) {
		String description = user.getDescription();
		if(description != null && description.length() > 500) {
			throw new UserValidationException(DESCRIPTION_TOO_LONG);
		}
	}
	
	private void validatePhotos(UserEntity user) {
		List<String> photos = user.getPhotos();
		if(photos == null || photos.isEmpty()) {
			throw new UserValidationException(PHOTOS_EMPTY);
		}
		if(photos.size() > 5) {
			throw new UserValidationException(TOO_MANY_PHOTOS);
		}
	}
	
	private boolean isNullOrBlank(String value) {
		return value == null || value.isBlank();
	}
	
	private boolean containsOnlyLetters(String value) {
		char[] chars = value.toCharArray();
		for(char c : chars) {
			if(!Character.isLetter(c) && !Character.isSpaceChar(c)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean hasInvalidCharacters(String value) {
		char[] chars = value.toCharArray();
		for(char c : chars) {
			if(!Character.isLetterOrDigit(c)) {
				return true;
			}
		}
		return false;
	}

}
