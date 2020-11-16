package com.dater.event;

import com.dater.model.UserEntity;

public class FavoriteAddedEvent extends AbstractUserEvent {

	private static final long serialVersionUID = -6384590869038300098L;
	
	private final String favoriteId;

	public FavoriteAddedEvent(Object source, UserEntity user, String favoriteId) {
		super(source, EventType.FAVORITE, user);
		this.favoriteId = favoriteId;
	}

	public String getFavoriteId() {
		return favoriteId;
	}

	@Override
	public <V> V apply(ApplicationEventVisitor<V> visitor) {
		return visitor.visit(this);
	}

}
