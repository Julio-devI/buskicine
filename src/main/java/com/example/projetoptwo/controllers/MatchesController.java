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

        // Configurar as células do ListView para ajustar o texto com quebra de linha
        matchesListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Usar TextFlow para exibir texto ajustável
                    TextFlow textFlow = new TextFlow();
                    textFlow.setPrefWidth(matchesListView.getWidth() - 30); // Ajusta à largura do ListView

                    Text text = new Text(item);
                    text.setWrappingWidth(matchesListView.getWidth() - 50); // Configura quebra de linha automática
                    textFlow.getChildren().add(text);

                    setGraphic(textFlow);
                }
            }
        });

        // Listener para ajustar dinamicamente a largura ao redimensionar
        matchesListView.widthProperty().addListener((obs, oldVal, newVal) -> {
            matchesListView.refresh(); // Atualiza as células para refletir a nova largura
        });

        // Remover barras horizontais no ListView (CSS embutido)
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
                String overview = reader.readLine(); // Lê a próxima linha como sinopse
                if (overview == null) {
                    overview = "Sinopse não disponível.";
                }
                movies.add("Título: " + title + "\nSinopse: " + overview);
                reader.readLine(); // Ignora a linha em branco entre filmes (se existir)
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
            stage.close(); // Fecha a janela atual
        }
    }

    @FXML
    private void handleMinimizeButton() {
        if (stage != null) {
            stage.setIconified(true); // Minimiza a janela
        }
    }

    @FXML
    private void handleMaximizeButton() {
        if (stage != null) {
            stage.setFullScreen(!stage.isFullScreen()); // Alterna para tela cheia
        }
    }

    @FXML
    private void handleBackButton() {
        try {
            // Carregar a tela anterior (home-view.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projetoptwo/home-view.fxml"));
            Parent homeView = loader.load();

            // Configurar o controlador da tela anterior, se necessário
            HomeController homeController = loader.getController();
            homeController.setStage(stage);

            // Alterar a cena para a tela anterior
            Scene homeScene = new Scene(homeView, 800, 600);
            stage.setScene(homeScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}