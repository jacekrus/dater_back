package com.dater.event;

import org.springframework.context.ApplicationEvent;

public abstract class AbstractApplicationEvent extends ApplicationEvent {

	private static final long serialVersionUID = -6027887853029986920L;
	
	protected final EventType type;

	public AbstractApplicationEvent(Object source, EventType type) {
		super(source);
		this.type = type;
	}
	
	public EventType getType() {
		return type;
	}
	
	public abstract <V> V apply(ApplicationEventVisitor<V> visitor);
	
	public interface ApplicationEventVisitor<V> {
		
		V visit(MessageSendEvent e);
		
		V visit(DateCreatedEvent e);
		
		V visit(FavoriteAddedEvent e);
		
		V visit(UserLikedEvent e);
		
	}

}
