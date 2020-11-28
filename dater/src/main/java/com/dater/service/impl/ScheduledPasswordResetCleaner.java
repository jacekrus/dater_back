package com.dater.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dater.model.PasswordResetEntity;
import com.dater.repository.PasswordResetRepository;

@Component
public class ScheduledPasswordResetCleaner {
	
	@Autowired
	private PasswordResetRepository repo;
	
	@Scheduled(fixedRate = 10 * 60 * 1000)
	public void clean() {
		List<PasswordResetEntity> requests = repo.findRequestedBefore(LocalDateTime.now().minusMinutes(30));
		repo.deleteInBatch(requests);
	}

}
