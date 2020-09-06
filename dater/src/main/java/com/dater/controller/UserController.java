package com.dater.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dater.model.Gender;
import com.dater.model.UserEntity;
import com.dater.service.UserService;

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
	
	@GetMapping(value = "/me", produces = JSON)
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
	
	@PutMapping(value = "/like")
	public void addFavoriteUser(@RequestParam(name = "id") String id) {
		userService.addFavoriteUser(id);
	}
	
	@PutMapping
	public UserEntity updateUser(@RequestBody UserEntity user) {
		return userService.updateUser(user);
	}
	
	@DeleteMapping
	public void deleteUser(@RequestParam(name = "id") String id) {
		userService.removeUser(id);
	}
	
	@EventListener(ApplicationStartedEvent.class)
	public void onStartup() {
//		UserEntity admin = new UserEntity("admin", "admin@datr.com", "admin", LocalDate.now(), "Katowice", "ADMIN", Gender.MALE, Gender.FEMALE,
//				Arrays.asList("https://i.redd.it/7tp05fk4rp021.jpg", "https://www.wykop.pl/cdn/c3201142/comment_xGzPTCePZe26dWY0HySM0QCZGyAyS0HC.jpg"));
//		userService.addUser(admin);
//		
//		UserEntity user = new UserEntity("user", "user@datr.com", "user", LocalDate.now(), "Katowice", "USER", Gender.MALE, Gender.FEMALE,
//		Arrays.asList("https://www.wykop.pl/cdn/c3201142/comment_xGzPTCePZe26dWY0HySM0QCZGyAyS0HC.jpg", "https://i.redd.it/7tp05fk4rp021.jpg"));
//		userService.addUser(user);
//		
//		UserEntity user2 = new UserEntity("user2", "user2@datr.com", "user2", LocalDate.now(), "Katowice", "USER", Gender.MALE, Gender.MALE,
//				Arrays.asList("https://www.wykop.pl/cdn/c3201142/comment_xGzPTCePZe26dWY0HySM0QCZGyAyS0HC.jpg", "https://i.redd.it/7tp05fk4rp021.jpg"));
//		userService.addUser(user2);
//		
//		UserEntity user3 = new UserEntity("user3", "user3@datr.com", "user3", LocalDate.now(), "Katowice", "USER", Gender.FEMALE, Gender.MALE,
//				Arrays.asList("https://www.wykop.pl/cdn/c3201142/comment_xGzPTCePZe26dWY0HySM0QCZGyAyS0HC.jpg", "https://i.redd.it/7tp05fk4rp021.jpg"));
//		userService.addUser(user3);
//		
//		UserEntity user4 = new UserEntity("user4", "user4@datr.com", "user4", LocalDate.now(), "Katowice", "USER", Gender.FEMALE, Gender.MALE,
//				Arrays.asList("https://www.wykop.pl/cdn/c3201142/comment_xGzPTCePZe26dWY0HySM0QCZGyAyS0HC.jpg", "https://i.redd.it/7tp05fk4rp021.jpg"));
//		userService.addUser(user4);
//		
//		for(int i = 0; i < 90; i++) {
//			userService.addUser(new UserEntity("girl" + i, "girl" + i + "@datr.com", "user" + i, LocalDate.now(), "Katowice", "USER", Gender.FEMALE, Gender.MALE,
//					Arrays.asList("https://www.wykop.pl/cdn/c3201142/comment_xGzPTCePZe26dWY0HySM0QCZGyAyS0HC.jpg", "https://i.redd.it/7tp05fk4rp021.jpg")));
//		}
	}
	
}
