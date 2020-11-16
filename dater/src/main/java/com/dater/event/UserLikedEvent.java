package com.dater.event;

import com.dater.model.UserEntity;

public class UserLikedEvent extends AbstractUserEvent {

	private static final long serialVersionUID = 912958449822620681L;
	
	private final String likedById;

	public UserLikedEvent(Object source, UserEntity user, String likedById) {
		super(source, EventType.LIKED, user);
		this.likedById = likedById;
	}

	public String getLikedById() {
		return likedById;
	}

	@Override
	public <V> V apply(ApplicationEventVisitor<V> visitor) {
		return visitor.visit(this);
	}

}
