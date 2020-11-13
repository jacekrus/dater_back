package com.dater.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.dater.model.UserEntity;
import com.dater.repository.SkippingPageable;

public interface UserService extends UserDetailsService {
	
	void addUser(UserEntity user);
	
	void validateUser(UserEntity user);
	
	void removeUser(String id);
	
	boolean addFavoriteUser(String id);
	
	UserEntity updateUser(UserEntity user);
	
	UserEntity setProfilePhoto(String photo);
	
	UserEntity removePhoto(String photo);
	
	UserEntity findUserById(String id);
	
	UserEntity getLoggedInUser();
	
	UserEntity getReference(String id);
	
	List<UserEntity> findRecommendedForUser(String userId);
	
	List<UserEntity> findUsers(Example<UserEntity> example, SkippingPageable pageable); 
	
	List<UserEntity> findFavoritesForUser(String userId, SkippingPageable pageable);
	
	List<UserEntity> findLikedByForUser(String userId, SkippingPageable pageable);
	
	List<UserEntity> findDatesForUser(String userId, SkippingPageable pageable);

}
