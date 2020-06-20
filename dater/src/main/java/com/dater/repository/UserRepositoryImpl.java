package com.dater.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.dater.model.Gender;
import com.dater.model.UserEntity;

public class UserRepositoryImpl implements CustomUserRepository {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<UserEntity> findRecommendedForCurrentUser(Gender userGender, Optional<Gender> preferredGender) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT u from UserEntity u");
		queryBuilder.append(" where u.preference = :userGender");
		preferredGender.ifPresent(gen -> queryBuilder.append(" and u.gender = :preference"));
		queryBuilder.append(" order by random()");
		TypedQuery<UserEntity> query = em.createQuery(queryBuilder.toString(), UserEntity.class);
		query.setParameter("userGender", userGender);
		preferredGender.ifPresent(gen -> query.setParameter("preference", gen));
		return query.setMaxResults(9).getResultList();
	}

}
