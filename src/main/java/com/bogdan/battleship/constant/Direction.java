package com.bogdan.battleship.constant;

public enum Direction {
    UP(0), LEFT(270);

    private final double angle;

    Direction(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }
}
