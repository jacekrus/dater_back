package com.dater.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {

	private static char[] DICT = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', '+', '/' };

	private static long MASK = 63L;

	@Id
	@Column(nullable = false, unique = true)
	protected String id;

	public BaseEntity(String id) {
		if (id == null || id.isBlank()) {
			this.id = generateId();
		} else {
			this.id = id;
		}
	}

	protected BaseEntity() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String generateId() {
		return getIdString(UUID.randomUUID());
	}

	private static String getIdString(UUID uuid) {
		long hi = uuid.getMostSignificantBits();
		long lo = uuid.getLeastSignificantBits();
		StringBuilder sb = new StringBuilder();
		for (int i = 58; i >= 4; i -= 6) {
			sb.append(DICT[(int) ((hi >>> i) & MASK)]);
		}
		sb.append(DICT[(int) (((hi << 2) + (lo >>> 62)) & MASK)]);
		for (int i = 56; i >= 2; i -= 6) {
			sb.append(DICT[(int) ((lo >>> i) & MASK)]);
		}
		sb.append(DICT[(int) (lo & 3L)]);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity other = (BaseEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
