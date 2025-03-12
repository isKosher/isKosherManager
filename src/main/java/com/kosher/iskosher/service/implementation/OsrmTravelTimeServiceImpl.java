package com.kosher.iskosher.service.implementation;

import com.kosher.iskosher.common.enums.TravelMode;
import com.kosher.iskosher.service.TravelTimeService;
import com.kosher.iskosher.types.OsrmResponseWrapper;
import com.kosher.iskosher.types.OsrmRoute;
import com.kosher.iskosher.types.TravelInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class OsrmTravelTimeServiceImpl implements TravelTimeService {

    private final RestTemplate restTemplate;

    @Value("${osrm.api.url}")
    private String osrmApiUrl;

    @Cacheable(value = "travelInfoCache", key = "#fromLat + ',' + #fromLon + ',' + #toLat + ',' + #toLon")
    public Optional<TravelInfo> getTravelInfo(double fromLat, double fromLon, double toLat, double toLon) {
        try {
            // Execute both API calls concurrently
            CompletableFuture<Optional<OsrmRoute>> drivingRouteFuture = fetchRouteAsync(TravelMode.DRIVING, fromLat, fromLon, toLat, toLon);
            CompletableFuture<Optional<OsrmRoute>> walkingRouteFuture = fetchRouteAsync(TravelMode.WALKING, fromLat, fromLon, toLat, toLon);

            // Wait for both to complete
            CompletableFuture.allOf(drivingRouteFuture, walkingRouteFuture).join();

            // Get results
            Optional<OsrmRoute> drivingRoute = drivingRouteFuture.get();
            Optional<OsrmRoute> walkingRoute = walkingRouteFuture.get();

            if (drivingRoute.isPresent() && walkingRoute.isPresent()) {
                adjustWalkingDuration(walkingRoute.get());

                return Optional.of(TravelInfo.builder()
                        .drivingDuration(formatDuration(drivingRoute.get().getDuration()))
                        .drivingDistance(formatDistance(drivingRoute.get().getDistance()))
                        .walkingDuration(formatDuration(walkingRoute.get().getDuration()))
                        .walkingDistance(formatDistance(walkingRoute.get().getDistance()))
                        .build());
            } else {
                log.warn("No routes found for coordinates: {},{} to {},{}", fromLat, fromLon, toLat, toLon);
            }
        } catch (Exception e) {
            log.error("Error getting route for coordinates: {},{} to {},{}", fromLat, fromLon, toLat, toLon, e);
        }

        return Optional.empty();
    }

    @Async
    public CompletableFuture<Optional<OsrmRoute>> fetchRouteAsync(TravelMode travelMode, double fromLat, double fromLon, double toLat, double toLon) {
        return CompletableFuture.completedFuture(fetchRoute(travelMode, fromLat, fromLon, toLat, toLon));
    }

    private Optional<OsrmRoute> fetchRoute(TravelMode travelMode, double fromLat, double fromLon, double toLat, double toLon) {
        String url = UriComponentsBuilder.fromHttpUrl(osrmApiUrl + "/route/v1/" + travelMode.getMode())
                .path("/{fromLon},{fromLat};{toLon},{toLat}")
                .queryParam("steps", "false")
                .queryParam("overview", "false")
                .buildAndExpand(fromLon, fromLat, toLon, toLat)
                .toUriString();

        log.info("{} route URL: {}", travelMode.name(), url);

        try {
            OsrmResponseWrapper response = restTemplate.getForObject(url, OsrmResponseWrapper.class);

            if (response != null && response.getRoutes() != null && !response.getRoutes().isEmpty()) {
                return response.getRoutes().stream().findFirst();
            }
        } catch (Exception e) {
            log.error("Failed to fetch {} route: {}", travelMode.name(), e.getMessage());
        }

        return Optional.empty();
    }

    private void adjustWalkingDuration(OsrmRoute walkingRoute) {
        double walkingSpeedAdjustment = 5.0;
        double distance = walkingRoute.getDistance();
        double adjustedDuration = (distance / 1000.0) / walkingSpeedAdjustment * 3600;
        walkingRoute.setDuration(adjustedDuration);
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