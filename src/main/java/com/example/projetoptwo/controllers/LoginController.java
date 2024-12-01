package com.example.projetoptwo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoginController {
    @FXML
    private BorderPane root;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton, signUpButton, toggleThemeButton, closeButton, minimizeButton, maximizeButton;

    private Stage stage;
    private double xOffset=0, yOffset = 0;
    private boolean isDarkMode = false;

    public void initialize() {
        loginButton.setOnAction(e -> handleLogin());
        signUpButton.setOnAction(e -> navigateToSignUp());
        closeButton.setOnAction(e -> stage.close());
        minimizeButton.setOnAction(e -> stage.setIconified(true));
        maximizeButton.setOnAction(e -> stage.setFullScreen(!stage.isFullScreen()));
        toggleThemeButton.setOnAction(e -> toggleDarkMode());
        makeStageDraggable();
    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    private void navigateToSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projetoptwo/signup-view.fxml"));
            Parent signUpView = loader.load();

            RegisterController registercontroller = loader.getController();

            registercontroller.setStage(stage);

            Scene signUpScene = new Scene(signUpView, 600, 400);
            stage.setScene(signUpScene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Não foi possível carregar a tela de cadastro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Erro", "Por favor, preencha todos os campos");
        } else if (!isValidEmail(email)) {
            showAlert("Erro", "E-mail inválido");
        } else if (authenticateUser(email, password)) {
            showAlert("Sucesso", "Login realizado com sucesso!");
            navigateToHome(); // Navega para a tela home
        } else {
            showAlert("Erro", "Credenciais inválidas! Verifique o e-mail e a senha.");
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

    private void toggleDarkMode() {
        if (isDarkMode) {
            root.getStylesheets().remove("dark-mode");
        } else {
            root.getStylesheets().add(getClass().getResource("/styles/dark-mode.css").toExternalForm());
        }
        isDarkMode = !isDarkMode;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    private boolean authenticateUser(String email, String password) {
        File file = new File("database/users.txt");

        if (!file.exists()) {
            showAlert("Erro", "Nenhum usuário registrado!");
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Linha lida do arquivo: " + line);

                String[] userData = line.split(";");
                if (userData.length == 2) {
                    System.out.println("Comparando: " + userData[0].trim() + " e " + email.trim());
                    System.out.println("Senha: " + userData[1].trim() + " e " + password.trim());

                    if (userData[0].trim().equalsIgnoreCase(email.trim()) &&
                            userData[1].trim().equals(password.trim())) {
                        return true;
                    }
                } else {
                    System.out.println("Linha mal formatada: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erro", "Erro ao ler o arquivo de usuários: " + e.getMessage());
        }

        return false;
    }


    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    private void navigateToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projetoptwo/home-view.fxml"));
            Parent homeView = loader.load();

            HomeController homeController = loader.getController();
            homeController.setStage(stage);

            Scene homeScene = new Scene(homeView, 600, 400);
            stage.setScene(homeScene);
            stage.show();
        } catch (IOException e) {
            showAlert("Erro", "Não foi possível carregar a tela inicial: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
