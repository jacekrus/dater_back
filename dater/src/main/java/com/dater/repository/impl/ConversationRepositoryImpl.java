package com.dater.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Pageable;

import com.dater.model.ConversationMessageEntity;
import com.dater.model.ConversationEntity;
import com.dater.model.UserEntity;
import com.dater.repository.CustomConversationRepository;

public class ConversationRepositoryImpl extends AbstractRepository implements CustomConversationRepository {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public void addMessage(ConversationMessageEntity message) {
		em.persist(message);
	}

	@Override
	public List<ConversationEntity> findConversationsForUser(UserEntity user, Pageable pageable) {
		String queryString = "from ConversationEntity c where :user member of c.users";
		TypedQuery<ConversationEntity> query = em.createQuery(queryString, ConversationEntity.class).setParameter("user", user);
		applyPagination(query, pageable);
		return query.getResultList();
	}

	@Override
	public ConversationEntity getReference(String conversationId) {
		return em.getReference(ConversationEntity.class, conversationId);
	}

	@Override
	public List<ConversationMessageEntity> findMessagesForConversation(String conversationId, Pageable pageable) {
		String queryString = "from ConversationMessageEntity m join fetch m.sender where m.conversation.id = :id";
		TypedQuery<ConversationMessageEntity> query = em.createQuery(queryString, ConversationMessageEntity.class).setParameter("id", conversationId);
		applyPagination(query, pageable);
		return query.getResultList();
	}

}
