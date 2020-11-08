package com.dater.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.dater.model.ConversationMessageEntity;
import com.dater.model.ConversationEntity;
import com.dater.model.UserEntity;

public interface ConversationService {
	
	void addConversation(ConversationEntity conversation);
	
	void addUserToConversation(UserEntity user, String conversationId);
	
	ConversationMessageEntity addMessageToConversation(UserEntity sender, String text, String conversationId);
	
	ConversationEntity findById(String conversationId);
	
	ConversationEntity getReference(String conversationId);
	
	ConversationEntity findConversationByUsers(List<UserEntity> users, boolean createIfNotExists);
	
	List<ConversationEntity> findConversationsForUser(UserEntity user, Pageable pageable);
	
	List<ConversationMessageEntity> findMessagesForConversation(String conversationId, Pageable pageable);

}
