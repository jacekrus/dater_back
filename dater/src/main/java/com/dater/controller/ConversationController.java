package com.dater.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dater.model.ConversationEntity;
import com.dater.model.ConversationMessageEntity;
import com.dater.model.UserEntity;
import com.dater.repository.impl.PageWithSkipRequest;
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
	
	@GetMapping
	public List<ConversationEntity> findConversationsForUser(Pageable pageable) {
		return convSvc.findConversationsForUser(userSvc.getLoggedInUser(), pageable);
	}
	
	@PostMapping
	public ConversationEntity findConversationByUsers(@RequestBody List<UserEntity> users, 
			@RequestParam(name = "create", required = false, defaultValue = "false") boolean create) {
		return convSvc.findConversationByUsers(users, create);
	}
	
	@GetMapping(value = "/messages")
	public List<ConversationMessageEntity> findMessagesForConversation(@RequestParam(name = "id") String conversationId, 
			@RequestParam(name = "skip", required = false, defaultValue = "0") int skip, Pageable pageable) {
		return convSvc.findMessagesForConversation(conversationId, new PageWithSkipRequest(pageable, skip));
	}
	
	@PostMapping(value = "/messages")
	public ConversationMessageEntity addMessageToConversation(@RequestBody TextNode message, @RequestParam(name= "id") String conversationId) {
		return convSvc.addMessageToConversation(userSvc.getLoggedInUser(), message.asText(), conversationId);
	}

}
