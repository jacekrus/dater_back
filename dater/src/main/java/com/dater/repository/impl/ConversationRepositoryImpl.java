package com.dater.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Pageable;

import com.dater.model.ChatMessageEntity;
import com.dater.model.ConversationEntity;
import com.dater.model.UserEntity;
import com.dater.repository.CustomConversationRepository;

public class ConversationRepositoryImpl extends AbstractRepository implements CustomConversationRepository {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public void addMessage(ChatMessageEntity message) {
		em.persist(message);
	}

	@Override
	public List<ConversationEntity> findConversationsForUser(UserEntity user, Pageable pageable) {
		String queryString = "from ConversationEntity c where :user member of c.users";
		TypedQuery<ConversationEntity> query = em.createQuery(queryString, ConversationEntity.class).setParameter("user", user);
		applyPagination(query, pageable);
		return query.getResultList();
	}

}
