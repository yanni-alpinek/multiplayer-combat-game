package com.in.clientgame;

import com.in.clientgame.controller.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplicationRun extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/in/clientgame/game.fxml"));

        Scene scene = new Scene(loader.load(), 1200, 800);

        GameController controller = loader.getController();

        scene.setOnKeyPressed(controller::handleKeyPressed);

        stage.setScene(scene);
        stage.setTitle("Game Client");
        stage.show();

       stage.setOnCloseRequest(e -> {
           controller.cleanup();
       });
    }
}
