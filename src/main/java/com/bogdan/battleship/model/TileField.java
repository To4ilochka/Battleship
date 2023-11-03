package com.bogdan.battleship.model;

import javafx.scene.Group;
import javafx.scene.layout.Pane;

public class TileField extends Group {
    private Tile[][] board;
    private final int height;
    private final int width;

    public TileField(int x, int y, int height, int width) {
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
}
