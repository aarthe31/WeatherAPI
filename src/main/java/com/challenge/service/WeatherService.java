package com.challenge.service;

import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.challenge.exception.SystemException;
import com.challenge.model.Weather;
import com.challenge.repository.WeatherRepository;
/**
 * Retrieves the weather description data from the endpoint and updates them in DB
 */
@Service
public class WeatherService {
	@Value("${openweathermap.url}")
	private String openWeatherMapUrl;

	private final APIKeyService limitService;

	private final WeatherRepository weatherRepository;

	public WeatherService(WeatherRepository weatherRepository, APIKeyService limitService) {
		this.weatherRepository = weatherRepository;
		this.limitService = limitService;
	}

	final Logger logger = LoggerFactory.getLogger(APIKeyService.class);

	public String retrieveWeather(String city, String country, String apiKey) {
		String description = "";
		if (limitService.hasRateLimitExceeded(apiKey)) {
			throw new SystemException("Hourly access limit has been exceeded! Please try again after sometime.",
					HttpStatus.BAD_REQUEST.value());
		} else {
			limitService.updateTimeAndCount(apiKey);

			Optional<Weather> weatherinDb = weatherRepository.findByCityNameAndCountryCode(city, country);
			if (weatherinDb.isPresent()) {
				description = weatherinDb.get().getDescription();
			} else {

				String responseString = "";
				String url = openWeatherMapUrl + "?q=" + city + "," + country + "&appid=" + apiKey;
				//logger.info("url:" + url);

				RestTemplate restTemplate = new RestTemplate();
				responseString = restTemplate.getForObject(url, String.class);
				//logger.info("responseString:" + responseString);
				if (responseString != null && !responseString.isEmpty()) {

					JSONObject jsonObject = new JSONObject(responseString);
					JSONArray weather = jsonObject.getJSONArray("weather");
					JSONObject weatherJson = (JSONObject) (weather.get(0));
					description = weatherJson.getString("description");
					//logger.info("description:" + description);

					Weather weatherData = new Weather();
					weatherData.setCityName(city);
					weatherData.setCountryCode(country);
					weatherData.setDescription(description);
					weatherRepository.save(weatherData);
					logger.info("weatherData:" + weatherData.toString());
				}
			}
			return description;
		}
	}
}
