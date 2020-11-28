package com.dater.event;

public class UserLikedEvent extends AbstractUserEvent {

	private static final long serialVersionUID = 912958449822620681L;
	
	private final String likedByUsername;

	public UserLikedEvent(Object source, String userId, String likedByUsername) {
		super(source, EventType.LIKED, userId);
		this.likedByUsername = likedByUsername;
	}

	public String getLikedByUsername() {
		return likedByUsername;
	}

	@Override
	public <V> V apply(ApplicationEventVisitor<V> visitor) {
		return visitor.visit(this);
	}

}
