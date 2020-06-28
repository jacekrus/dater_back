package com.dater.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import com.dater.service.UserService;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {
	
	private static final String loginUrl = "/datrLogin";
	private static final String logoutUrl = "/datrLogout";

	private UserService userDetailsService;

	@Autowired
	public WebSecurityConfig(UserService userService) {
		this.userDetailsService = userService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers(HttpMethod.DELETE, "/users").hasAuthority("ADMIN")
			.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			.anyRequest().authenticated()
//			.anyRequest().permitAll()
			.and()
			.addFilterBefore(new CorsFilterConfig(), ChannelProcessingFilter.class)
			.addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling()
			.accessDeniedHandler((request, response, accessDeniedException) -> response.setStatus(HttpStatus.FORBIDDEN.value()))
			.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
			.and()
			.rememberMe()
			.and()
			.logout()
			.deleteCookies("JSESSIONID")
			.invalidateHttpSession(true)
			.logoutUrl(logoutUrl)
			.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
			.and().csrf().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
	
	@Bean
	public UsernamePasswordAuthenticationFilter authFilter() throws Exception {
		UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
		filter.setAuthenticationSuccessHandler(new AuthSuccessHandler());
		filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler());
		filter.setAuthenticationManager(super.authenticationManager());
		filter.setFilterProcessesUrl(loginUrl);
		return filter;
	}
	
}
