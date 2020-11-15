package com.dater.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Pageable;

import com.dater.model.DateEntity;
import com.dater.model.FavoriteEntity;
import com.dater.model.Gender;
import com.dater.model.UserEntity;
import com.dater.repository.CustomUserRepository;

public class UserRepositoryImpl extends AbstractRepository implements CustomUserRepository {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<UserEntity> findRecommended(Gender userGender, Optional<Gender> preferredGender) {
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
	

	@Override
	public UserEntity getUserReference(String userId) {
		return em.getReference(UserEntity.class, userId);
	}
	
	@Override
	public void addFavorite(UserEntity owner, UserEntity ownersFavorite) {
		em.persist(new FavoriteEntity(owner, ownersFavorite));
	}
	
	@Override
	public boolean isFavorite(UserEntity user, UserEntity favorite) {
		String queryString = "select count(f) > 0 from FavoriteEntity f where f.owner = :owner and f.ownersFavorite = :ownersFavorite";
		TypedQuery<Boolean> query = em.createQuery(queryString, Boolean.class)
				.setParameter("owner", user)
				.setParameter("ownersFavorite", favorite);
		return query.getSingleResult();
	}
	
	@Override
	public List<String> findDateIdsForUser(UserEntity user, Pageable pageable) {	
		String queryString = "select u.id from UserEntity u "
				+ "join DateEntity d on d.firstUser.id = u.id or d.secondUser.id = u.id "
				+ "where (d.firstUser.id = :id or d.secondUser.id = :id) and u.id != :id order by d.createTime desc nulls last";
		TypedQuery<String> query = em.createQuery(queryString, String.class).setParameter("id", user.getId());
		applyPagination(query, pageable);
		return query.getResultList();
	}
	
	@Override
	public void createDate(UserEntity firstUser, UserEntity secondUser) {
		em.persist(new DateEntity(firstUser, secondUser));
	}
	
	@Override
	public List<String> findLikedByIdsForUser(UserEntity user, Pageable pageable) {
		String queryString = "select f.owner.id from FavoriteEntity f where f.ownersFavorite = :ownersFavorite order by f.createTime desc nulls last";
		TypedQuery<String> query = em.createQuery(queryString, String.class).setParameter("ownersFavorite", user);
		applyPagination(query, pageable);
		return query.getResultList();
	}


	@Override
	public List<UserEntity> findUsersByIdWithPhotos(Collection<String> ids) {
		String queryString = "select distinct u from UserEntity u left join fetch u.photos where u.id in :ids";
		TypedQuery<UserEntity> query = em.createQuery(queryString, UserEntity.class).setParameter("ids", ids);
		return query.getResultList();
	}

	@Override
	public List<String> findFavoriteIdsForUser(UserEntity user, Pageable pageable) {
		String queryString = "select f.ownersFavorite.id from FavoriteEntity f where f.owner = :owner order by f.createTime desc nulls last";
		TypedQuery<String> query = em.createQuery(queryString, String.class).setParameter("owner", user);
		applyPagination(query, pageable);
		return query.getResultList();
	}
	
}
