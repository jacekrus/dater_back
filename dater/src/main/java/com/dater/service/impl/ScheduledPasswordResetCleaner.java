package com.dater.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dater.model.PasswordResetEntity;
import com.dater.repository.PasswordResetRepository;

@Component
public class ScheduledPasswordResetCleaner {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledPasswordResetCleaner.class);
	private static final String SUCCESS_MSG = "[%d] password reset request were found and deleted.";
	
	@Autowired
	private PasswordResetRepository repo;
	
	@Scheduled(fixedRate = 10 * 60 * 1000)
	public void clean() {
		List<PasswordResetEntity> requests = repo.findRequestedBefore(LocalDateTime.now().minusMinutes(30));
		repo.deleteInBatch(requests);
		if(!requests.isEmpty()) {
			LOGGER.info(SUCCESS_MSG, requests.size());
		}
	}

}
