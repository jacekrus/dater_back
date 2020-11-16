package com.dater.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.dater.model.FavoriteEntity;
import com.dater.model.Gender;
import com.dater.model.UserEntity;

public interface CustomUserRepository {
	
	void createDate(UserEntity firstUser, UserEntity secondUser);
	
	void addFavorite(UserEntity owner, UserEntity favorite);
	
	UserEntity getUserReference(String userId);
	
	Optional<FavoriteEntity> isFavorite(UserEntity user, UserEntity favorite);
	
	List<String> findFavoriteIdsForUser(UserEntity user, Pageable pageable);
	
	List<String> findDateIdsForUser(UserEntity user, Pageable pageable);
	
	List<String> findLikedByIdsForUser(UserEntity user, Pageable pageable);
	
	List<UserEntity> findRecommended(Gender userGender, Optional<Gender> preferredGender);
	
	List<UserEntity> findUsersByIdWithPhotos(Collection<String> ids);
	
}
