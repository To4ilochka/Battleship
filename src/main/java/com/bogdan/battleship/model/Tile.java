package com.bogdan.battleship.model;

import com.bogdan.battleship.controller.PreparationController;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;
import java.util.Objects;

public class Tile extends Rectangle {

    private ShipPart shipPart;
    private final LinkedList<Ship> nearShips;
    private int boardX;
    private int boardY;

    public Tile(int x, int y) {
        this.boardX = x;
        this.boardY = y;
        this.nearShips = new LinkedList<>();
        setWidth(PreparationController.TILE_SIZE);
        setHeight(PreparationController.TILE_SIZE);

        relocate(x * PreparationController.TILE_SIZE, y * PreparationController.TILE_SIZE);

        setStrokeWidth(1);
        setStroke(Color.valueOf("#6600ff"));
    }

    public boolean isBusy() {
        return hasShipPart() || !nearShips.isEmpty();
    }

    public boolean hasShipPart() {
        return shipPart != null;
    }

    public LinkedList<Ship> getNearShips() {
        return nearShips;
    }

    public ShipPart getShipPart() {
        return shipPart;
    }

    public void setShipPart(ShipPart shipPart) {
        this.shipPart = shipPart;
    }

    public int getBoardX() {
        return boardX;
    }

    public void setBoardX(int boardX) {
        this.boardX = boardX;
    }

    public int getBoardY() {
        return boardY;
    }

    public void setBoardY(int boardY) {
        this.boardY = boardY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return boardX == tile.boardX && boardY == tile.boardY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardX, boardY);
    }
}
