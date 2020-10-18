package com.dater.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.dater.model.ChatMessageEntity;
import com.dater.model.ConversationEntity;
import com.dater.model.UserEntity;

public interface CustomConversationRepository {
	
	void addMessage(ChatMessageEntity message);
	
	List<ConversationEntity> findConversationsForUser(UserEntity user, Pageable pageable);

}
