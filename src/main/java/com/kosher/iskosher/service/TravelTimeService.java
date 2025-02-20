package com.kosher.iskosher.service;

import com.kosher.iskosher.types.TravelInfo;

public interface TravelTimeService {
    TravelInfo getTravelInfo(double fromLat, double fromLon, double toLat, double toLon);
}
