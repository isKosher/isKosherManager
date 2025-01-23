package com.kosher.iskosher.service.implementation;

import com.kosher.iskosher.service.OAuth2Service;
import com.kosher.iskosher.types.GoogleUser;
import org.springframework.web.client.RestTemplate;

public class GoogleAuthService implements OAuth2Service<GoogleUser> {
    private static final String GOOGLE_TOKEN_INFO_URL = "https://oauth2.googleapis.com/tokeninfo?id_token=";

    @Override
    public GoogleUser verifyToken(String token) {
        RestTemplate restTemplate = new RestTemplate();
        String url = GOOGLE_TOKEN_INFO_URL + token;

        try {
            // Fetch user details from Google
            return restTemplate.getForObject(url, GoogleUser.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Google token", e);
        }
    }
    @Override
    public <R> R mapToApplicationUser(GoogleUser userDetails) {
        return null;
    }

    @Override
    public String getProviderName() {
        return "Google";
    }
}
