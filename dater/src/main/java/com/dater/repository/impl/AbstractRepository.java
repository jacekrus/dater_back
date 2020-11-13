package com.dater.repository.impl;

import javax.persistence.Query;

import com.dater.repository.SkippingPageable;

public abstract class AbstractRepository {
	
	protected void applyPagination(Query query, SkippingPageable pageable) {
		int pageNumber = pageable.getPageNumber();
		int pageSize = pageable.getPageSize();
		query.setFirstResult((pageSize * pageNumber) + pageable.getSkip()).setMaxResults(pageSize);
	}

}
