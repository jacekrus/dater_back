package com.dater.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.dater.event.MessageSendEvent;
import com.dater.exception.AuthorizationException;
import com.dater.exception.ConversationNotFoundException;
import com.dater.model.ConversationEntity;
import com.dater.model.ConversationMessageEntity;
import com.dater.model.UserEntity;
import com.dater.repository.ConversationRepository;
import com.dater.repository.SkippingPageable;
import com.dater.service.ConversationService;

import static com.dater.service.impl.ConversationMessages.*;

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
			throw new IllegalArgumentException(EMPTY_TEXT);
		}
		LocalDateTime currentTime = LocalDateTime.now();
		ConversationEntity conversation = findByIdWithUsers(conversationId);
		ConversationMessageEntity message = new ConversationMessageEntity(sender, conversation, currentTime, text);
		conversation.setLatestMessageTime(currentTime);
		conversation.getUserLastAccessedTime().put(sender.getId(), currentTime);
		conversationRepository.addMessage(message);
		eventPublisher.publishEvent(new MessageSendEvent(this, message));
		return message;
	}

	@Override
	public ConversationEntity findByIdWithUsers(String conversationId) {
		return conversationRepository.findByIdWithUsers(conversationId).orElseThrow(
				() -> new ConversationNotFoundException(String.format(CONVERSATION_NOT_FOUND_BY_ID, conversationId)));
	}
	

	@Override
	public ConversationEntity findByIdWithUserPhotos(String conversationId) {
		ConversationEntity conversation = findByIdWithUsers(conversationId);
		conversation.getUsers().forEach(usr -> Hibernate.initialize(usr.getPhotos()));
		return conversation;
	}

	@Override
	public List<ConversationEntity> findConversationsForUser(UserEntity user, SkippingPageable pageable) {
		List<ConversationEntity> conversations = conversationRepository.findConversationsForUser(user, pageable);
		conversations.stream().forEach(c -> {
			c.getUsers().forEach(u -> Hibernate.initialize(u.getPhotos()));
			LocalDateTime lastAccessTime = c.getUserLastAccessedTime().getOrDefault(user.getId(), c.getCreateTime());
			c.setHasUnreadMessages(lastAccessTime.isBefore(c.getLatestMessageTime()));
		});
		return conversations;
	}

	@Override
	@Transactional
	public void addUserToConversation(UserEntity user, String conversationId) {
		ConversationEntity conversation = findByIdWithUsers(conversationId);
		conversation.addUser(user, LocalDateTime.now());
	}

	@Override
	public ConversationEntity getReference(String conversationId) {
		return conversationRepository.getReference(conversationId);
	}

	@Override
	@Transactional
	public List<ConversationMessageEntity> findMessagesForConversation(String conversationId, UserEntity loggedInUser, SkippingPageable pageable) {
		ConversationEntity conversation = findByIdWithUserPhotos(conversationId);
		if(!conversation.getUsers().contains(loggedInUser) && !loggedInUser.getRole().equals("ADMIN")) {
			throw new AuthorizationException(USER_NOT_ALLOWED_VIEW_MESSAGES);
		}
		conversation.getUserLastAccessedTime().put(loggedInUser.getId(), LocalDateTime.now());
		return conversationRepository.findMessagesForConversation(conversationId, pageable);
	}

	@Override
	@Transactional
	public ConversationEntity findConversationByUsers(List<UserEntity> users, boolean createIfNotExists) {
		if(users.size() != 2) {
			throw new IllegalArgumentException("For now only two element list is accepted.");
		}
		Optional<ConversationEntity> conversation = conversationRepository.findConversationByUsers(users);
		LocalDateTime currentTime = LocalDateTime.now();
		conversation.ifPresent(conv -> conv.setLatestMessageTime(currentTime));
		if(createIfNotExists && conversation.isEmpty()) {
			Map<String, LocalDateTime> userAccTime = Map.of(users.get(0).getId(), currentTime, users.get(1).getId(), currentTime);
			ConversationEntity newConversation = new ConversationEntity(Set.of(users.get(0), users.get(1)), currentTime, userAccTime);
			newConversation.setLatestMessageTime(currentTime);
			conversationRepository.save(newConversation);
			return newConversation;
		}
		return conversation.orElseThrow(() -> new ConversationNotFoundException(CONVERSATION_NOT_FOUND_BY_USERS));
	}

	@Override
	@Transactional
	public void updateConversationAccessTime(UserEntity user, String conversationId) {
		ConversationEntity conversation = findByIdWithAccessTimes(conversationId);
		conversation.getUserLastAccessedTime().put(user.getId(), LocalDateTime.now());
	}

	@Override
	public ConversationEntity findByIdWithAccessTimes(String conversationId) {
		return conversationRepository.findByIdWithAccessTimes(conversationId).orElseThrow(
				() -> new ConversationNotFoundException(String.format(CONVERSATION_NOT_FOUND_BY_ID, conversationId)));
	}

}
