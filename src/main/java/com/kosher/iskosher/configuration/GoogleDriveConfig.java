package com.kosher.iskosher.configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class GoogleDriveConfig {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${spring.google.private-key-id}")
    private String googlePrivateKeyId;

    @Value("${spring.google.private-key}")
    private String googlePrivateKey;

    @Bean
    public Drive googleDriveClient() throws GeneralSecurityException, IOException {

        Resource resource = new ClassPathResource("google-config.json");
        String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String processedContent = content
                .replace("${GOOGLE_PRIVATE_KEY_ID}", googlePrivateKeyId)
                .replace("${GOOGLE_PRIVATE_KEY}", googlePrivateKey);

        GoogleCredential credential = GoogleCredential
                .fromStream(new ByteArrayInputStream(processedContent.getBytes()))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential
        ).setApplicationName(applicationName).build();
    }
}