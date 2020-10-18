package com.dater.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.dater.model.ChatMessageEntity;
import com.dater.model.ConversationEntity;
import com.dater.model.UserEntity;

public interface ConversationService {
	
	void addConversation(ConversationEntity conversation);
	
	ConversationEntity findById(String conversationId);
	
	void addUserToConversation(UserEntity user, String conversationId);
	
	List<ConversationEntity> findConversationsForUser(UserEntity user, Pageable pageable);
	
	void addMessage(ChatMessageEntity message);

}
