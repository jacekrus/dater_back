package com.dater.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import com.dater.exception.UserNotAuthenticatedException;

public class CustomTokenBasedRememberMeServices extends TokenBasedRememberMeServices {

	public CustomTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService) {
		super(key, userDetailsService);
	}
	
	@Override
	protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request,
			HttpServletResponse response) {
		String rememberMeHeader = request.getHeader("Dater-Remember-Me");
		if(rememberMeHeader != null && rememberMeHeader.equals("true")) {
			return super.processAutoLoginCookie(cookieTokens, request, response);
		}
		throw new UserNotAuthenticatedException("Your session has expired. Please log in again.");
	}

}
