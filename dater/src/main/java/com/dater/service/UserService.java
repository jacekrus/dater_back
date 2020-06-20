package com.dater.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.dater.exception.UserNotFoundException;
import com.dater.model.UserEntity;

public interface UserService extends UserDetailsService {
	
	void addUser(UserEntity user);
	
	void removeUser(String id);
	
	List<UserEntity> findAllUsers(); 
	
	UserEntity findUserById(String id) throws UserNotFoundException;
	
	void updateUser(UserEntity user);
	
	List<UserEntity> findRecommendedForCurrentUser(String prefferedGender);
	
	UserEntity getLoggedInUser();

}
