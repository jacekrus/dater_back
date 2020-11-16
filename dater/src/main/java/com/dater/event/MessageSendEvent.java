package com.dater.event;

import com.dater.model.ConversationMessageEntity;

public class MessageSendEvent extends AbstractApplicationEvent {
	
	private static final long serialVersionUID = -4484258678520751959L;
	private final ConversationMessageEntity message;

	public MessageSendEvent(Object source, ConversationMessageEntity message) {
		super(source, EventType.MESSAGE);
		this.message = message;
	}

	public ConversationMessageEntity getMessage() {
		return message;
	}

	@Override
	public <V> V apply(ApplicationEventVisitor<V> visitor) {
		return visitor.visit(this);
	}

}
