package com.dater.service.impl;

import static com.dater.service.impl.UserMessages.AUTH_FAILED;
import static com.dater.service.impl.UserMessages.ERROR_ADDING_PHOTOS;
import static com.dater.service.impl.UserMessages.ERROR_REMOVING_PHOTO;
import static com.dater.service.impl.UserMessages.ERROR_SETTING_PROFILE_PHOTO;
import static com.dater.service.impl.UserMessages.MAX_PHOTOS_EXECEEDED;
import static com.dater.service.impl.UserMessages.PHOTO_NOT_FOUND;
import static com.dater.service.impl.UserMessages.PHOTO_REQUIRED;
import static com.dater.service.impl.UserMessages.TRY_AGAIN_OR_CONTACT;
import static com.dater.service.impl.UserMessages.USER_ALREADY_EXISTS;
import static com.dater.service.impl.UserMessages.USER_NOT_FOUND_BY_EMAIL;
import static com.dater.service.impl.UserMessages.USER_NOT_FOUND_BY_ID;
import static com.dater.service.impl.UserMessages.USER_NOT_FOUND_BY_USERNAME;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dater.event.DateCreatedEvent;
import com.dater.event.FavoriteAddedEvent;
import com.dater.event.UserLikedEvent;
import com.dater.exception.UserNotFoundException;
import com.dater.exception.UserValidationException;
import com.dater.model.FavoriteEntity;
import com.dater.model.Gender;
import com.dater.model.UserEntity;
import com.dater.repository.UserRepository;
import com.dater.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final ApplicationEventPublisher eventPublisher;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
		this.userRepository = userRepository;
		this.eventPublisher = eventPublisher;
	}

	@Override
	public void addUser(UserEntity userEntity) {
		UserValidator.getInstance().validateWithPhotos(userEntity);
		checkDoesNotAlreadyExist(userEntity);
		userEntity.setPassword(new BCryptPasswordEncoder().encode(userEntity.getPassword()));
		userEntity.setRole("USER");
		if (userEntity.getPreference() == null) {
			userEntity.setPreference(userEntity.getGender() == Gender.MALE ? Gender.FEMALE : Gender.MALE);
		}
		userRepository.save(userEntity);
	}

	@Override
	public void validateUser(UserEntity userEntity) {
		UserValidator.getInstance().validate(userEntity);
		checkDoesNotAlreadyExist(userEntity);
	}

	@Override
	public UserEntity updateUser(UserEntity user) {
		UserEntity loggedInUser = getLoggedInUserForUpdate();
		if (user.getDescription() != null) {
			loggedInUser.setDescription(user.getDescription());
		}
		if (user.getPreference() != loggedInUser.getPreference()) {
			loggedInUser.setPreference(user.getPreference());
		}
		if (user.getLocation() != null) {
			UserValidator.getInstance().validateLocation(user.getLocation());
			loggedInUser.setLocation(user.getLocation());
		}
		if (user.getPhotos() != null) {
			List<String> photos = loggedInUser.getPhotos();
			if (user.getPhotos().size() + photos.size() > 4) {
				throw new UserValidationException(MAX_PHOTOS_EXECEEDED);
			}
			if (!loggedInUser.addPhotos(user.getPhotos())) {
				throw new UserValidationException(ERROR_ADDING_PHOTOS + TRY_AGAIN_OR_CONTACT);
			}
		}
		UserEntity updatedUser = userRepository.save(loggedInUser);
		updateCache(updatedUser);
		return updatedUser;
	}
	
	@Override
	@Transactional
	public void updateUserPassword(String email, String newPassword) {
		UserValidator.getInstance().validatePassword(newPassword);
		UserEntity user = findUserByEmail(email);
		user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
	}

	@Override
	public UserEntity setProfilePhoto(String photo) {
		UserEntity loggedInUser = getLoggedInUserForUpdate();
		if (!validatePhotoExistsInUserGallery(loggedInUser, photo)) {
			throw new UserValidationException(ERROR_SETTING_PROFILE_PHOTO + PHOTO_NOT_FOUND);
		}
		loggedInUser.setProfilePhoto(photo);
		UserEntity updatedUser = userRepository.save(loggedInUser);
		updateCache(updatedUser);
		return updatedUser;
	}

	@Override
	public UserEntity removePhoto(String photo) {
		UserEntity loggedInUser = getLoggedInUserForUpdate();
		if (!validatePhotoExistsInUserGallery(loggedInUser, photo)) {
			throw new UserValidationException(ERROR_REMOVING_PHOTO + PHOTO_NOT_FOUND);
		}
		if (loggedInUser.getPhotos().size() < 2) {
			throw new UserValidationException(ERROR_REMOVING_PHOTO + PHOTO_REQUIRED);
		}
		loggedInUser.removePhoto(photo);
		UserEntity updatedUser = userRepository.save(loggedInUser);
		updateCache(updatedUser);
		return updatedUser;
	}

	@Override
	public List<UserEntity> findRecommendedForUser(String userId) {
		UserEntity user = findUserById(userId);
		List<UserEntity> recommended = userRepository.findRecommended(user.getGender(),
				Optional.ofNullable(user.getPreference()));
		recommended.stream().findFirst().ifPresent(rec -> Hibernate.initialize(rec.getPhotos()));
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
	public UserEntity findUserById(String id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_BY_ID, id)));
	}
	
	@Override
	public UserEntity findUserByIdWithPhotos(String id) {
		return userRepository.findByIdWithPhotos(id)
				.orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_BY_ID, id)));
	}


	
	@Override
	public UserEntity findUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_BY_EMAIL, email)));
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException(AUTH_FAILED + String.format(USER_NOT_FOUND_BY_USERNAME, username)));
	}

	@Override
	public UserEntity getLoggedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof UserEntity)) {
			return null;
		}
		return (UserEntity) auth.getPrincipal();
	}

	@Override
	@Transactional
	public ResponseEntity<String> addFavoriteUser(String favoriteId) {
		UserEntity loggedInUser = getLoggedInUser();
		UserEntity potentialDate = findUserById(favoriteId);
		Optional<FavoriteEntity> favorite = userRepository.isFavorite(loggedInUser, potentialDate);
		return favorite.map(fav -> {
			fav.setCreateTime(LocalDateTime.now());
			return new ResponseEntity<String>(HttpStatus.OK);
		}).orElseGet(() -> {
			userRepository.addFavorite(loggedInUser, potentialDate);
			eventPublisher.publishEvent(new FavoriteAddedEvent(this, loggedInUser.getUsername(), potentialDate.getUsername()));
			eventPublisher.publishEvent(new UserLikedEvent(this, potentialDate.getUsername(), loggedInUser.getUsername()));
			return userRepository.isFavorite(potentialDate, loggedInUser).map(fav -> {
				userRepository.createDate(loggedInUser, potentialDate);
				eventPublisher.publishEvent(new DateCreatedEvent(this, loggedInUser.getUsername(), potentialDate.getUsername()));
				return new ResponseEntity<String>(HttpStatus.CREATED);
			}).orElse(new ResponseEntity<String>("Favorite added", HttpStatus.OK));
		});
	}

	@Override
	public List<UserEntity> findFavoritesForUser(String userId, Pageable pageable) {
		List<String> ids = userRepository.findFavoriteIdsForUser(userRepository.getUserReference(userId), pageable);
		return sortWithIdOrder(ids, userRepository.findUsersByIdWithPhotos(ids));
	}

	@Override
	public List<UserEntity> findLikedByForUser(String userId, Pageable pageable) {
		List<String> ids = userRepository.findLikedByIdsForUser(userRepository.getUserReference(userId), pageable);
		return sortWithIdOrder(ids, userRepository.findUsersByIdWithPhotos(ids));
	}

	@Override
	public List<UserEntity> findDatesForUser(String userId, Pageable pageable) {
		List<String> ids = userRepository.findDateIdsForUser(userRepository.getUserReference(userId), pageable);
		return sortWithIdOrder(ids, userRepository.findUsersByIdWithPhotos(ids));
	}

	@Override
	public UserEntity getReference(String id) {
		return userRepository.getUserReference(id);
	}

	private UserEntity getLoggedInUserForUpdate() {
		return (UserEntity) loadUserByUsername(getLoggedInUser().getUsername());
	}

	private void checkDoesNotAlreadyExist(UserEntity user) {
		if (userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
			throw new UserValidationException(USER_ALREADY_EXISTS);
		}
	}

	private boolean validatePhotoExistsInUserGallery(UserEntity user, String photo) {
		List<String> photos = user.getPhotos();
		return photos.contains(photo);
	}

	private void updateCache(UserEntity user) {
		UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
				user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}

	private List<UserEntity> sortWithIdOrder(List<String> ids, List<UserEntity> users) {
		Collections.sort(users,
				(left, right) -> Integer.compare(ids.indexOf(left.getId()), ids.indexOf(right.getId())));
		return users;
	}

}
