package com.kosher.iskosher.common.enums;

public enum TravelMode {
    DRIVING("driving"),
    WALKING("walking");

    private final String mode;

    TravelMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }
}
