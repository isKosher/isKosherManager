package com.kosher.iskosher.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonParserUtil {

    private final ObjectMapper objectMapper;
    private static ObjectMapper staticObjectMapper;

    @PostConstruct
    public void init() {
        staticObjectMapper = this.objectMapper;
    }

    public static <T> List<T> parseJson(String json, Class<T> clazz) {
        try {
            return staticObjectMapper.readValue(json,
                    staticObjectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            log.error("Failed to parse JSON to List of {}: {}", clazz.getSimpleName(), e.getMessage());
            log.debug("JSON content: {}", json);
            log.debug("Exception details:", e);
            return new ArrayList<>();
        }
    }
}
