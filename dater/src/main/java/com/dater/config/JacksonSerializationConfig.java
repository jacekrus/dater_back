package com.dater.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@Configuration
public class JacksonSerializationConfig implements WebMvcConfigurer {

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.stream()
				  .filter(conv -> conv instanceof MappingJackson2HttpMessageConverter)
				  .map(conv -> ((MappingJackson2HttpMessageConverter) conv).getObjectMapper())
				  .forEach(mapper -> mapper.registerModule(new Hibernate5Module()));
	}
	
}
