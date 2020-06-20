package com.dater.utils;

import java.util.Optional;

import com.dater.model.Gender;

public final class ConversionUtil {
	
	private ConversionUtil() {}
	
	public static Optional<Gender> convertStringToGender(String s) {
		try {
			return Optional.of(Gender.valueOf(s));
		}
		catch(Exception e) {
			return Optional.empty();
		}
	}

}
