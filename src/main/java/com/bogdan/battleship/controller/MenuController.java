package com.bogdan.battleship.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MenuController extends SceneController {
    @FXML
    private Button exitButton;
    @FXML
    private AnchorPane anchorPane;
    private Stage stage;

    public void exit(ActionEvent event) {
        stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
}
