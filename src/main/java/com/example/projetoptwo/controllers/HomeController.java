package com.example.projetoptwo.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
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
    private Button closeButton, minimizeButton, maximizeButton, fetchMoviesButton;

    @FXML
    private Label apiResultLabel;

    @FXML
    private GridPane moviesGridPane;

    private Stage stage;
    private double xOffset = 0, yOffset = 0;

    public static class movieData {
        private String title;
        private String imageUrl;

        public movieData(String title, String imageUrl) {
            this.title = title;
            this.imageUrl = imageUrl;
        }

        public String getTitle() { return title; }
        public String getImageUrl() { return imageUrl; }
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
            fetchMoviesButton.setOnAction( e -> fetchPopularMovies());
        }
    }

    private void fetchPopularMovies () {
        String endpoint = BASE_URL + "/movie/popular?api_key=" + API_KEY + "&language=pt-BR";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .build();

        CompletableFuture<Void> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::processMoviesResponse)
                .exceptionally(this::handleError);
    }

    private void processMoviesResponse(String responseBody) {
        try {
            // Basic parsing to extract movie titles
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            //System.out.println(rootNode);
            JsonNode resultsNode = rootNode.get("results");

            moviesList.clear();

            Platform.runLater(() -> {
                // Clear previous movies
                moviesGridPane.getChildren().clear();

                // Populate grid with movie titles
                for (int i = 1; i < resultsNode.size(); i++) {
                    JsonNode movieNode = resultsNode.get(i);
                    String title = movieNode.get("title").asText();
                    String posterPath = movieNode.get("poster_path").asText();
                    String fullUrl = imageBaseUrl + posterPath;

                    movieData movie = new movieData(title, fullUrl);
                    moviesList.add(movie);

                    ImageView imageView = new ImageView(new Image(fullUrl));
                    imageView.setFitWidth(150);
                    imageView.setFitHeight(150);
                    imageView.setPreserveRatio(true);

                    Label titleLabel = new Label(title);

                    moviesGridPane.add(imageView, (i) % 3, (i) / 3 * 2);
                    moviesGridPane.add(titleLabel, (i) % 3, (i) / 3 * 2 + 1);
                }

                apiResultLabel.setText(moviesList.size()+ " filmes carregados!");
            });
        } catch (Exception e) {
            handleError(e);
        }
    }

    public List<movieData> getMoviesList() {
        return new ArrayList<>(moviesList);
    }

    private Void handleError(Throwable e) {
        Platform.runLater(() -> {
            if (apiResultLabel != null) {
                apiResultLabel.setText("Erro na requisição: " + e.getMessage());
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
