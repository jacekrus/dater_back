package com.dater.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "dates")
public class DateEntity extends BaseEntity {
	
	@ManyToOne
	@JoinColumn(nullable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserEntity firstUser;
	
	@ManyToOne
	@JoinColumn(nullable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserEntity secondUser;
	
	@Column(updatable = false)
	private LocalDateTime createTime;
	
	public DateEntity() {}

	public DateEntity(UserEntity firstUser, UserEntity secondUser) {
		this.firstUser = firstUser;
		this.secondUser = secondUser;
		this.createTime = LocalDateTime.now();
	}

	public UserEntity getFirstUser() {
		return firstUser;
	}

	public void setFirstUser(UserEntity firstUser) {
		this.firstUser = firstUser;
	}

	public UserEntity getSecondUser() {
		return secondUser;
	}

	public void setSecondUser(UserEntity secondUser) {
		this.secondUser = secondUser;
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
		result = prime * result + ((firstUser == null) ? 0 : firstUser.hashCode());
		result = prime * result + ((secondUser == null) ? 0 : secondUser.hashCode());
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
		DateEntity other = (DateEntity) obj;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (firstUser == null) {
			if (other.firstUser != null)
				return false;
		} else if (!firstUser.equals(other.firstUser))
			return false;
		if (secondUser == null) {
			if (other.secondUser != null)
				return false;
		} else if (!secondUser.equals(other.secondUser))
			return false;
		return true;
	}
	
}
