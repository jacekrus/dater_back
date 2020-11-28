package com.dater.event;

public abstract class AbstractUserEvent extends AbstractApplicationEvent {
	
	private static final long serialVersionUID = 4159292051314549225L;
	private final String usernameOrEmail;

	public AbstractUserEvent(Object source, EventType type, String usernameOrEmail) {
		super(source, type);
		this.usernameOrEmail = usernameOrEmail;
	}

	public String getUsernameOrEmail() {
		return usernameOrEmail;
	}
	
}
