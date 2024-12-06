package com.example.projetoptwo.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HomeController {

    @FXML
    private BorderPane root;

    @FXML
    private Button closeButton, minimizeButton, maximizeButton, fetchMoviesButton, saveMovieButton;

    @FXML
    private Label resultTitleLabel;

    @FXML
    private Label resultOverviewLabel;

    @FXML
    private ImageView resultImageView;

    private Stage stage;
    private double xOffset = 0, yOffset = 0;

    private int currentPage = 1;

    private static final String path = "database/films.txt";

    public static class movieData {
        private String title;
        private String imageUrl;
        private String overview;

        public movieData(String title, String imageUrl, String overview) {
            this.title = title;
            this.imageUrl = imageUrl;
            this.overview = overview;
        }

        public String getTitle() { return title; }
        public String getImageUrl() { return imageUrl; }
        public String getOverview() { return overview; }
    }

    private List<movieData> moviesList = new ArrayList<>();

    //credenciais de acesso da api dos site dos filmim
    private static final String API_KEY = "3b8175d4cbf708a38a0625be8f237957";
    private static final String BASE_URL="https://api.themoviedb.org/3";
    private static final String imageBaseUrl = "https://image.tmdb.org/t/p/w500";

    @FXML
    public void initialize() {
        setupWindowControls();
        makeStageDraggable();
        setupMovieFetching();
        setupSaveMovie();
    }

    public void setupWindowControls()
    {
        closeButton.setOnAction(e -> stage.close());
        minimizeButton.setOnAction(e -> stage.setIconified(true));
        maximizeButton.setOnAction(e -> stage.setFullScreen(!stage.isFullScreen()));
    }

    private void setupMovieFetching()
    {
        if (fetchMoviesButton != null) {
            fetchMoviesButton.setOnAction( e -> fetchSingleMovie());
        }
    }

    private void fetchSingleMovie() {
        String endpoint = BASE_URL + "/movie/popular?api_key=" + API_KEY + "&language=pt-BR&page=" + currentPage;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .build();

        CompletableFuture<Void> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::processSingleMovieResponse)
                .exceptionally(this::handleError);
    }

    private void processSingleMovieResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode resultsNode = rootNode.get("results");

            System.out.println(resultsNode.toString());

            if (!resultsNode.isEmpty())
            {
                JsonNode movieNode = resultsNode.get(0);

                String title = movieNode.get("title").asText("Título Desconhecido");
                String posterPath = movieNode.path("poster_path").asText("");
                String overview = movieNode.get("overview").asText("Descrição não disponível");

                Platform.runLater(() -> {
                    resultTitleLabel.setText(title);
                    resultOverviewLabel.setText(overview);

                    if (!posterPath.isEmpty()) {
                        String fullPosterUrl = imageBaseUrl + posterPath;
                        resultImageView.setImage(new Image(fullPosterUrl));
                    }
                });

                currentPage++;
            }
            moviesList.clear();
        } catch (Exception e) {
            handleError(e);
        }
    }

    private void setupSaveMovie()
    {
        if (saveMovieButton != null) {
            saveMovieButton.setOnAction(e -> {
                try {
                    String title = resultTitleLabel.getText();
                    String overview = resultOverviewLabel.getText();

                    System.out.println("Saving movie: " + title + " - " + overview);

                    saveMovie(title, overview);
                } catch (IOException ex) {
                    // Melhor tratamento da exceção
                    System.err.println("Error saving the movie: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void saveMovie(String title, String overview) throws IOException {
        File file = new File("database/films.txt");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(title);
            writer.newLine();
            writer.write(overview);
            showAlert("Sucesso", "Filme salvo com sucesso");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<movieData> getMoviesList() {
        return new ArrayList<>(moviesList);
    }

    private Void handleError(Throwable e) {
        Platform.runLater(() -> {
            if (resultTitleLabel != null) {
                resultTitleLabel.setText("Erro na requisição: " + e.getMessage());
                e.printStackTrace();
            }
        });
        return null;
    }

    public void setStage(Stage stage) {
        if (stage != null) {
            this.stage = stage;
        }
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
