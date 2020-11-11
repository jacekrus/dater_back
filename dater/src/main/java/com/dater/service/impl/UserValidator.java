package com.dater.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dater.exception.UserValidationException;
import com.dater.model.Gender;
import com.dater.model.UserEntity;

import static com.dater.service.impl.UserMessages.*;

public class UserValidator {
	
	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	private static final Pattern VALID_CITY_NAME_REGEX = Pattern.compile("^([a-z\u0080-\u024F]+(?:. |-| |'))*[a-z\u0080-\u024F]*$", Pattern.CASE_INSENSITIVE);

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
	
	public void validateLocation(UserEntity user) {
		String location = user.getLocation();
		if(isNullOrBlank(location)) {
			throw new UserValidationException(LOCATION_EMPTY);
		}
		Matcher matcher = VALID_CITY_NAME_REGEX.matcher(location);
		if(!matcher.find()) {
			throw new UserValidationException(LOCATION_INVALID_CHARS);
		}
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
