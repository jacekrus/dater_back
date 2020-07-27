package com.dater.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "favorites")
public class FavoriteEntity extends BaseEntity {

	@ManyToOne
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserEntity owner; 
	
	@ManyToOne
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserEntity ownersFavorite;
	
	public FavoriteEntity() {}

	public FavoriteEntity(UserEntity owner, UserEntity ownersFavorite) {
		super(null);
		this.owner = owner;
		this.ownersFavorite = ownersFavorite;
	}

	public UserEntity getOwner() {
		return owner;
	}

	public void setOwner(UserEntity owner) {
		this.owner = owner;
	}

	public UserEntity getOwnersFavorite() {
		return ownersFavorite;
	}

	public void setOwnersFavorite(UserEntity ownersFavorite) {
		this.ownersFavorite = ownersFavorite;
	}

}
