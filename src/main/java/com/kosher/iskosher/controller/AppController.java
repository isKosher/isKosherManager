package com.kosher.iskosher.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AppController {
    @GetMapping("/")
    public String home() {
        return "Service is up and running";
    }

    @GetMapping({"favicon.ico", "favicon.png"}) // Handle both .ico and .png requests
    public ResponseEntity<Void> handleFaviconRequest() {
        try {
            // When browser requests favicon.ico, it will get an empty response with status 204
            // Status 204 tells the browser "request is fine, but there's no content to return"
            // This will cause the browser to stop requesting favicon in subsequent requests
            log.debug("Favicon request handled successfully");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error handling favicon request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}