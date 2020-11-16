package com.dater.model;

import java.time.LocalDateTime;

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
	@JoinColumn(nullable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserEntity owner; 
	
	@ManyToOne
	@JoinColumn(nullable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserEntity ownersFavorite;
	
	private LocalDateTime createTime;
	
	public FavoriteEntity() {}

	public FavoriteEntity(UserEntity owner, UserEntity ownersFavorite) {
		this.owner = owner;
		this.ownersFavorite = ownersFavorite;
		this.createTime = LocalDateTime.now();
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

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((ownersFavorite == null) ? 0 : ownersFavorite.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FavoriteEntity other = (FavoriteEntity) obj;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (ownersFavorite == null) {
			if (other.ownersFavorite != null)
				return false;
		} else if (!ownersFavorite.equals(other.ownersFavorite))
			return false;
		return true;
	}
	
}
