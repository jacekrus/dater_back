package com.dater.event;

import com.dater.model.UserEntity;

public class DateCreatedEvent extends AbstractUserEvent {
	
	private static final long serialVersionUID = -8674223266097756404L;
	private final UserEntity date;

	public DateCreatedEvent(Object source, UserEntity user, UserEntity date) {
		super(source, EventType.DATE, user);
		this.date = date;
	}

	public UserEntity getDate() {
		return date;
	}

	@Override
	public <V> V apply(ApplicationEventVisitor<V> visitor) {
		return visitor.visit(this);
	}

}
