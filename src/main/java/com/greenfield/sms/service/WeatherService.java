package com.greenfield.sms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public WeatherService() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5s timeout
        factory.setReadTimeout(5000);

        this.restTemplate = new RestTemplate(factory);
    }

    public Map<String, Object> getWeather(String city) {

        try {
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);

            String url = "https://api.openweathermap.org/data/2.5/weather?q="
                    + encodedCity
                    + "&appid=" + apiKey
                    + "&units=metric";

            return restTemplate.getForObject(url, Map.class);

        } catch (RestClientException ex) {
            System.err.println("Weather API error: " + ex.getMessage());
            return getFallbackWeather();
        } catch (Exception ex) {
            System.err.println("Unexpected error: " + ex.getMessage());
            return getFallbackWeather();
        }
    }

    private Map<String, Object> getFallbackWeather() {
        return Map.of(
                "main", Map.of(
                        "temp", "N/A",
                        "humidity", "N/A"
                ),
                "weather", List.of(
                        Map.of("description", "Weather unavailable")
                )
        );
    }
}
