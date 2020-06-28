package com.dater.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dater.model.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, CustomUserRepository {

	@Query("SELECT user FROM UserEntity user left join fetch user.photos where user.username=:username")
	Optional<UserEntity> findByUsername(@Param("username") String username);
	
}
