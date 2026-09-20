package com.sownly.nufli.domain.nutrition;

public enum GoalType {
    CUT(-500),
    MAINTAIN(0),
    BULK(300);

    private final int calorieAdjustment;

    GoalType(int calorieAdjustment) {
        this.calorieAdjustment = calorieAdjustment;
    }

    public int getCalorieAdjustment() {
        return calorieAdjustment;
    }
}
