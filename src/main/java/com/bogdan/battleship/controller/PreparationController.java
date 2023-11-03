package com.bogdan.battleship.controller;

import com.bogdan.battleship.model.Ship;
import com.bogdan.battleship.model.ShipPart;
import com.bogdan.battleship.model.TileField;
import com.bogdan.battleship.util.ShipType;
import com.bogdan.battleship.util.Vector;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import java.util.LinkedList;

public class PreparationController extends SceneController {
    public static final double TILE_SIZE = 32;
    public static final int MAIN_FIELD_WIDTH = 10;
    public static final int MAIN_FIELD_HEIGHT = 10;
    public static final int SMALL_FIELD_WIDTH = 8;
    public static final int SMALL_FIELD_HEIGHT = 5;
    @FXML
    private AnchorPane anchorPane;
    private TileField mainTileField;
    private TileField smallTileField;
    private LinkedList<TileField> tileFields;
    private double mouseX, mouseY;

    public void initPreparationController() {
        this.mainTileField = new TileField(352, 64, MAIN_FIELD_HEIGHT, MAIN_FIELD_WIDTH);
        this.smallTileField = new TileField(64, 224, SMALL_FIELD_HEIGHT, SMALL_FIELD_WIDTH);
        tileFields = new LinkedList<>();
        tileFields.add(mainTileField);
        tileFields.add(smallTileField);
        anchorPane.getChildren().add(mainTileField);
        anchorPane.getChildren().add(smallTileField);
        initShips();
    }

    private void initShips() {
        initializeShip(mainTileField, ShipType.CRUISER, Vector.UP, 1, 1);
        initializeShip(mainTileField, ShipType.CRUISER, Vector.LEFT, 2, 3);
        initializeShip(mainTileField, ShipType.BOAT, Vector.UP, 4, 4);
        initializeShip(smallTileField, ShipType.BATTLESHIP, Vector.UP, 0, 0);
    }

    private void initializeShip(TileField tileField, ShipType shipType, Vector vector, int x, int y) {
        Ship ship = new Ship(shipType, vector, tileField, x, y);

        tileField.getChildren().add(ship);

        setShipActions(ship);
    }

    private boolean tryMove(Ship ship, int newX, int newY) {
        TileField tileField = ship.getHoveredField();
        for (int i = 1; i <= ship.getShipType().getSize(); i++) {
            if (newX >= tileField.getWidth() || newY >= tileField.getHeight()) {
                return false;
            }
            if (tileField.getBoard()[newX][newY].hasShipPart()) {
                return false;
            }
            if (ship.getNewVector() == Vector.UP) {
                newY++;
            } else {
                newX++;
            }
        }
        return true;
    }

    private void setOnMouseReleased(Ship ship) {
        ship.setOnMouseReleased(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                anchorPane.requestFocus();
                if (ship.getHoveredField() == null) {
                    ship.abortMove();
                    return;
                }
                int newXTale = toBoard(ship.getNewXPix() + ship.getCurrentField().getLayoutX()) - toBoard(ship.getHoveredField().getLayoutX());
                int newYTale = toBoard(ship.getNewYPix() + ship.getCurrentField().getLayoutY()) - toBoard(ship.getHoveredField().getLayoutY());

                if (tryMove(ship, newXTale, newYTale)) {
                    ship.move(newXTale, newYTale);
                } else {
                    ship.abortMove();
                }
                ship.setActiveShipPart(null);
            }
            if (e.getButton() == MouseButton.SECONDARY) {
                if (ship.isFocused()) {
                    ship.turnOver(ship.getNewVector() == Vector.LEFT ? Vector.UP : Vector.LEFT,
                            ship.getShip().getFirst().getLayoutX() + TILE_SIZE / 2,
                            ship.getShip().getFirst().getLayoutY() + TILE_SIZE / 2
                    );
                }
            }
        });
    }

    private void setOnMouseDragged(Ship ship) {
        ship.setOnMouseDragged(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                ship.toFront();
                double differenceX = e.getSceneX() - mouseX + ship.getOldXPix();
                double differenceY = e.getSceneY() - mouseY + ship.getOldYPix();
                ship.setNewXPix(toBoard(differenceX + ship.getCurrentField().getLayoutX()) * TILE_SIZE - ship.getCurrentField().getLayoutX());
                ship.setNewYPix(toBoard(differenceY + ship.getCurrentField().getLayoutY()) * TILE_SIZE - ship.getCurrentField().getLayoutY());
                ship.setHoveredField(onField(ship, differenceX + ship.getCurrentField().getLayoutX(),
                        differenceY + ship.getCurrentField().getLayoutY()));

                if (ship.getHoveredField() == null) {
                    ship.relocate(differenceX, differenceY);
                } else {
                    ship.relocate(ship.getNewXPix(), ship.getNewYPix());
                }
            }
        });
    }

    private void setOnMousePressed(Ship ship) {
        ship.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                ship.requestFocus();
                mouseX = e.getSceneX();
                mouseY = e.getSceneY();
                for (ShipPart shipPart : ship.getShip()) {
                    double x = e.getSceneX() - ship.getCurrentField().getLayoutX();
                    double y = e.getSceneY() - ship.getCurrentField().getLayoutY();
                    double xShipPart = shipPart.getLayoutX() + ship.getLayoutX();
                    double yShipPart = shipPart.getLayoutY() + ship.getLayoutY();
                    ship.getCurrentField().getBoard()[shipPart.getTaleX()][shipPart.getTaleY()].setShipPart(null);
                    if (isInRange(x, y, xShipPart, xShipPart + TILE_SIZE, yShipPart, yShipPart + TILE_SIZE)) {
                        ship.setActiveShipPart(shipPart);
                        System.out.println(shipPart);
                    }
                }
                ship.getCurrentField().toFront();
            }
        });
    }

    private void setShipActions(Ship ship) {
        setOnMousePressed(ship);

        setOnMouseDragged(ship);

        setOnMouseReleased(ship);
    }

    private boolean isInRange(double x, double y, double minX, double maxX, double minY, double maxY) {
        return x >= minX && x <= maxX &&
                y >= minY && y <= maxY;
    }

    private TileField onField(Ship ship, double x, double y) {
        for (TileField tileField : tileFields) {
            double fieldX = tileField.getLayoutX();
            double fieldY = tileField.getLayoutY();
            double fieldWidth = tileField.getWidth() * TILE_SIZE;
            double fieldHeight = tileField.getHeight() * TILE_SIZE;

            if (ship.getNewVector() == Vector.LEFT) {
                if (x >= fieldX && x + ship.getShipType().getSize() * TILE_SIZE <= fieldX + fieldWidth &&
                        y >= fieldY && y + TILE_SIZE <= fieldY + fieldHeight) {
                    return tileField;
                }
            }

            if (ship.getNewVector() == Vector.UP) {
                if (x >= fieldX && x + TILE_SIZE <= fieldX + fieldWidth &&
                        y >= fieldY && y + ship.getShipType().getSize() * TILE_SIZE <= fieldY + fieldHeight) {
                    return tileField;
                }
            }
        }
        return null;
    }

    private int toBoard(double pixel) {
        return (int) ((int) (pixel + TILE_SIZE / 2) / TILE_SIZE);
    }

}
