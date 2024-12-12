package com.example.projetoptwo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

public class MatchesController {

    @FXML
    private ListView<String> matchesListView;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMatches(List<String> matches) {
        matchesListView.getItems().setAll(matches);
    }

    @FXML
    private void handleCloseButton() {
        if (stage != null) {
            stage.close();
        }
    }

    @FXML
    private void handleMinimizeButton() {
        if (stage != null) {
            stage.setIconified(true);
        }
    }

    @FXML
    private void handleMaximizeButton() {
        if (stage != null) {
            stage.setFullScreen(!stage.isFullScreen());
        }
    }
}