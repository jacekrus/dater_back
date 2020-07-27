package com.dater.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dater.exception.AuthorizationException;
import com.dater.exception.UserNotAuthenticatedException;
import com.dater.exception.UserNotFoundException;
import com.dater.model.UserEntity;
import com.dater.repository.UserRepository;
import com.dater.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void addUser(UserEntity userEntity) {
		if(userEntity.getPassword().length() > 24) {
			throw new IllegalArgumentException("Too long password, only 24 characters are allowed.");
		}
		userEntity.setId(userEntity.generateId());
		userEntity.setPassword(passwordEncoder().encode(userEntity.getPassword()));
		if(userEntity.getRole() == null) {
			userEntity.setRole("USER");
		}
		userRepository.save(userEntity);
	}
	
	@Override
	public void updateUser(UserEntity user) {
		UserEntity loggedInUser = getLoggedInUser();
		if(!loggedInUser.getRole().equals("ADMIN") && loggedInUser.getId().equals(user.getId())) {
			throw new AuthorizationException("Update is not permitted for other user than currently logged in.");
		}
	}
	
	@Override
	public List<UserEntity> findRecommendedForUser(String userId) {
		UserEntity user = findUserById(userId);
		List<UserEntity> recommended = userRepository.findRecommended(user.getGender(), Optional.ofNullable(user.getPreference()));
		//workaround to trigger lazy association fetching
		recommended.stream().findFirst().ifPresent(rec -> rec.getPhotos().forEach(photo -> {}));
		return recommended;
	}

	@Override
	@PreAuthorize("hasAuthority('ADMIN')")
	public void removeUser(String userId) {
		userRepository.deleteById(userId);
	}

	@Override
	public List<UserEntity> findUsers(Example<UserEntity> example, Pageable pageable) {
		return userRepository.findAll(example, pageable).getContent();
	}

	@Override
	public UserEntity findUserById(String id) throws UserNotFoundException {
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id [" + id + "] has not been found."));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Authentication failed in UserService"));
	}

	@Override
	public UserEntity getLoggedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null) {
			throw new UserNotAuthenticatedException("No authentication found in security context.");
		}
		Object user = auth.getPrincipal();
		if(user == null || !(user instanceof UserEntity)) {
			throw new UserNotAuthenticatedException("No logged in user found or authentication data is incorrect.");
		}
		return (UserEntity) user;
	}

	@Override
	@Transactional
	public void addFavoriteUser(String id) {
		UserEntity loggedInUser = userRepository.getUserReference(getLoggedInUser().getId());
		UserEntity potentialDate = userRepository.getUserReference(id);
		
		if(!userRepository.isFavorite(loggedInUser, potentialDate)) {
			userRepository.addFavorite(loggedInUser, potentialDate);
			if(userRepository.isFavorite(potentialDate, loggedInUser)) {
				userRepository.createDate(loggedInUser, potentialDate);
			}
		}
	}

	@Override
	public List<UserEntity> findFavoritesForUser(String userId, Pageable pageable) {
		List<String> ids = userRepository.findFavoriteIdsForUser(userRepository.getUserReference(userId), pageable);
		return userRepository.findUsersByIdWithPhotos(ids);
	}
	
	@Override
	public List<UserEntity> findLikedByForUser(String userId, Pageable pageable) {
		List<String> ids = userRepository.findLikedByIdsForUser(userRepository.getUserReference(userId), pageable);
		return userRepository.findUsersByIdWithPhotos(ids);
	}
	
	@Override
	public List<UserEntity> findDatesForUser(String userId, Pageable pageable) {
		List<String> ids = userRepository.findDateIdsForUser(userRepository.getUserReference(userId), pageable);
		return userRepository.findUsersByIdWithPhotos(ids);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
