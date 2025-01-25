package com.kosher.iskosher.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @Value("${spring.firebase.private-key-id}")
    private String privateKeyId;

    @Value("${spring.firebase.private-key}")
    private String privateKey;

    /**
     * Initializes and configures the Firebase application.
     *
     * @return Initialized FirebaseApp instance
     * @throws IOException if there's an error reading the configuration file
     */
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            // Load and process the Firebase configuration file
            Resource resource = new ClassPathResource("firebase-config.json");
            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String processedContent = content
                    .replace("${PRIVATE_KEY_ID}", privateKeyId)
                    .replace("${PRIVATE_KEY}", privateKey);

            // Create GoogleCredentials from the processed configuration
            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    new ByteArrayInputStream(processedContent.getBytes())
            );

            // Build FirebaseOptions with the credentials
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            // Initialize and return the FirebaseApp
            return FirebaseApp.initializeApp(options);
        }
        return FirebaseApp.getInstance();
    }

    /**
     * Creates a FirebaseAuth instance using the initialized FirebaseApp.
     *
     * @param firebaseApp The initialized FirebaseApp
     * @return FirebaseAuth instance
     */
    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }
}
