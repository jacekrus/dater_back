package com.dater.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.dater.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final String LOGIN_URL = "/datrLogin";
	private static final String LOGOUT_URL = "/datrLogout";

	private UserService userDetailsService;

	@Autowired
	public WebSecurityConfig(UserService userService) {
		this.userDetailsService = userService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers(HttpMethod.DELETE, "/users").hasAuthority("ADMIN")
			.antMatchers(HttpMethod.POST, "/users/add").permitAll()
			.antMatchers(HttpMethod.POST, "/users/validate").permitAll()
			.antMatchers(HttpMethod.POST, "/passwords", "/passwords/reset").permitAll()
			.antMatchers(HttpMethod.GET, "/passwords").permitAll()
			.anyRequest().authenticated()
			.and()
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
			.logoutUrl(LOGOUT_URL)
			.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
			.and().cors()
			.and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Bean
	public UsernamePasswordAuthenticationFilter authFilter() throws Exception {
		UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
		filter.setAuthenticationSuccessHandler(new AuthSuccessHandler());
		filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler());
		filter.setAuthenticationManager(super.authenticationManager());
		filter.setFilterProcessesUrl(LOGIN_URL);
		return filter;
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "PATCH", "DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Content-Length", "Accept", "X-Requested-With", "X-XSRF-TOKEN"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
}
