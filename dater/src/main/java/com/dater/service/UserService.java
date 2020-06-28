package com.dater.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.dater.exception.UserNotFoundException;
import com.dater.model.UserEntity;

public interface UserService extends UserDetailsService {
	
	void addUser(UserEntity user);
	
	void removeUser(String id);
	
	List<UserEntity> findUsers(Example<UserEntity> example, Pageable pageable); 
	
	UserEntity findUserById(String id) throws UserNotFoundException;
	
	void updateUser(UserEntity user);
	
	List<UserEntity> findRecommendedForCurrentUser();
	
	UserEntity getLoggedInUser();

}
