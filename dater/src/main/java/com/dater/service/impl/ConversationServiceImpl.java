package com.dater.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dater.exception.ConversationNotFoundException;
import com.dater.model.ChatMessageEntity;
import com.dater.model.ConversationEntity;
import com.dater.model.UserEntity;
import com.dater.repository.ConversationRepository;
import com.dater.service.ConversationService;

@Service
public class ConversationServiceImpl implements ConversationService {
	
	private final ConversationRepository conversationRepository;

	@Autowired
	public ConversationServiceImpl(ConversationRepository conversationRepository) {
		this.conversationRepository = conversationRepository;
	}

	@Override
	public void addConversation(ConversationEntity conversation) {
		conversationRepository.save(conversation);
	}

	@Override
	@Transactional
	public void addMessage(ChatMessageEntity message) {
		conversationRepository.addMessage(message);
	}

	@Override
	public ConversationEntity findById(String conversationId) {
		return conversationRepository.findById(conversationId).orElseThrow(() -> new ConversationNotFoundException("conversation with id: [" +  conversationId +"] not found."));
	}

	@Override
	public List<ConversationEntity> findConversationsForUser(UserEntity user, Pageable pageable) {
		List<ConversationEntity> conversations = conversationRepository.findConversationsForUser(user, pageable);
		conversations.stream().findFirst().ifPresent(c -> c.getUsers().forEach(u -> Hibernate.initialize(u.getPhotos())));
		return conversations;
	}

	@Override
	@Transactional
	public void addUserToConversation(UserEntity user, String conversationId) {
		ConversationEntity conversation = this.findById(conversationId);
		conversation.addUser(user);
	}

}
