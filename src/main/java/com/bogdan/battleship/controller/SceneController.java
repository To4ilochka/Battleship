package com.bogdan.battleship.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneController {
    private Stage stage;
    private Scene scene;


    public void switchToMenu(Stage stage, double width, double height) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/com/bogdan/battleship/fxml/menu.fxml"));
        Scene scene = new Scene(loader.load());
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/bogdan/battleship/image/icon/icon.png"))));
        stage.setTitle("Battleship");
        stage.setX(960 - width / 2); // middle 960 of the HD
        stage.setY(540 - height / 2); // middle 540 of the HD
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void switchToMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/com/bogdan/battleship/fxml/menu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void switchToOptions(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/com/bogdan/battleship/fxml/options.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void switchToPreparation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/com/bogdan/battleship/fxml/preparation.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }
}