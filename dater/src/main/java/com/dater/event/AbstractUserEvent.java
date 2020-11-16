package com.dater.event;

import com.dater.model.UserEntity;

public abstract class AbstractUserEvent extends AbstractApplicationEvent {
	
	private static final long serialVersionUID = 4159292051314549225L;
	private final UserEntity user;

	public AbstractUserEvent(Object source, EventType type, UserEntity user) {
		super(source, type);
		this.user = user;
	}

	public UserEntity getUser() {
		return user;
	}
	
}
