package com.bogdan.battleship.model;

import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import static com.bogdan.battleship.controller.PreparationController.TILE_SIZE;

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

    public void setRed() {
        setEffect(new Blend(BlendMode.ADD,
                new ColorInput(0, 0,
                        TILE_SIZE, TILE_SIZE,
                        Color.valueOf("#200000")),
                null)
        );
    }

    public void setDefault() {
        setEffect(null);
    }
}
