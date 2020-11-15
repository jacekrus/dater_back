package com.dater.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
	
	@PostMapping(value = "/validate", consumes = JSON)
	public void validateUser(@RequestBody UserEntity user) {
		userService.validateUser(user);
	}
	
	@PutMapping(value = "/like")
	public ResponseEntity<Void> addFavoriteUser(@RequestParam(name = "id") String id) {
		boolean dateCreated = userService.addFavoriteUser(id);
		return dateCreated ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.OK);
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
	
	@EventListener(ApplicationStartedEvent.class)
	public void onStartup() {
//		UserEntity user = new UserBuilder().withUsername("chattest")
//						 .withEmail("chattest@gmail.com")
//						 .withPassword("chattest")
//						 .bornOn(LocalDate.of(1996, 11, 12))
//						 .withLocation("Konin")
//						 .withRole("USER")
//						 .withGender(Gender.FEMALE)
//						 .withDescription("First chater")
//						 .withPhotos(Arrays.asList("https://i.redd.it/7tp05fk4rp021.jpg", "https://www.wykop.pl/cdn/c3201142/comment_xGzPTCePZe26dWY0HySM0QCZGyAyS0HC.jpg"))
//						 .build();
//		UserEntity user2 = new UserBuilder().withUsername("chattest2")
//				 .withEmail("chattest2@gmail.com")
//				 .withPassword("chattest2")
//				 .bornOn(LocalDate.of(1996, 11, 12))
//				 .withLocation("Konin")
//				 .withRole("USER")
//				 .withGender(Gender.FEMALE)
//				 .withDescription("Second chater")
//				 .withPhotos(Arrays.asList("https://i.redd.it/7tp05fk4rp021.jpg", "https://www.wykop.pl/cdn/c3201142/comment_xGzPTCePZe26dWY0HySM0QCZGyAyS0HC.jpg"))
//				 .build();
//		LocalDateTime currentDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
//		ConversationEntity conversation = new ConversationEntity(Set.of(user, user2), currentDateTime, UUID.randomUUID().toString());
//		userService.addUser(user);
//		userService.addUser(user2);
//		svc.addConversation(conversation);
		
		
//		LocalDateTime currentDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
//		UserEntity user = userService.findUserById("4028808c753b9a2501753b9a316b0000");
//		UserEntity user2 = userService.findUserById("4028808c753b9a2501753b9a31e00001");
//		ConversationEntity conversation = svc.findById("4028808c753b9a2501753b9a31e80002");
//		svc.addMessage(new ChatMessageEntity(user, conversation, currentDateTime, "Hello from user chater"));
//		svc.addMessage(new ChatMessageEntity(user2, conversation, currentDateTime.plusSeconds(2), "Hello from user chater2"));
		
		
//		svc.addUserToConversation(userService.findUserById("MIE5kFdERB5wtFEMlEtQQD"), "4028808c753b9a2501753b9a31e80002");
//		svc.addUserToConversation(userService.findUserById("oJ8o5081Q1IMZ4MBN95x8D"), "4028808c753b9a2501753b9a31e80002");
//		svc.addUserToConversation(userService.findUserById("FZpoxscBQsoFRwctxcYVBC"), "4028808c753b9a2501753b9a31e80002");
//		svc.addUserToConversation(userService.findUserById("JZc1UMAxRVJ0hdM4kp9FFD"), "4028808c753b9a2501753b9a31e80002");
//		svc.addUserToConversation(userService.findUserById("lttBspM0Q00BRBMwhAgsID"), "4028808c753b9a2501753b9a31e80002");
//		svc.addUserToConversation(userService.findUserById("ZsEU1o4gQAwoQ9BpxZUlhA"), "4028808c753b9a2501753b9a31e80002");
//		svc.addUserToConversation(userService.findUserById("wp4gpMsZQgl9YsQ00cx5MA"), "4028808c753b9a2501753b9a31e80002");
//		svc.addUserToConversation(userService.findUserById("R0BosVFMRstYZ4Zt5kthtB"), "4028808c753b9a2501753b9a31e80002");
//		svc.addUserToConversation(userService.findUserById("kgJhk9cURsEggwQEJ9UtMA"), "4028808c753b9a2501753b9a31e80002");
//		svc.addUserToConversation(userService.findUserById("VEI45NAMRB0NRMRoZZtQpD"), "4028808c753b9a2501753b9a31e80002");
//		svc.addUserToConversation(userService.findUserById("cVlEFNYEQkxwxw50tc9F9C"), "4028808c753b9a2501753b9a31e80002");
		
		
		
//		UserEntity main = userService.findUserById("4028808c753b9a2501753b9a31e00001");
//		UserEntity first = userService.findUserById("ZsEU1o4gQAwoQ9BpxZUlhA");
//		UserEntity second = userService.findUserById("oJ8o5081Q1IMZ4MBN95x8D");
//		UserEntity third = userService.findUserById("FZpoxscBQsoFRwctxcYVBC");
//		UserEntity fourth = userService.findUserById("JZc1UMAxRVJ0hdM4kp9FFD");
//		UserEntity fivth = userService.findUserById("lttBspM0Q00BRBMwhAgsID");
//		UserEntity sixth = userService.findUserById("wp4gpMsZQgl9YsQ00cx5MA");
//		UserEntity seventh = userService.findUserById("kgJhk9cURsEggwQEJ9UtMA");
//		UserEntity eighth = userService.findUserById("VEI45NAMRB0NRMRoZZtQpD");
//		UserEntity nineth = userService.findUserById("cVlEFNYEQkxwxw50tc9F9C");
//		LocalDateTime currentDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
//		svc.addConversation(new ConversationEntity(Set.of(first, second), currentDateTime, UUID.randomUUID().toString()));
//		svc.addConversation(new ConversationEntity(Set.of(main, second), currentDateTime.plusSeconds(1), UUID.randomUUID().toString()));
//		svc.addConversation(new ConversationEntity(Set.of(main, third), currentDateTime.plusSeconds(2), UUID.randomUUID().toString()));
//		svc.addConversation(new ConversationEntity(Set.of(main, fourth), currentDateTime.plusSeconds(3), UUID.randomUUID().toString()));
//		svc.addConversation(new ConversationEntity(Set.of(main, fivth), currentDateTime.plusSeconds(4), UUID.randomUUID().toString()));
//		svc.addConversation(new ConversationEntity(Set.of(main, sixth), currentDateTime.plusSeconds(5), UUID.randomUUID().toString()));
//		svc.addConversation(new ConversationEntity(Set.of(main, seventh), currentDateTime.plusSeconds(6), UUID.randomUUID().toString()));
//		svc.addConversation(new ConversationEntity(Set.of(main, eighth), currentDateTime.plusSeconds(7), UUID.randomUUID().toString()));
//		svc.addConversation(new ConversationEntity(Set.of(main, nineth), currentDateTime.plusSeconds(8), UUID.randomUUID().toString()));
		
	}
	
}
