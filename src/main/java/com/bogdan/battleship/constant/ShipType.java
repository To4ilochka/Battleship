package com.bogdan.battleship.constant;

public enum ShipType {
    BOAT(1), DESTROYER(2), CRUISER(3), BATTLESHIP(4);
    private final int size;

    ShipType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
