package com.kosher.iskosher.service.implementation;

import com.kosher.iskosher.types.TravelInfo;
import com.kosher.iskosher.service.TravelTimeService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Service
public class TravelTimeServiceImpl implements TravelTimeService {

    private final RestTemplate restTemplate;

    @Value("${osrm.api.url}")
    private String osrmApiUrl;

    public TravelTimeServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "travelInfoCache", key = "#fromLat + ',' + #fromLon + ',' + #toLat + ',' + #toLon")
    public TravelInfo getTravelInfo(double fromLat, double fromLon, double toLat, double toLon) {
        try {

            String drivingUrl = UriComponentsBuilder.fromHttpUrl(osrmApiUrl + "/route/v1/driving")
                    .path("/{fromLon},{fromLat};{toLon},{toLat}")
                    .queryParam("steps", "false")
                    .queryParam("overview", "false")
                    .buildAndExpand(fromLon, fromLat, toLon, toLat)
                    .toUriString();

            log.info("Driving URL: {}", drivingUrl);


            String walkingUrl = UriComponentsBuilder.fromHttpUrl(osrmApiUrl + "/route/v1/walking")
                    .path("/{fromLon},{fromLat};{toLon},{toLat}")
                    .queryParam("steps", "false")
                    .queryParam("overview", "false")
                    .buildAndExpand(fromLon, fromLat, toLon, toLat)
                    .toUriString();

            log.info("Walking URL: {}", walkingUrl);

            OSRMResponseWrapper drivingResponse = restTemplate.getForObject(drivingUrl, OSRMResponseWrapper.class);

            OSRMResponseWrapper walkingResponse = restTemplate.getForObject(walkingUrl, OSRMResponseWrapper.class);

            if (drivingResponse != null && !drivingResponse.getRoutes().isEmpty() &&
                    walkingResponse != null && !walkingResponse.getRoutes().isEmpty()) {

                OSRMRoute drivingRoute = drivingResponse.getRoutes().get(0);
                OSRMRoute walkingRoute = walkingResponse.getRoutes().get(0);

                double walkingSpeedAdjustment = 5.0;
                double distance = walkingRoute.getDistance();
                double adjustedDuration = (distance / 1000.0) / walkingSpeedAdjustment * 3600;
                walkingRoute.setDuration(adjustedDuration);

                return TravelInfo.builder()
                        .drivingDuration(formatDuration(drivingRoute.getDuration()))
                        .drivingDistance(formatDistance(drivingRoute.getDistance()))
                        .walkingDuration(formatDuration(walkingRoute.getDuration()))
                        .walkingDistance(formatDistance(walkingRoute.getDistance()))
                        .build();
            } else {
                log.error("No routes found for the given coordinates.");
            }
        } catch (Exception e) {
            log.error("Error getting route for coordinates: {},{} to {},{}",
                    fromLat, fromLon, toLat, toLon, e);
        }

        return null;
    }

    private String formatDuration(double seconds) {
        long totalMinutes = Math.round(seconds / 60);
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        if (hours == 0) {
            return minutes + " דקות";
        }
        if (minutes == 0) {
            return hours + " שעות";
        }
        return String.format("%d שעות ו-%d דקות", hours, minutes);
    }

    private String formatDistance(double meters) {
        return String.format("%.1f ק\"מ", meters / 1000.0);
    }
}

@Data
class OSRMResponseWrapper {
    private List<OSRMRoute> routes;
}

@Data
class OSRMRoute {
    private double duration;
    private double distance;
}
