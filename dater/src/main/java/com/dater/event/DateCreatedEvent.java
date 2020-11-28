package com.dater.event;

public class DateCreatedEvent extends AbstractUserEvent {
	
	private static final long serialVersionUID = -8674223266097756404L;
	private final String dateUsername;

	public DateCreatedEvent(Object source, String userId, String dateUsername) {
		super(source, EventType.DATE, userId);
		this.dateUsername = dateUsername;
	}

	public String getDateUsername() {
		return dateUsername;
	}

	@Override
	public <V> V apply(ApplicationEventVisitor<V> visitor) {
		return visitor.visit(this);
	}

}
