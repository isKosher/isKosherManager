package com.kosher.iskosher.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;

@Configuration
public class BeansConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Creates and returns a customized ObjectMapper bean.
     * The ObjectMapper is configured to handle Java 8 time API types and to use a custom date format
     * and naming strategy (snake_case).
     *
     * @return a new instance of ObjectMapper configured with specific settings.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Registering JavaTimeModule to handle Java 8 Date/Time API (e.g., Instant)
        objectMapper.registerModule(new JavaTimeModule());

        // Setting a custom date format (ISO 8601 with milliseconds and time zone)
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));

        // Setting the property naming strategy to Snake Case (e.g., camelCase to snake_case)
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        return objectMapper;
    }
}
