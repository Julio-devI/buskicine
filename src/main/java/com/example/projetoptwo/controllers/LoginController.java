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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projetoptwo/signup.fxml"));
            Parent signUpView = loader.load();

            RegisterController registercontroller = loader.getController();

            registercontroller.setStage(stage);

            Scene signUpScene = new Scene(signUpView);
        } catch (IOException e) {

        }
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if(email.isEmpty() || password.isEmpty()){
            showAlert("Erro", "Por favor, preencha todos os campos");
        } else if (!isValidEmail(email)) {
            showAlert("Erro", "E-mail invalido");
        } else if (authenticateUser(email, password)) {
            showAlert("Sucesso", "Login realizado com sucesso !");
        } else {
            showAlert("Erro" ," Credenciais inválidas");
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
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]$";
        return email.matches(emailRegex);
    }

    private boolean authenticateUser(String email, String password) {
        return "usuario@example.com".equals(email) && "123456".equals(password);
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
