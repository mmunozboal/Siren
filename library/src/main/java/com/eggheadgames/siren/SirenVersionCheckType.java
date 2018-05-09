package com.eggheadgames.siren;

/**
 * Determines the frequency in which the the version check is performed
 */
public enum SirenVersionCheckType {

    IMMEDIATELY(0),    // Version check performed every time the app is launched
    DAILY(1),          // Version check performed once a day
    WEEKLY(7);         // Version check performed once a week

    private final int value;

    SirenVersionCheckType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
