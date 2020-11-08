package com.dater.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

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

	@Column(nullable = false, updatable = false)
	private String name;
	
	private LocalDateTime latestMessageTime;
	
	public ConversationEntity() {}

	public ConversationEntity(Set<UserEntity> users, LocalDateTime createTime, String name) {
		this.users = users;
		this.createTime = createTime;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ConversationEntity other = (ConversationEntity) obj;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
