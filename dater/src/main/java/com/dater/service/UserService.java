package com.dater.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.dater.model.UserEntity;

public interface UserService extends UserDetailsService {
	
	void addUser(UserEntity user);
	
	void validateUser(UserEntity user);
	
	void removeUser(String id);
	
	UserEntity updateUser(UserEntity user);
	
	UserEntity setProfilePhoto(String photo);
	
	UserEntity removePhoto(String photo);
	
	UserEntity findUserById(String id);
	
	UserEntity getLoggedInUser();
	
	UserEntity getReference(String id);
	
	ResponseEntity<String> addFavoriteUser(String id);
	
	List<UserEntity> findRecommendedForUser(String userId);
	
	List<UserEntity> findUsers(Example<UserEntity> example, Pageable pageable); 
	
	List<UserEntity> findFavoritesForUser(String userId, Pageable pageable);
	
	List<UserEntity> findLikedByForUser(String userId, Pageable pageable);
	
	List<UserEntity> findDatesForUser(String userId, Pageable pageable);

}
