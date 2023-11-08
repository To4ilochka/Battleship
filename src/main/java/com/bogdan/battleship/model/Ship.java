package com.bogdan.battleship.model;

import com.bogdan.battleship.util.ShipType;
import com.bogdan.battleship.util.Direction;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

import java.util.LinkedList;
import java.util.Objects;

import static com.bogdan.battleship.controller.PreparationController.TILE_SIZE;

public class Ship extends Group {

    private Direction oldDirection, newDirection;
    private final Direction anchorDirection;
    private final ShipType shipType;
    private final LinkedList<ShipPart> ship;
    private TileField currentField;
    private TileField hoveredField;
    private double newXPix, newYPix;
    private double oldXPix, oldYPix;
    private ShipPart activeShipPart;
    private final Rotate rotate;

    public Ship(ShipType shipType, Direction direction, TileField currentField, int x, int y) {
        this.shipType = shipType;
        this.anchorDirection = direction;
        this.oldDirection = direction;
        this.newDirection = direction;
        this.ship = new LinkedList<>();
        this.currentField = currentField;
        this.hoveredField = currentField;
        this.oldXPix = x * TILE_SIZE;
        this.oldYPix = y * TILE_SIZE;
        this.newXPix = oldXPix;
        this.newYPix = oldYPix;

        for (int i = 0; i < shipType.getSize(); i++) {
            if (i == 0) {
                initializeShipPart("/com/bogdan/battleship/image/ship/head.png", i, x, y);
            } else if (i == shipType.getSize() - 1) {
                initializeShipPart("/com/bogdan/battleship/image/ship/back.png", i, x, y);
            } else {
                initializeShipPart("/com/bogdan/battleship/image/ship/middle.png", i, x, y);
            }
            if (oldDirection == Direction.UP) {
                y++;
            } else {
                x++;
            }
        }
        this.rotate = new Rotate(0,ship.getFirst().getLayoutX() + TILE_SIZE / 2,
                ship.getFirst().getLayoutY() + TILE_SIZE / 2);
        getTransforms().add(rotate);
    }

    public void move(int newXTale, int newYTale) {
        oldXPix = newXTale * TILE_SIZE;
        oldYPix = newYTale * TILE_SIZE;
        relocate(oldXPix, oldYPix);
        setOldDirection(getNewDirection());
        for (ShipPart shipPart : getShip()) {
            shipPart.setTaleX(newXTale);
            shipPart.setTaleY(newYTale);
            getHoveredField().getBoard()[newXTale][newYTale].setShipPart(shipPart);
            if (getNewDirection() == Direction.UP) {
                newYTale++;
            } else {
                newXTale++;
            }
        }
        getCurrentField().getChildren().remove(this);
        getHoveredField().getChildren().add(this);
        setCurrentField(getHoveredField());
    }

    public void abortMove() {
        for (ShipPart shipPart : getShip()) {
            currentField.getBoard()[shipPart.getTaleX()][shipPart.getTaleY()].setShipPart(shipPart);
        }
        relocate(oldXPix, oldYPix);
        if (newDirection != getOldDirection()) {
            turnOver(getOldDirection());
        }
        setHoveredField(getCurrentField());

    }

    public void turnOver(Direction newDirection) {
        setNewDirection(newDirection);
        rotate.setAngle(angleCalculation(newDirection));
        getTransforms().set(getTransforms().indexOf(rotate), rotate);
    }

    public ShipType getShipType() {
        return shipType;
    }

    public Direction getOldDirection() {
        return oldDirection;
    }

    public void setOldDirection(Direction oldDirection) {
        this.oldDirection = oldDirection;
    }

    public Direction getNewDirection() {
        return newDirection;
    }

    public void setNewDirection(Direction newDirection) {
        this.newDirection = newDirection;
    }

    public LinkedList<ShipPart> getShip() {
        return ship;
    }

    public double getOldXPix() {
        return oldXPix;
    }

    public double getOldYPix() {
        return oldYPix;
    }

    public double getNewXPix() {
        return newXPix;
    }

    public void setNewTileXPix(double newXPix) {
        this.newXPix = newXPix;
    }

    public double getNewYPix() {
        return newYPix;
    }

    public void setNewTileYPix(double newYPix) {
        this.newYPix = newYPix;
    }

    public TileField getCurrentField() {
        return currentField;
    }

    public void setCurrentField(TileField currentField) {
        this.currentField = currentField;
    }

    public TileField getHoveredField() {
        return hoveredField;
    }

    public void setHoveredField(TileField hoveredField) {
        this.hoveredField = hoveredField;
    }

    public ShipPart getActiveShipPart() {
        return activeShipPart;
    }

    public void setActiveShipPart(ShipPart activeShipPart) {
        this.activeShipPart = activeShipPart;
    }

    private void initializeShipPart(String pathToImage, int index, int x, int y) {
        ShipPart shipPart = new ShipPart(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream((pathToImage)))
        ), index, x, y);
        shipPart.relocate(x * TILE_SIZE, y * TILE_SIZE);
        shipPart.setRotate(getOldDirection().getAngle());
        currentField.getBoard()[shipPart.getTaleX()][shipPart.getTaleY()].setShipPart(shipPart);
        getChildren().add(shipPart);
        ship.add(shipPart);
    }

    private int angleCalculation(Direction direction) {
        if (direction == Direction.LEFT) {
            if (anchorDirection == Direction.UP) {
                return -90;
            }
        } else {
            if (anchorDirection == Direction.LEFT) {
                return 90;
            }
        }
        return 0;
    }
}
