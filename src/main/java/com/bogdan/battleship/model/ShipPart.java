package com.bogdan.battleship.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShipPart extends ImageView {

    private int taleX, taleY;
    private final int index;

    public ShipPart(Image image, int index, int taleX, int taleY) {
        super(image);
        this.index = index;
        this.taleX = taleX;
        this.taleY = taleY;
    }

    public int getTaleX() {
        return taleX;
    }

    public void setTaleX(int taleX) {
        this.taleX = taleX;
    }

    public int getTaleY() {
        return taleY;
    }

    public void setTaleY(int taleY) {
        this.taleY = taleY;
    }

    public int getIndex() {
        return index;
    }
}
