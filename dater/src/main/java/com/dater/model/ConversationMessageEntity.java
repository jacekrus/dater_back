package com.dater.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "messages")
public class ConversationMessageEntity extends BaseEntity {
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private UserEntity sender;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private ConversationEntity conversation;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime sendTime;
	
	@Column(nullable = false, length = 500)
	private String text;

	public ConversationMessageEntity() {}
	
	public ConversationMessageEntity(UserEntity sender, ConversationEntity conversation, LocalDateTime sendTime, String text) {
		this.sender = sender;
		this.conversation = conversation;
		this.sendTime = sendTime;
		this.text = text;
	}

	public UserEntity getSender() {
		return sender;
	}

	public void setSender(UserEntity sender) {
		this.sender = sender;
	}

	public ConversationEntity getConversation() {
		return conversation;
	}

	public void setConversation(ConversationEntity conversation) {
		this.conversation = conversation;
	}

	public LocalDateTime getSendTime() {
		return sendTime;
	}

	public void setSendTime(LocalDateTime sendTime) {
		this.sendTime = sendTime;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((conversation == null) ? 0 : conversation.hashCode());
		result = prime * result + ((sendTime == null) ? 0 : sendTime.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		ConversationMessageEntity other = (ConversationMessageEntity) obj;
		if (conversation == null) {
			if (other.conversation != null)
				return false;
		} else if (!conversation.equals(other.conversation))
			return false;
		if (sendTime == null) {
			if (other.sendTime != null)
				return false;
		} else if (!sendTime.equals(other.sendTime))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
	
}
