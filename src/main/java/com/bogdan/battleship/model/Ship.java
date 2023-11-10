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
        this.rotate = new Rotate(0, ship.getFirst().getLayoutX() + TILE_SIZE / 2,
                ship.getFirst().getLayoutY() + TILE_SIZE / 2);
        getTransforms().add(rotate);
        if (!getCurrentField().isAllowProximityShips()) {
            processSurroundingTiles(this);
        }
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

    public void processSurroundingTiles(Ship setNearShip) {
        for (ShipPart shipPart : getShip()) {
            int taleX = shipPart.getTaleX();
            int taleY = shipPart.getTaleY();
            if (Direction.LEFT == getOldDirection()) {
                if (taleY == 0) {
                    calculateTakeTiles(setNearShip, taleX, taleY + 1);
                } else if (taleY == currentField.getHeight() - 1) {
                    calculateTakeTiles(setNearShip, taleX, taleY - 1);
                } else {
                    calculateTakeTiles(setNearShip, taleX, taleY + 1);
                    calculateTakeTiles(setNearShip, taleX, taleY - 1);
                }
            } else {
                if (taleX == 0) {
                    calculateTakeTiles(setNearShip, taleX + 1, taleY);
                } else if (taleX == currentField.getWidth() - 1) {
                    calculateTakeTiles(setNearShip, taleX - 1, taleY);
                } else {
                    calculateTakeTiles(setNearShip, taleX + 1, taleY);
                    calculateTakeTiles(setNearShip, taleX - 1, taleY);
                }
            }
        }
        processNearHeadTiles(setNearShip);
        processNearTailTiles(setNearShip);
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

    private void calculateTakeTiles(Ship nearShip, int taleX, int taleY) {
        if (nearShip != null) {
            currentField.getBoard()[taleX][taleY].getNearShips().add(nearShip);
        } else {
            currentField.getBoard()[taleX][taleY].getNearShips().remove(this);
        }
    }

    private void processNearHeadTiles(Ship setNearShip) {
        ShipPart head = getShip().getFirst();
        if (head.getTaleX() == 0 && head.getTaleY() == 0) {
            return;
        }
        if (Direction.LEFT == getOldDirection()) {
            if (head.getTaleX() == 0) {
                return;
            }
            if (head.getTaleY() != 0) {
                calculateTakeTiles(setNearShip, head.getTaleX() - 1, head.getTaleY() - 1);
            }
            if (head.getTaleY() != currentField.getHeight() - 1) {
                calculateTakeTiles(setNearShip, head.getTaleX() - 1, head.getTaleY() + 1);
            }
            calculateTakeTiles(setNearShip, head.getTaleX() - 1, head.getTaleY());
        } else {
            if (head.getTaleY() == 0) {
                return;
            }
            if (head.getTaleX() != 0) {
                calculateTakeTiles(setNearShip, head.getTaleX() - 1, head.getTaleY() - 1);
            }
            if (head.getTaleX() != currentField.getWidth() - 1) {
                calculateTakeTiles(setNearShip, head.getTaleX() + 1, head.getTaleY() - 1);
            }
            calculateTakeTiles(setNearShip, head.getTaleX(), head.getTaleY() - 1);
        }
    }

    private void processNearTailTiles(Ship setNearShip) {
        ShipPart tail = getShip().getLast();
        int lastTaleX = currentField.getWidth() - 1;
        int lastTaleY = currentField.getHeight() - 1;
        if (tail.getTaleX() == lastTaleX && tail.getTaleY() == lastTaleY) {
            return;
        }
        if (Direction.LEFT == getOldDirection()) {
            if (tail.getTaleX() == lastTaleX) {
                return;
            }
            if (tail.getTaleY() != 0) {
                calculateTakeTiles(setNearShip, tail.getTaleX() + 1, tail.getTaleY() - 1);
            }
            if (tail.getTaleY() != lastTaleY) {
                calculateTakeTiles(setNearShip, tail.getTaleX() + 1, tail.getTaleY() + 1);
            }
            calculateTakeTiles(setNearShip, tail.getTaleX() + 1, tail.getTaleY());
        } else {
            if (tail.getTaleY() == lastTaleY) {
                return;
            }
            if (tail.getTaleX() != 0) {
                calculateTakeTiles(setNearShip, tail.getTaleX() - 1, tail.getTaleY() + 1);
            }
            if (tail.getTaleX() != lastTaleX) {
                calculateTakeTiles(setNearShip, tail.getTaleX() + 1, tail.getTaleY() + 1);
            }
            calculateTakeTiles(setNearShip, tail.getTaleX(), tail.getTaleY() + 1);
        }
    }

}
