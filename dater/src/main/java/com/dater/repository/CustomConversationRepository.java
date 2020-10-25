package com.dater.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.dater.model.ConversationMessageEntity;
import com.dater.model.ConversationEntity;
import com.dater.model.UserEntity;

public interface CustomConversationRepository {
	
	void addMessage(ConversationMessageEntity message);
	
	ConversationEntity getReference(String conversationId);
	
	List<ConversationEntity> findConversationsForUser(UserEntity user, Pageable pageable);
	
	List<ConversationMessageEntity> findMessagesForConversation(String conversationId, Pageable pageable);

}
