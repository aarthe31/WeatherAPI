package com.challenge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.challenge.model.Weather;

/**
 * Repository class to save and retrieve Weather data
 */
@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {
	Optional<Weather> findByCityNameAndCountryCode(String cityName, String countryCode);
}
