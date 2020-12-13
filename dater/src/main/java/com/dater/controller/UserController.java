package com.dater.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dater.model.UserEntity;
import com.dater.service.UserService;
import com.fasterxml.jackson.databind.node.TextNode;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private final UserService userService;
	private static final String JSON = "application/json";
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping(value = "/recommended", produces = JSON)
	public List<UserEntity> getRecommendedUsers() {
		return userService.findRecommendedForUser(getLoggedInUser().getId());
	}
	
	@GetMapping(value = "/self", produces = JSON)
	public UserEntity getLoggedInUser() {
		return userService.getLoggedInUser();
	}
	
	@GetMapping(produces = JSON)
	public UserEntity getUser(@RequestParam(name = "id") String id) {
		return userService.findUserById(id);
	}
	
	@GetMapping(value = "/favorites", produces = JSON)
	public List<UserEntity> getFavorites(Pageable pageable) {
		return userService.findFavoritesForUser(getLoggedInUser().getId(), pageable);
	}
	
	@GetMapping(value = "/likedby", produces = JSON)
	public List<UserEntity> getLikedBy(Pageable pageable) {
		return userService.findLikedByForUser(getLoggedInUser().getId(), pageable);
	}
	
	@GetMapping(value = "/dates", produces = JSON)
	public List<UserEntity> getDates(Pageable pageable) {
		return userService.findDatesForUser(getLoggedInUser().getId(), pageable);
	}
	
	@PostMapping(consumes = JSON, produces = JSON)
	public List<UserEntity> getUsers(@RequestBody UserEntity exampleUser, Pageable pageable) {
		return userService.findUsers(Example.of(exampleUser), pageable);
	}

	@PostMapping(value = "/add", consumes = JSON)
	public void addUser(@RequestBody UserEntity user) {
		userService.addUser(user);
	}
	
	@PostMapping(value = "/validate", consumes = JSON)
	public void validateUser(@RequestBody UserEntity user) {
		userService.validateUser(user);
	}
	
	@PutMapping(value = "/like")
	public ResponseEntity<String> addFavoriteUser(@RequestParam(name = "id") String id) {
		return userService.addFavoriteUser(id);
	}
	
	@PutMapping
	public UserEntity updateUser(@RequestBody UserEntity user) {
		return userService.updateUser(user);
	}
	
	@PutMapping(value = "/photos")
	public UserEntity setProfilePhoto(@RequestBody TextNode photo) {
		return userService.setProfilePhoto(photo.asText());
	}
	
	@PatchMapping(value = "/photos")
	public UserEntity removePhoto(@RequestBody TextNode photo) {
		return userService.removePhoto(photo.asText());
	}
	
	@DeleteMapping
	public void deleteUser(@RequestParam(name = "id") String id) {
		userService.removeUser(id);
	}
	
}
