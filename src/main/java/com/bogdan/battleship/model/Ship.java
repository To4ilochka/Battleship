package com.bogdan.battleship.model;

import com.bogdan.battleship.util.ShipType;
import com.bogdan.battleship.util.Vector;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

import java.util.LinkedList;
import java.util.Objects;

import static com.bogdan.battleship.controller.PreparationController.TILE_SIZE;

public class Ship extends Group {

    private Vector oldVector, newVector;
    private final Vector anchorVector;
    private final ShipType shipType;
    private final LinkedList<ShipPart> ship;
    private TileField currentField;
    private TileField hoveredField;
    private double newXPix, newYPix;
    private double oldXPix, oldYPix;
    private ShipPart activeShipPart;
    private final Rotate rotate;

    public Ship(ShipType shipType, Vector vector, TileField currentField, int x, int y) {
        this.shipType = shipType;
        this.anchorVector = vector;
        this.oldVector = vector;
        this.newVector = vector;
        this.ship = new LinkedList<>();
        this.currentField = currentField;
        this.hoveredField = currentField;
        this.oldXPix = x * TILE_SIZE;
        this.oldYPix = y * TILE_SIZE;
        this.newXPix = oldXPix;
        this.newYPix = oldYPix;
        this.rotate = new Rotate();

        for (int i = 0; i < shipType.getSize(); i++) {
            if (i == 0) {
                initializeShipPart("/com/bogdan/battleship/image/ship/head.png", x, y);
            } else if (i == shipType.getSize() - 1) {
                initializeShipPart("/com/bogdan/battleship/image/ship/back.png", x, y);
            } else {
                initializeShipPart("/com/bogdan/battleship/image/ship/middle.png", x, y);
            }
            if (oldVector == Vector.UP) {
                y++;
            } else {
                x++;
            }
        }
        getTransforms().add(rotate);
    }

    public void move(int newXTale, int newYTale) {
        oldXPix = newXTale * TILE_SIZE;
        oldYPix = newYTale * TILE_SIZE;
        relocate(oldXPix, oldYPix);
        setOldVector(getNewVector());
        for (ShipPart shipPart : getShip()) {
            shipPart.setTaleX(newXTale);
            shipPart.setTaleY(newYTale);
            getHoveredField().getBoard()[newXTale][newYTale].setShipPart(shipPart);
            if (getNewVector() == Vector.UP) {
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
        if (newVector != getOldVector()) {
            turnOver(getOldVector(),
                    ship.getFirst().getLayoutX() + TILE_SIZE / 2,
                    ship.getFirst().getLayoutY() + TILE_SIZE / 2);
        }
        setHoveredField(getCurrentField());

    }

    public void turnOver(Vector newVector, double xPix, double yPix) {
        setNewVector(newVector);
        rotate.setAngle(angleCalculation(newVector));
        rotate.setPivotX(xPix);
        rotate.setPivotY(yPix);
        getTransforms().set(getTransforms().indexOf(rotate), rotate);
    }

    public ShipType getShipType() {
        return shipType;
    }

    public Vector getOldVector() {
        return oldVector;
    }

    public void setOldVector(Vector oldVector) {
        this.oldVector = oldVector;
    }

    public Vector getNewVector() {
        return newVector;
    }

    public void setNewVector(Vector newVector) {
        this.newVector = newVector;
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

    public void setNewXPix(double newXPix) {
        this.newXPix = newXPix;
    }

    public double getNewYPix() {
        return newYPix;
    }

    public void setNewYPix(double newYPix) {
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

    private void initializeShipPart(String pathToImage, int x, int y) {
        ShipPart shipPart = new ShipPart(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream((pathToImage)))
        ), x, y);
        shipPart.relocate(x * TILE_SIZE, y * TILE_SIZE);
        shipPart.setRotate(getOldVector().getAngle());
        currentField.getBoard()[shipPart.getTaleX()][shipPart.getTaleY()].setShipPart(shipPart);
        getChildren().add(shipPart);
        ship.add(shipPart);
    }

    private int angleCalculation(Vector vector) {
        if (vector == Vector.LEFT) {
            if (anchorVector == Vector.UP) {
                return -90;
            }
        } else {
            if (anchorVector == Vector.LEFT) {
                return 90;
            }
        }
        return 0;
    }
}
