package com.challenge.service;

import java.sql.Timestamp;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.challenge.model.APIAccess;
import com.challenge.repository.APIAccessRepository;

/**
 * This class checks if the API access count is reached within the specified
 * time limit
 */
@Service
public class APIKeyService {
	private static final long ONE_HOUR_IN_MILLIS = 3600000;
	@Value("${weather.api.rate-limit}")
	private int rateLimit;
	Logger logger = LoggerFactory.getLogger(APIKeyService.class);

	private APIAccessRepository counterRepository;

	public APIKeyService(APIAccessRepository counterRepository) {
		this.counterRepository = counterRepository;
	}

	/**
	 * Checks if the user with the given API key has exceeded retrieval count of the
	 * weather data
	 * 
	 * @param apiKey
	 * @return
	 */
	public boolean hasRateLimitExceeded(String apiKey) {

		Optional<APIAccess> apiAccessCounter = counterRepository.findByApiKey(apiKey);

		if (apiAccessCounter.isPresent()) {
			Timestamp lastAccessTs = apiAccessCounter.get().getLastAccessTime();
			int accessCount = apiAccessCounter.get().getAccessCount();
			logger.info("accessCount:" + accessCount);
			logger.info("time diff:" + (System.currentTimeMillis() - lastAccessTs.getTime()));
			if (accessCount >= rateLimit
					&& (System.currentTimeMillis() - lastAccessTs.getTime() < ONE_HOUR_IN_MILLIS)) {
				logger.info("hasRateLimitExceeded:" + true);
				return true;
			}
		}
		logger.info("hasRateLimitExceeded:" + false);
		return false;
	}

	/**
	 * Updates the weather accessed time and increments the access count of the
	 * given API Key
	 * 
	 * @param apiKey
	 */
	public void updateTimeAndCount(String apiKey) {
		Optional<APIAccess> apiAccessCounter = counterRepository.findByApiKey(apiKey);
		APIAccess accessData = new APIAccess();
		if (apiAccessCounter.isPresent()) {
			accessData = apiAccessCounter.get();
			int accessCount = apiAccessCounter.get().getAccessCount();
			Timestamp lastAccessTs = apiAccessCounter.get().getLastAccessTime();
			if (accessCount == rateLimit
					&& (System.currentTimeMillis() - lastAccessTs.getTime() >= ONE_HOUR_IN_MILLIS)) {
				resetCount(accessData);
			} else {
				incrementCount(accessData, accessCount);
			}
			setLastAccessTime(accessData);
		} else {
			resetCount(accessData);
			accessData.setApiKey(apiKey);
			setLastAccessTime(accessData);
		}

		accessData = counterRepository.save(accessData);
		logger.info("accessData:" + accessData.toString());
	}

	private void setLastAccessTime(APIAccess accessData) {
		accessData.setLastAccessTime(new Timestamp(System.currentTimeMillis()));
	}

	private void incrementCount(APIAccess accessData, int accessCount) {
		accessData.setAccessCount(accessCount + 1);
	}

	private void resetCount(APIAccess accessData) {
		accessData.setAccessCount(1);
	}
}
