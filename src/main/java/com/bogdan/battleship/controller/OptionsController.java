package com.bogdan.battleship.controller;

import javafx.event.ActionEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OptionsController extends SceneController {
    public void scam(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://youtu.be/dQw4w9WgXcQ"));
    }
}
