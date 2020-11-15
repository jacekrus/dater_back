package com.dater.event;

import java.util.Objects;

import org.springframework.context.ApplicationEvent;

import com.dater.model.ConversationMessageEntity;

public class MessageSendEvent extends ApplicationEvent {
	
	private static final long serialVersionUID = -4484258678520751959L;
	private final ConversationMessageEntity message;

	public MessageSendEvent(Object source, ConversationMessageEntity message) {
		super(source);
		Objects.requireNonNull(message, "message cannot be null");
		this.message = message;
	}

	public ConversationMessageEntity getMessage() {
		return message;
	}

}
