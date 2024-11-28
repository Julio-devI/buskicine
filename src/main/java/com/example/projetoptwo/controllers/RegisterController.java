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

    private static final String path = "database/users.txt";

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

        System.out.println(new File("database/users.txt").getAbsolutePath());

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Erro", "Por favor, preencha todos os campos");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Erro", "Por favor, insira um endereço de e-mail válido");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Erro", "As senhas não coincidem, por favor insira as senhas iguais");
            return;
        }

        try {
            if (UserAlreadyExists(email)) {
                showAlert("Erro", "Este e-mail já está cadastrado");
                return; // Adicionado para interromper o fluxo
            }

            saveUser(email, password);
            showAlert("Sucesso", "Cadastro realizado com sucesso");

            emailField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível salvar o cadastro: " + e.getMessage());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("database/users.txt"))) {
            String line;
            System.out.println("Conteúdo atual do arquivo users.txt:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean UserAlreadyExists(String email) throws IOException {
        try (BufferedReader leitor = new BufferedReader(new FileReader(path))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length > 0 && partes[0].equalsIgnoreCase(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    private void saveUser(String email, String password) throws IOException {
        File file = new File("database/users.txt");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(email + ";" + password);
            writer.newLine();
            System.out.println("Usuário salvo com sucesso: " + email);
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
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
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
