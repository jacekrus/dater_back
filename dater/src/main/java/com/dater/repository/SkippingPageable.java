package com.dater.repository;

import org.springframework.data.domain.Pageable;

/*
 * skips n first results 
 */
public interface SkippingPageable extends Pageable {

	int getSkip();
	
}
