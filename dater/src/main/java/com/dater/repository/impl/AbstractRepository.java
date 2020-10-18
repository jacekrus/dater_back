package com.dater.repository.impl;

import javax.persistence.Query;

import org.springframework.data.domain.Pageable;

public abstract class AbstractRepository {
	
	protected void applyPagination(Query query, Pageable pageable) {
		int pageNumber = pageable.getPageNumber();
		int pageSize = pageable.getPageSize();
		query.setFirstResult(pageSize * pageNumber).setMaxResults(pageSize);
	}

}
