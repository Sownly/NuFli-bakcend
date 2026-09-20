package com.sownly.nufli.domain.identity;

public enum ActivityLevel {
    SEDENTARY(1.200),
    LIGHTLY_ACTIVE(1.375),
    MODERATELY_ACTIVE(1.550),
    VERY_ACTIVE(1.725),
    EXTRA_ACTIVE(1.900);

    private final double multiplier;

    ActivityLevel(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }
}
