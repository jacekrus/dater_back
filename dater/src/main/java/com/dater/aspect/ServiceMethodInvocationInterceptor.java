package com.dater.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dater.exception.BaseDaterException;

@Aspect
public class ServiceMethodInvocationInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceMethodInvocationInterceptor.class);

	@Pointcut("execution(* com.dater.service.impl..*.*(..))")
	public void intercept() { /* Pointcut declaration */ }

	@Around("intercept()")
	public Object executeServiceMethod(ProceedingJoinPoint joinPoint) {
		try {
			return joinPoint.proceed();
		} catch (BaseDaterException e) {
			throw e;
		} catch (Throwable t) {
			LOGGER.error("Unexpected error occured while executing service method", t);
			throw new BaseDaterException("Internal server error occured. Please try again later or contact site's administrator.");
		}
	}

}
