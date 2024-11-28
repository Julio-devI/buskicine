package com.example.projetoptwo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private BorderPane root;

    @FXML
    private Button closeButton, minimizeButton, maximizeButton;

    private Stage stage;
    private double xOffset = 0, yOffset = 0;

    @FXML
    public void initialize() {

        closeButton.setOnAction(e -> stage.close());
        minimizeButton.setOnAction(e -> stage.setIconified(true));
        maximizeButton.setOnAction(e -> stage.setFullScreen(!stage.isFullScreen()));

        makeStageDraggable();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void makeStageDraggable() {
        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}
