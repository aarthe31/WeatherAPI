package com.challenge;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.challenge.exception.SystemException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherAPIApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void testWeather() throws Exception {
		try {
			RequestBuilder requestBuilder = MockMvcRequestBuilders
					.get("/data/weather?countryCode=AUS&apiKey=11100215fe5ae9873a3918f35fd129a3&city=Melbourne")
					.accept(MediaType.APPLICATION_JSON);

			ResultActions result = mockMvc.perform(requestBuilder);
			result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));

		} catch (Exception e) {
			throw new SystemException("Error executing testcase", HttpStatus.BAD_REQUEST.value());
		}
	}
	
	@Test
	void testIncorrectAPIKey() throws Exception {
		try {
			RequestBuilder requestBuilder = MockMvcRequestBuilders
					.get("/data/weather?countryCode=AUS&apiKey=errorKey&city=Melbourne")
					.accept(MediaType.APPLICATION_JSON);

			ResultActions result = mockMvc.perform(requestBuilder);
			result.andExpect(status().isBadRequest());

		} catch (Exception e) {
			throw new SystemException("Error executing testcase", HttpStatus.BAD_REQUEST.value());
		}
	}

}
