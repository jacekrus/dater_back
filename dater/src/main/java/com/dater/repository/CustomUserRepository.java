package com.dater.repository;

import java.util.List;
import java.util.Optional;

import com.dater.model.Gender;
import com.dater.model.UserEntity;

public interface CustomUserRepository {
	
	List<UserEntity> findRecommendedForCurrentUser(Gender userGender, Optional<Gender> preferredGender);

}
