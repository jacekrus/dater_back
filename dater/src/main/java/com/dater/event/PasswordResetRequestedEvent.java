package com.dater.event;

import com.dater.model.PasswordResetEntity;

public class PasswordResetRequestedEvent extends AbstractUserEvent {
	
	private static final long serialVersionUID = -8768581222048626487L;
	private final PasswordResetEntity passReset;
	
	public PasswordResetRequestedEvent(Object source, String email, PasswordResetEntity passReset) {
		super(source, EventType.PASS_RESET, email);
		this.passReset = passReset;
	}

	public PasswordResetEntity getPassReset() {
		return passReset;
	}

	@Override
	public <V> V apply(ApplicationEventVisitor<V> visitor) {
		return visitor.visit(this);
	}

}
