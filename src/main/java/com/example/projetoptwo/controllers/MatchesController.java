package com.example.projetoptwo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MatchesController {

    @FXML
    private ListView<String> matchesListView;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        loadMoviesFromFile();

        matchesListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                
                    TextFlow textFlow = new TextFlow();
                    textFlow.setPrefWidth(matchesListView.getWidth() - 30);

                    Text text = new Text(item);
                    text.setWrappingWidth(matchesListView.getWidth() - 50);
                    textFlow.getChildren().add(text);

                    setGraphic(textFlow);
                }
            }
        });

        
        matchesListView.widthProperty().addListener((obs, oldVal, newVal) -> {
            matchesListView.refresh();
        });

        matchesListView.setStyle("-fx-hbar-policy: never;");
    }

    private void loadMoviesFromFile() {
        File file = new File("database/films.txt");

        if (!file.exists()) {
            matchesListView.getItems().add("Nenhum filme encontrado.");
            return;
        }

        List<String> movies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String title;
            while ((title = reader.readLine()) != null) {
                String overview = reader.readLine();
                if (overview == null) {
                    overview = "Sinopse não disponível.";
                }
                movies.add("Título: " + title + "\nSinopse: " + overview);
                reader.readLine();
            }
        } catch (IOException e) {
            matchesListView.getItems().add("Erro ao carregar os filmes: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        matchesListView.getItems().setAll(movies);
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

    @FXML
    private void handleBackButton() {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projetoptwo/home-view.fxml"));
            Parent homeView = loader.load();

            
            HomeController homeController = loader.getController();
            homeController.setStage(stage);

            
            Scene homeScene = new Scene(homeView, 800, 600);
            stage.setScene(homeScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}