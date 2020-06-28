package com.dater.controller;

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
		return userService.findRecommendedForCurrentUser();
	}
	
	@GetMapping(value = "/me", produces = JSON)
	public UserEntity getLoggedInUser() {
		return userService.getLoggedInUser();
	}
	
	@GetMapping(value = "/details", produces = JSON)
	public UserEntity getUserDetails(@RequestParam(name = "id") String id) {
		return userService.findUserById(id);
	}
	
	@PostMapping(consumes = JSON, produces = JSON)
	public List<UserEntity> getUsersPaginated(@RequestBody UserEntity exampleUser, Pageable pageable) {
		return userService.findUsers(Example.of(exampleUser), pageable);
	}

	@PostMapping(value = "/add", consumes = JSON)
	public void addUser(@RequestBody UserEntity user) {
		userService.addUser(user);
	}
	
	@DeleteMapping
	public void test(@RequestParam(name = "id") String id) {
		userService.removeUser(id);
	}
	
	@PutMapping
	public void updateUser(@RequestBody UserEntity user) {
		userService.updateUser(user);
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
