package com.dater.repository;

import java.util.List;
import java.util.Optional;

import com.dater.model.ConversationEntity;
import com.dater.model.ConversationMessageEntity;
import com.dater.model.UserEntity;

public interface CustomConversationRepository {
	
	void addMessage(ConversationMessageEntity message);
	
	ConversationEntity getReference(String conversationId);
	
	Optional<ConversationEntity> findConversationByUsers(List<UserEntity> users);
	
	List<ConversationEntity> findConversationsForUser(UserEntity user, SkippingPageable pageable);
	
	List<ConversationMessageEntity> findMessagesForConversation(String conversationId, SkippingPageable pageable);

}
