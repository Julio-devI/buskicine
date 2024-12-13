package com.example.projetoptwo;

import com.example.projetoptwo.controllers.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/com/example/projetoptwo/home-view.fxml"));
        BorderPane root = loader.load();

        HomeController controller = loader.getController();
        controller.setStage(stage);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
