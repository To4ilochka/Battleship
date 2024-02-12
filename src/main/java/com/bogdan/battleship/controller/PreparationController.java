package com.bogdan.battleship.controller;

import com.bogdan.battleship.constant.Direction;
import com.bogdan.battleship.constant.ShipType;
import com.bogdan.battleship.model.Ship;
import com.bogdan.battleship.model.ShipPart;
import com.bogdan.battleship.model.TileField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class PreparationController extends SceneController implements Initializable {
    public static final double TILE_SIZE = 32;
    public static final int MAIN_FIELD_WIDTH = 10;
    public static final int MAIN_FIELD_HEIGHT = 10;
    public static final int SMALL_FIELD_WIDTH = 8;
    public static final int SMALL_FIELD_HEIGHT = 5;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView leftClickImage;
    @FXML
    private ImageView rightClickImage;
    @FXML
    private ImageView save;
    private MediaPlayer mediaPlayer;
    private TileField mainTileField;
    private TileField smallTileField;
    private List<TileField> tileFields;
    private double mouseX, mouseY;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.mainTileField = new TileField(352, 64, MAIN_FIELD_HEIGHT, MAIN_FIELD_WIDTH, false);
        this.smallTileField = new TileField(64, 224, SMALL_FIELD_HEIGHT, SMALL_FIELD_WIDTH, true);
        tileFields = new LinkedList<>();
        tileFields.add(mainTileField);
        tileFields.add(smallTileField);
        anchorPane.getChildren().add(mainTileField);
        anchorPane.getChildren().add(smallTileField);
        initImages();
        initShips();
        initSounds();
    }

    @FXML
    public void openSavesPane(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            //todo openSavesPane
        }
    }

    private void initImages() {
        leftClickImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/bogdan/battleship/image/preparation/mouse/left-click.png"))));
        rightClickImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/bogdan/battleship/image/preparation/mouse/right-click.png"))));
        save.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/bogdan/battleship/image/preparation/save/save-image.png"))));
    }

    private void initSounds() {
        leftClickImage.setOnMouseClicked(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.seek(mediaPlayer.getStartTime());
            } else {
                try {
                    mediaPlayer = new MediaPlayer(
                            new Media(Objects.requireNonNull(
                                    getClass().getResource("/com/bogdan/battleship/sound/fart-sound.mp3")
                            ).toURI().toString()));
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
            }
            mediaPlayer.play();
        });
    }

    private void initShips() {
        initializeShip(smallTileField, ShipType.BOAT, Direction.UP, 2, 0);
        initializeShip(smallTileField, ShipType.BOAT, Direction.UP, 3, 0);
        initializeShip(smallTileField, ShipType.BOAT, Direction.UP, 4, 0);
        initializeShip(smallTileField, ShipType.BOAT, Direction.UP, 5, 0);
        initializeShip(smallTileField, ShipType.DESTROYER, Direction.LEFT, 3, 1);
        initializeShip(smallTileField, ShipType.DESTROYER, Direction.LEFT, 2, 2);
        initializeShip(smallTileField, ShipType.DESTROYER, Direction.LEFT, 4, 2);
        initializeShip(smallTileField, ShipType.CRUISER, Direction.LEFT, 1, 3);
        initializeShip(smallTileField, ShipType.CRUISER, Direction.LEFT, 4, 3);
        initializeShip(smallTileField, ShipType.BATTLESHIP, Direction.LEFT, 2, 4);
    }

    private void initializeShip(TileField tileField, ShipType shipType, Direction direction, int x, int y) {
        Ship ship = new Ship(shipType, direction, tileField, x, y);

        tileField.getChildren().add(ship);

        setShipActions(ship);
    }

    private boolean tryMove(Ship ship, int newX, int newY) {
        TileField tileField = ship.getHoveredField();
        for (int i = 1; i <= ship.getShipType().getSize(); i++) {
            if (tileField.getBoard()[newX][newY].isBusy()) {
                return false;
            }
            if (ship.getNewDirection() == Direction.UP) {
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
                    hideAllVisual();
                    ship.abortMove();
                    return;
                }
                relocateMovingShip(ship, e);
                int newXTale = toBoard(ship.getNewXPix() + ship.getCurrentField().getLayoutX()) - toBoard(ship.getHoveredField().getLayoutX());
                int newYTale = toBoard(ship.getNewYPix() + ship.getCurrentField().getLayoutY()) - toBoard(ship.getHoveredField().getLayoutY());

                if (tryMove(ship, newXTale, newYTale)) {
                    ship.move(newXTale, newYTale);
                    if (ship.getCurrentField().isNotAllowProximityShips()) {
                        ship.processSurroundingTiles(ship);
                    }
                } else {
                    ship.abortMove();
                }
                ship.setActiveShipPart(null);
                hideAllVisual();
            }
            if (e.getButton() == MouseButton.SECONDARY) {
                if (ship.isFocused()) {
                    ship.turnOver(ship.getNewDirection() == Direction.LEFT ? Direction.UP : Direction.LEFT);
                    relocateMovingShip(ship, e);
                }
            }
        });
    }

    private void setOnMouseDragged(Ship ship) {
        ship.setOnMouseDragged(e -> {
            if (e.isPrimaryButtonDown()) {
                relocateMovingShip(ship, e);
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
                    double xShipPart = shipPart.getTaleX() * TILE_SIZE;
                    double yShipPart = shipPart.getTaleY() * TILE_SIZE;
                    ship.getCurrentField().getBoard()[shipPart.getTaleX()][shipPart.getTaleY()].setShipPart(null);
                    if (isInRange(x, y, xShipPart, xShipPart + TILE_SIZE, yShipPart, yShipPart + TILE_SIZE)) {
                        ship.setActiveShipPart(shipPart);
                    }
                    if (ship.getCurrentField().isNotAllowProximityShips()) {
                        ship.processSurroundingTiles(null);
                    }
                    showAllVisual(ship);
                }
                ship.getCurrentField().toFront();
            }
        });
    }

    private void showAllVisual(Ship currentShip) {
        tileFields.stream()
                .filter(TileField::isNotAllowProximityShips)
                .forEach(TileField::showBusyTiles);
        tileFields.stream()
                .flatMap(tileField -> tileField.getShips().stream())
                .filter(s -> s != currentShip)
                .forEach(Ship::setRed);
    }

    private void hideAllVisual() {
        tileFields.stream()
                .filter(TileField::isNotAllowProximityShips)
                .forEach(TileField::hideBusyTiles);
        tileFields.stream()
                .flatMap(tileField -> tileField.getShips().stream())
                .forEach(Ship::setDefault);
    }

    private Point2D getShipPixBasedOnDirection(Ship ship, double presentMouseXPix, double presentMouseYPix) {
        double newShipPositionX = calculateNewShipPosition(presentMouseXPix, mouseX, ship.getOldXPix());
        double newShipPositionY = calculateNewShipPosition(presentMouseYPix, mouseY, ship.getOldYPix());
        if (ship.getOldDirection() != ship.getNewDirection()) {
            if (Direction.UP == ship.getNewDirection()) {
                newShipPositionX += ship.getActiveShipPart().getTaleX() * TILE_SIZE - ship.getOldXPix();
                newShipPositionY -= ship.getActiveShipPart().getIndex() * TILE_SIZE;
            } else {
                newShipPositionX -= ship.getActiveShipPart().getIndex() * TILE_SIZE;
                newShipPositionY += ship.getActiveShipPart().getTaleY() * TILE_SIZE - ship.getOldYPix();
            }
        }
        return new Point2D(newShipPositionX, newShipPositionY);
    }

    private double calculateNewShipPosition(double presentMousePix, double oldMousePix, double oldShipPix) {
        return presentMousePix - oldMousePix + oldShipPix;
    }

    private void relocateMovingShip(Ship ship, MouseEvent e) {
        ship.toFront();
        Point2D newPoint = getShipPixBasedOnDirection(ship, e.getSceneX(), e.getSceneY());
        ship.setNewTileXPix(toBoard(newPoint.getX() + ship.getCurrentField().getLayoutX()) * TILE_SIZE - ship.getCurrentField().getLayoutX());
        ship.setNewTileYPix(toBoard(newPoint.getY() + ship.getCurrentField().getLayoutY()) * TILE_SIZE - ship.getCurrentField().getLayoutY());
        ship.setHoveredField(onField(ship, newPoint.getX() + ship.getCurrentField().getLayoutX(),
                newPoint.getY() + ship.getCurrentField().getLayoutY()));
        if (ship.getHoveredField() == null) {
            ship.relocate(newPoint.getX(), newPoint.getY());

        } else {
            ship.relocate(ship.getNewXPix(), ship.getNewYPix());
        }
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

            if (ship.getNewDirection() == Direction.LEFT) {
                if (x >= fieldX && x + ship.getShipType().getSize() * TILE_SIZE <= fieldX + fieldWidth &&
                        y >= fieldY && y + TILE_SIZE <= fieldY + fieldHeight) {
                    return tileField;
                }
            }

            if (ship.getNewDirection() == Direction.UP) {
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
