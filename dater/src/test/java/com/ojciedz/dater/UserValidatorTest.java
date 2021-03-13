package com.ojciedz.dater;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.dater.exception.UserValidationException;
import com.dater.model.Gender;
import com.dater.model.UserBuilder;
import com.dater.model.UserEntity;
import com.dater.service.impl.UserValidator;

class UserValidatorTest {
	
    @Test
    void validateUsernameTest() throws Exception {
    	UserEntity entity = new UserBuilder()
	    	.withUsername("username")
    		.withPassword("password")
    		.bornOn(LocalDate.of(2001, 9, 15))
    		.withLocation("here")
    		.withRole("USER")
    		.withDescription("description")
    		.withGender(Gender.MALE)
    		.withPreference(Gender.FEMALE)
    		.withPhotos(List.of("first", "second"))
	    	.withEmail("testemail@test.com")
	    	.build();
    	UserValidator.getInstance().validate(entity);
    }
    
    @Test
    void validateInvalidLocationShouldThrowException() {
    	Assertions.assertThrows(UserValidationException.class, () -> UserValidator.getInstance().validateLocation("54---dascz!"));
    }
    
}
