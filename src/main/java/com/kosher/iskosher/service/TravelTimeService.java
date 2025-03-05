package com.kosher.iskosher.service;

import com.kosher.iskosher.types.TravelInfo;

import java.util.Optional;

public interface TravelTimeService {
    Optional<TravelInfo> getTravelInfo(double fromLat, double fromLon, double toLat, double toLon);
}
