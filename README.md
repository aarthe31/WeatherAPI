#Vanguard Java Challenge
The purpose of this application is to retrieve the weather information from openWeatherMap.org and return the description from the detailed weather information.

## Getting Started

### Tech stack used

- Springboot
- Java 17
- Maven
- Junit
- Postman (For testing)

#### Running the application
1. Import the project into eclipse
- Right click on the project -> Run As -> Maven clean and then select Maven Install
- Again Right click on the project -> Run As -> Springboot App
- Go to Postman, create new HHTP request and the enter the GET url as: 
	- http://localhost:8080/data/weather?city=<<city>>&countryCode=<<country_code>>&apiKey=<<api_key>>
		- Ex: http://localhost:8080/data/weather?city=Melbourne&countryCode=AUS&apiKey=11100215fe5ae9873a3918f35fd129a3
- Enter valid city for 'city' and country code for 'countryCode' and one of the api keys for 'apiKey' as mentioned below.
- The permitted API Keys are:

	1. a5a059298f457f64a6bf001e35bbe526
		2. 11100215fe5ae9873a3918f35fd129a3
		3. 0043f1d70cbc253d30568cec36d7c141
		4. d7e92b49ff950f5a35ce07a7bc96f998
		5. daf3f452f423d6956098f48ae8ab4fa4
- The API Keys can be changed in application.properties file"# WeatherAPI" 
