package com.dater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dater.model.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, CustomUserRepository {

	UserEntity findByUsername(String username);
	
}
