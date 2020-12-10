package com.dater.event;

public class PasswordResetRequestedEvent extends AbstractUserEvent {
	
	private static final long serialVersionUID = -8768581222048626487L;
	private final String passResetId;
	
	public PasswordResetRequestedEvent(Object source, String email, String passResetId) {
		super(source, EventType.PASS_RESET, email);
		this.passResetId = passResetId;
	}

	public String getPassResetId() {
		return passResetId;
	}

	@Override
	public <V> V apply(ApplicationEventVisitor<V> visitor) {
		return visitor.visit(this);
	}

}
