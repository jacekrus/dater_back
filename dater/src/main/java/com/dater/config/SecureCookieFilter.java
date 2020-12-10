package com.dater.config;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

public class SecureCookieFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		filterChain.doFilter(request, response);
		addSameSiteCookieAttribute(response);
	}
	
	private void addSameSiteCookieAttribute(HttpServletResponse response) {
		Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
		boolean firstHeader = true;
		for(String header : headers) {
			if(firstHeader) {
				response.setHeader(HttpHeaders.SET_COOKIE, header + "; SameSite=None; Secure");
				firstHeader = false;
				continue;
			}
			response.addHeader(HttpHeaders.SET_COOKIE, header + "; SameSite=None; Secure");
		}
	}


}
