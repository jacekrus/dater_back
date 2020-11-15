package com.dater.repository.impl;

import javax.persistence.Query;

import org.springframework.data.domain.Pageable;

import com.dater.repository.SkippingPageable;

public abstract class AbstractRepository {
	
	protected void applyPagination(Query query, Pageable pageable) {
		int pageNumber = pageable.getPageNumber();
		int pageSize = pageable.getPageSize();
		int skip = pageable instanceof SkippingPageable ? ((SkippingPageable) pageable).getSkip() : 0;
		query.setFirstResult((pageSize * pageNumber) + skip).setMaxResults(pageSize);
	}

}
