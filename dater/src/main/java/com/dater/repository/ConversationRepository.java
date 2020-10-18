package com.dater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dater.model.ConversationEntity;

@Repository
public interface ConversationRepository extends JpaRepository<ConversationEntity, String>, CustomConversationRepository {

}
