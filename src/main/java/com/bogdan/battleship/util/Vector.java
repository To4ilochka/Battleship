package com.bogdan.battleship.util;

public enum Vector {
    UP(0), LEFT(270);

    private final double angle;

    Vector(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }
}
