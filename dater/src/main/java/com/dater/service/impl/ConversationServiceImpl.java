package com.dater.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dater.exception.ConversationNotFoundException;
import com.dater.model.ConversationMessageEntity;
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
	public void addMessage(ConversationMessageEntity message) {
		conversationRepository.addMessage(message);
	}

	@Override
	public ConversationEntity findById(String conversationId) {
		return conversationRepository.findById(conversationId).orElseThrow(() -> new ConversationNotFoundException("conversation with id: [" +  conversationId +"] not found."));
	}

	@Override
	public List<ConversationEntity> findConversationsForUser(UserEntity user, Pageable pageable) {
		List<ConversationEntity> conversations = conversationRepository.findConversationsForUser(user, pageable);
		conversations.stream().forEach(c -> c.getUsers().forEach(u -> Hibernate.initialize(u.getPhotos())));
		return conversations;
	}

	@Override
	@Transactional
	public void addUserToConversation(UserEntity user, String conversationId) {
		ConversationEntity conversation = this.findById(conversationId);
		conversation.addUser(user);
	}

	@Override
	public ConversationEntity getReference(String conversationId) {
		return conversationRepository.getReference(conversationId);
	}

	@Override
	public List<ConversationMessageEntity> findMessagesForConversation(String conversationId, Pageable pageable) {
		List<ConversationMessageEntity> messages = conversationRepository.findMessagesForConversation(conversationId, pageable);
		messages.stream().forEach(c -> Hibernate.initialize(c.getSender().getPhotos()));
		return messages;
	}

	@Override
	public ConversationEntity findConversationByUsers(List<UserEntity> users, boolean createIfNotExists) {
		if(users.size() != 2) {
			throw new IllegalArgumentException("For now only two element list is accepted");
		}
		Optional<ConversationEntity> conversation = conversationRepository.findConversationByUsers(users);
		if(createIfNotExists && conversation.isEmpty()) {
			ConversationEntity newConversation = new ConversationEntity(Set.of(users.get(0), users.get(1)), LocalDateTime.now(), UUID.randomUUID().toString());
			conversationRepository.save(newConversation);
			return newConversation;
		}
		return conversation.orElseThrow(() -> new ConversationNotFoundException("Conversation for given users not found"));
	}

}
