package com.dater.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.dater.aspect.ServiceMethodInvocationInterceptor;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan
public class AspectsConfig {
	
	@Bean
	public ServiceMethodInvocationInterceptor serviceAspect() {
		return new ServiceMethodInvocationInterceptor();
	}

}
