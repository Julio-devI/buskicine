package com.example.projetoptwo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;

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

    private static final String path = "/com/example/projetoptwo/database/users.txt";

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

        try {
            if (UserAlreadyExists(email)) {
                showAlert("Erro", "Este e-mail já está cadastrado");
                return;
            }

            saveUser(email, password);
            showAlert("Sucesso", "Cadastro realizado com sucesso");

            emailField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
        } catch (IOException e) {
            showAlert("Erro", "Não foi possível salvar o cadastro" + e.getMessage());
        }
    }

    private boolean UserAlreadyExists(String email) throws IOException {
        try (BufferedReader leitor = new BufferedReader(new FileReader(path))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                // Dividir a linha em email e senha
                String[] partes = linha.split(";");
                if (partes.length > 0 && partes[0].equalsIgnoreCase(email)) {
                    return true; // Usuário encontrado
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    private void saveUser(String email, String password) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true)))
        {
            writer.write(email + ";" + password);
            writer.newLine();
        }
    }

    private void navigateToLogin() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/example/projetoptwo/login-view.fxml"));
            javafx.scene.Parent loginView = loader.load();

            LoginController loginController = loader.getController();

            loginController.setStage(stage);

            javafx.scene.Scene loginScene = new javafx.scene.Scene(loginView, 600,400);

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
