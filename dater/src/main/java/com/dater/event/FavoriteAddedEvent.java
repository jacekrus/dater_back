package com.dater.event;

public class FavoriteAddedEvent extends AbstractUserEvent {

	private static final long serialVersionUID = -6384590869038300098L;
	
	private final String favoriteUsername;

	public FavoriteAddedEvent(Object source, String username, String favoriteUsername) {
		super(source, EventType.FAVORITE, username);
		this.favoriteUsername = favoriteUsername;
	}

	public String getFavoriteUsername() {
		return favoriteUsername;
	}

	@Override
	public <V> V apply(ApplicationEventVisitor<V> visitor) {
		return visitor.visit(this);
	}

}
