package com.bogdan.battleship.util;

public enum ShipType {
    BOAT(1), DESTROYER(2), BATTLESHIP(3), CRUISER(4);
    private final int size;

    ShipType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
