package com.dater.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dater.model.ConversationEntity;
import com.dater.model.ConversationMessageEntity;
import com.dater.model.UserEntity;
import com.dater.service.ConversationService;
import com.dater.service.UserService;
import com.fasterxml.jackson.databind.node.TextNode;

@RestController
@RequestMapping("/conversations")
public class ConversationController {
	
	private final ConversationService convSvc;
	
	private final UserService userSvc;

	@Autowired
	public ConversationController(ConversationService convSvc, UserService userSvc) {
		this.convSvc = convSvc;
		this.userSvc = userSvc;
	}

	@MessageMapping("/chat.register")
	@SendTo("/topic/public")
	public ConversationMessageEntity register(@Payload ConversationMessageEntity msg) {
		return msg;
	}
	
	@MessageMapping("/chat.send")
	@SendTo("/topic/public")
	public ConversationMessageEntity sendMessage(@Payload ConversationMessageEntity msg) {
		return msg;
	}
	
	@GetMapping
	public List<ConversationEntity> findConversationsForUser(Pageable pageable) {
		return convSvc.findConversationsForUser(userSvc.getLoggedInUser(), pageable);
	}
	
	@PostMapping
	public ConversationEntity findConversationByUsers(@RequestBody List<UserEntity> users, @RequestParam(name = "create", required = false, defaultValue = "false") boolean create) {
		return convSvc.findConversationByUsers(users, create);
	}
	
	@GetMapping(value = "/messages")
	public List<ConversationMessageEntity> findMessagesForConversation(@RequestParam(name = "id") String conversationId, Pageable pageable) {
		return convSvc.findMessagesForConversation(conversationId, pageable);
	}
	
	@PostMapping(value = "/messages")
	public void addMessageToConversation(@RequestBody TextNode message, @RequestParam(name = "id") String conversationId) {
		ConversationEntity conversation = convSvc.getReference(conversationId);
		UserEntity sender = userSvc.getLoggedInUser();
		LocalDateTime currentDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
		convSvc.addMessage(new ConversationMessageEntity(sender, conversation, currentDateTime, message.asText()));
	}

}
