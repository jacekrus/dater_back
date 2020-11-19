package com.dater.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "conversations")
public class ConversationEntity extends BaseEntity {
	
	@ManyToMany
	@JoinTable(name = "conversation_users", 
			   joinColumns = {@JoinColumn(name = "conversation_id")},
			   inverseJoinColumns = {@JoinColumn(name = "user_id")})
	@BatchSize(size = 10)
	private Set<UserEntity> users;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime createTime;

	private LocalDateTime latestMessageTime;
	
	@Transient
	@JsonSerialize
	private boolean hasUnreadMessages;
	
	public ConversationEntity() {}

	public ConversationEntity(Set<UserEntity> users, LocalDateTime createTime) {
		this.users = users;
		this.createTime = createTime;
	}

	public Set<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(Set<UserEntity> users) {
		this.users = users;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public void addUser(UserEntity user) {
		users.add(user);
	}
	
	public void removeUser(UserEntity user) {
		users.remove(user);
	}

	public LocalDateTime getLatestMessageTime() {
		return latestMessageTime;
	}

	public void setLatestMessageTime(LocalDateTime latestMessageTime) {
		this.latestMessageTime = latestMessageTime;
	}

	public boolean isHasUnreadMessages() {
		return hasUnreadMessages;
	}

	public void setHasUnreadMessages(boolean hasUnreadMessages) {
		this.hasUnreadMessages = hasUnreadMessages;
	}
	
}
