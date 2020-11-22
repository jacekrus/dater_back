package com.dater.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
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
	
	@ElementCollection
	@JoinTable(name = "usr_conv_acc_time", joinColumns = @JoinColumn(name = "conversation_id"))
	@MapKeyColumn(name = "user_id")
	@Column(name = "last_access_time")
	@BatchSize(size = 10)
	private Map<String, LocalDateTime> userLastAccessedTime;
	
	@Transient
	@JsonSerialize
	private boolean hasUnreadMessages;
	
	public ConversationEntity() {}

	public ConversationEntity(Set<UserEntity> users, LocalDateTime createTime, Map<String, LocalDateTime> userLastAccessedTime) {
		this.users = users;
		this.createTime = createTime;
		this.userLastAccessedTime = userLastAccessedTime;
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

	public void addUser(UserEntity user, LocalDateTime accessTime) {
		users.add(user);
		userLastAccessedTime.put(user.getId(), accessTime);
	}
	
	public void removeUser(UserEntity user) {
		users.remove(user);
		userLastAccessedTime.remove(user.getId());
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

	public Map<String, LocalDateTime> getUserLastAccessedTime() {
		return userLastAccessedTime;
	}

	public void setUserLastAccessedTime(Map<String, LocalDateTime> userLastAccessedTime) {
		this.userLastAccessedTime = userLastAccessedTime;
	}
	
}
