package com.bogdan.battleship.model;

import javafx.scene.Group;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TileField extends Group {
    private Tile[][] board;
    private final int height;
    private final int width;
    private final boolean isAllowProximityShips;

    public TileField(int x, int y, int height, int width, boolean isAllowProximityShips) {
        this.isAllowProximityShips = isAllowProximityShips;
        this.height = height;
        this.width = width;
        relocate(x, y);
        this.board = new Tile[width][height];
        for (int yCount = 0; yCount < height; yCount++) {
            for (int xCount = 0; xCount < width; xCount++) {
                Tile tile = new Tile(xCount, yCount);
                getChildren().add(tile);
                board[xCount][yCount] = tile;
            }
        }
    }

    public Tile[][] getBoard() {
        return board;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isNotAllowProximityShips() {
        return !isAllowProximityShips;
    }

    public void showBusyTiles() {
        Arrays.stream(board)
                .flatMap(Arrays::stream)
                .filter(Tile::hasNearShips)
                .forEach(Tile::paintBoardOrange);
    }

    public void hideBusyTiles() {
        Arrays.stream(board)
                .flatMap(Arrays::stream)
                .filter(Tile::hasNearShips)
                .forEach(Tile::paintBoardDefault);
    }

    public List<Ship> getShips() {
        return getChildren().stream()
                .filter(node -> node instanceof Ship)
                .map(node -> (Ship) node)
                .collect(Collectors.toList());
    }
}
