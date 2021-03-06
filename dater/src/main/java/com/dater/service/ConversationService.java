package com.dater.service;

import java.util.List;

import com.dater.model.ConversationEntity;
import com.dater.model.ConversationMessageEntity;
import com.dater.model.UserEntity;
import com.dater.repository.SkippingPageable;

public interface ConversationService {
	
	void addConversation(ConversationEntity conversation);
	
	void addUserToConversation(UserEntity user, String conversationId);
	
	void updateConversationAccessTime(UserEntity user, String conversationId);
	
	ConversationMessageEntity addMessageToConversation(UserEntity sender, String text, String conversationId);
	
	ConversationEntity findByIdWithUsers(String conversationId);
	
	ConversationEntity findByIdWithAccessTimes(String conversationId);
	
	ConversationEntity findByIdWithUserPhotos(String conversationId);
	
	ConversationEntity getReference(String conversationId);
	
	ConversationEntity findConversationByUsers(List<UserEntity> users, boolean createIfNotExists);
	
	List<ConversationEntity> findConversationsForUser(UserEntity user, SkippingPageable pageable);
	
	List<ConversationMessageEntity> findMessagesForConversation(String conversationId, UserEntity loggedInUser, SkippingPageable pageable);

}
