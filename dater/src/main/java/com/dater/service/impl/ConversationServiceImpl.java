package com.dater.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dater.event.MessageSendEvent;
import com.dater.exception.ConversationNotFoundException;
import com.dater.model.ConversationEntity;
import com.dater.model.ConversationMessageEntity;
import com.dater.model.UserEntity;
import com.dater.repository.ConversationRepository;
import com.dater.repository.SkippingPageable;
import com.dater.service.ConversationService;

@Service
public class ConversationServiceImpl implements ConversationService {
	
	private final ConversationRepository conversationRepository;
	private final ApplicationEventPublisher eventPublisher;

	@Autowired
	public ConversationServiceImpl(ConversationRepository conversationRepository, ApplicationEventPublisher eventPublisher) {
		this.conversationRepository = conversationRepository;
		this.eventPublisher = eventPublisher;
	}

	@Override
	public void addConversation(ConversationEntity conversation) {
		conversationRepository.save(conversation);
	}

	@Override
	@Transactional
	public ConversationMessageEntity addMessageToConversation(UserEntity sender, String text, String conversationId) {
		if(text == null || text.isBlank()) {
			throw new IllegalArgumentException("Supplied empty message text");
		}
		LocalDateTime currentTime = LocalDateTime.now();
		ConversationEntity conversation = conversationRepository.findByIdWithUsers(conversationId).orElseThrow(() -> new ConversationNotFoundException("conversation with id: [" +  conversationId +"] not found."));
		ConversationMessageEntity message = new ConversationMessageEntity(sender, conversation, currentTime, text);
		conversationRepository.addMessage(message);
		conversation.setLatestMessageTime(currentTime);
		eventPublisher.publishEvent(new MessageSendEvent(this, message));
		return message;
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
	public List<ConversationMessageEntity> findMessagesForConversation(String conversationId, SkippingPageable pageable) {
		List<ConversationMessageEntity> messages = conversationRepository.findMessagesForConversation(conversationId, pageable);
		messages.stream().forEach(c -> Hibernate.initialize(c.getSender().getPhotos()));
		return messages;
	}

	@Override
	@Transactional
	public ConversationEntity findConversationByUsers(List<UserEntity> users, boolean createIfNotExists) {
		if(users.size() != 2) {
			throw new IllegalArgumentException("For now only two element list is accepted");
		}
		Optional<ConversationEntity> conversation = conversationRepository.findConversationByUsers(users);
		LocalDateTime currentTime = LocalDateTime.now();
		conversation.ifPresent(conv -> conv.setLatestMessageTime(currentTime));
		if(createIfNotExists && conversation.isEmpty()) {
			ConversationEntity newConversation = new ConversationEntity(Set.of(users.get(0), users.get(1)), currentTime, UUID.randomUUID().toString());
			newConversation.setLatestMessageTime(currentTime);
			conversationRepository.save(newConversation);
			return newConversation;
		}
		return conversation.orElseThrow(() -> new ConversationNotFoundException("Conversation for given users not found"));
	}

}
