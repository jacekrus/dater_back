package com.dater.model;

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
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserEntity firstUser;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserEntity secondUser;
	
	public DateEntity() {}

	public DateEntity(UserEntity firstUser, UserEntity secondUser) {
		this.firstUser = firstUser;
		this.secondUser = secondUser;
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
	
}
