package com.example.projetoptwo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class MatchesController {

    @FXML
    private ListView<String> matchesListView;

    private List<String> matches;

    public void setMatches(List<String> matches) {
        this.matches = matches;
        matchesListView.getItems().setAll(matches);
    }
}