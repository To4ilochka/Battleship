package com.bogdan.battleship.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShipPart extends ImageView {

    private int taleX, taleY;

    public ShipPart(Image image, int taleX, int taleY) {
        super(image);
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
}
