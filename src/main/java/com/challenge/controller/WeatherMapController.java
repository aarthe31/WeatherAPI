package com.challenge.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.exception.SystemException;
import com.challenge.service.WeatherService;

@RestController
@RequestMapping("/data")
public class WeatherMapController {

	final Logger logger = LoggerFactory.getLogger(WeatherMapController.class);

	private final WeatherService weatherService;

	public WeatherMapController(WeatherService weatherService) {
		this.weatherService = weatherService;
	}

	@Value("${api.key1}")
	private String apiKey1;
	@Value("${api.key2}")
	private String apiKey2;
	@Value("${api.key3}")
	private String apiKey3;
	@Value("${api.key4}")
	private String apiKey4;
	@Value("${api.key5}")
	private String apiKey5;

	/**
	 * Retrieves the weather data based on city and country code
	 * 
	 * @param city
	 * @param countryCode
	 * @param apiKey
	 * @return the weather
	 * @throws Exception
	 */
	@GetMapping(path = "/weather")
	public ResponseEntity<String> getWeather(@RequestParam(required = true) String city,
			@RequestParam(required = true) String countryCode, @RequestParam(required = true) String apiKey)
			throws Exception {
		//logger.info("city:" + city);
		//logger.info("countryCode:" + countryCode);
		//logger.info("apiKey:" + apiKey);
		if (validateAPIKey(apiKey)) {
			String description = weatherService.retrieveWeather(city, countryCode, apiKey);
			logger.info("description:" + description);
			return new ResponseEntity<>(description, HttpStatus.OK);
		} else {
			throw new SystemException("Invalid API Key. Enter valid API Key to check the weather.",
					HttpStatus.BAD_REQUEST.value());
		}

	}

	/**
	 * Validates the API key
	 * 
	 * @param apiKey
	 * @return
	 */
	private boolean validateAPIKey(String apiKey) {
		boolean isValid = true;
		if (apiKey.contentEquals(apiKey1) || apiKey.contentEquals(apiKey2) || apiKey.contentEquals(apiKey3)
				|| apiKey.contentEquals(apiKey4) || apiKey.contentEquals(apiKey5)) {
			return isValid;
		} else {
			return !isValid;
		}
	}

}
