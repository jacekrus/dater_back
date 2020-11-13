package com.dater.repository.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.dater.repository.SkippingPageable;

public class PageWithSkipRequest extends PageRequest implements SkippingPageable {
	
	private static final long serialVersionUID = -7090750232974977139L;
	private final int skip;
	
	public PageWithSkipRequest(Pageable pageable) {
		super(pageable.getPageNumber(), pageable.getPageSize(), Sort.unsorted());
		this.skip = 0;
	}
	
	public PageWithSkipRequest(Pageable pageable, int skip) {
		super(pageable.getPageNumber(), pageable.getPageSize(), Sort.unsorted());
		this.skip = skip;
	}

	@Override
	public int getSkip() {
		return skip;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + skip;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PageWithSkipRequest other = (PageWithSkipRequest) obj;
		return skip == other.skip;
	}

}
