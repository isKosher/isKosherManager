package com.kosher.iskosher.service.implantation;

import com.kosher.iskosher.dto.response.UnsplashResponse;
import com.kosher.iskosher.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class UnsplashImageService implements ImageService {


    private final RestTemplate restTemplate;
    private final String unsplashApiUrl;
    private final String unsplashAccessKey;

    public UnsplashImageService(RestTemplate restTemplate,
                                @Value("${unsplash.api.url}") String unsplashApiUrl,
                                @Value("${unsplash.access.key}") String unsplashAccessKey) {
        this.restTemplate = restTemplate;
        this.unsplashApiUrl = unsplashApiUrl;
        this.unsplashAccessKey = unsplashAccessKey;
    }
    @Override
    public String fetchImage(String businessType) {
        String url = UriComponentsBuilder.fromHttpUrl(unsplashApiUrl + "/photos/random")
                .queryParam("query", businessType)
                .queryParam("client_id", unsplashAccessKey)
                .toUriString();

        try {
            UnsplashResponse response = restTemplate.getForObject(url, UnsplashResponse.class);
            if (response != null && response.getUrls() != null) {
                return response.getUrls().getRegular();
            }
        } catch (Exception e) {
            System.out.println("Error fetching image: " + e.getMessage());
        }
        return null;
    }
}
