package com.kosher.iskosher.common.constant;

import java.util.concurrent.TimeUnit;

public class AppConstants {
    public static final String ACCESS_TOKEN_COOKIE = "access_token";
    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";
    public static final long ACCESS_TOKEN_DURATION = TimeUnit.HOURS.toSeconds(1);
    public static final long REFRESH_TOKEN_DURATION = TimeUnit.DAYS.toSeconds(7);
    public static final double EARTH_RADIUS_KM = 6371.0;



}
