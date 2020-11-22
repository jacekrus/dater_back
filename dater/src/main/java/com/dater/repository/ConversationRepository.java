package com.dater.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dater.model.ConversationEntity;

@Repository
public interface ConversationRepository extends JpaRepository<ConversationEntity, String>, CustomConversationRepository {
	
	@Query("from ConversationEntity c left join fetch c.users where c.id=:id")
	Optional<ConversationEntity> findByIdWithUsers(@Param("id") String conversationId);
	
	@Query("from ConversationEntity c left join fetch c.userLastAccessedTime where c.id=:id")
	Optional<ConversationEntity> findByIdWithAccessTimes(@Param("id") String conversationId);

}
