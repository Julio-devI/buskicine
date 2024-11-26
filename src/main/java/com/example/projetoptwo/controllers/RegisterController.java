package com.example.projetoptwo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button registerButton, backToLoginButton;

    private Stage stage;

    public void initialize() {
        registerButton.setOnAction(e -> handleSignUp());
        backToLoginButton.setOnAction(e -> navigateToLogin());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void handleSignUp() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Erro", "Por favor, preencha todos os campos");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Erro", "Por favor, Insira um endereço de e-mail válido");
            return;
        }

        if(!password.equals(confirmPassword)) {
            showAlert("Erro", "Por favor, Insira um endereço de e-mail válido");
            return;
        }

        showAlert("Sucesso", "Cadastro realizado com sucesso");
    }

    private void navigateToLogin() {
        try {
            // Load the Login FXML
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/login-view.fxml"));
            javafx.scene.Parent loginView = loader.load();

            // Get the LoginController
            LoginController loginController = loader.getController();

            // Pass the stage
            loginController.setStage(stage);

            // Create a new scene
            javafx.scene.Scene loginScene = new javafx.scene.Scene(loginView);

            // Set the new scene
            stage.setScene(loginScene);
            stage.show();

        } catch (java.io.IOException e) {
            showAlert("Erro", "Não foi possível carregar a tela de login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]$";
        return email.matches(emailRegex);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
