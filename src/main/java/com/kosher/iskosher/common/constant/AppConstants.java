package com.kosher.iskosher.common.constant;

import java.util.concurrent.TimeUnit;

public class AppConstants {
    public static final int DURATION_CACHE_EXPIRE = 2;
    public static final TimeUnit TIME_UNIT_CACHE_EXPIRE = TimeUnit.HOURS;
    public static final long MAXIMUM_SIZE_CACHE = 1000;
    public static final String ACCESS_TOKEN_COOKIE = "access_token";
    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";
    public static final long ACCESS_TOKEN_DURATION = TimeUnit.HOURS.toSeconds(1);
    public static final long REFRESH_TOKEN_DURATION = TimeUnit.DAYS.toSeconds(7);



}
