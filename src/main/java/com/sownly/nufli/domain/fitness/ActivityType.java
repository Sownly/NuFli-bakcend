package com.sownly.nufli.domain.fitness;

public enum ActivityType {
    RUNNING(9.8),
    WALKING(3.8),
    CYCLING(7.5),
    SWIMMING(8.0),
    HIIT(8.5),
    OTHER(5.0);

    private final double defaultMet;

    ActivityType(double defaultMet) {
        this.defaultMet = defaultMet;
    }

    public double getDefaultMet() {
        return defaultMet;
    }
}
