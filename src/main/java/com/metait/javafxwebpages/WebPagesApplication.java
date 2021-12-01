package com.metait.javafxwebpages;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import com.metait.javafxwebpages.WebPagesController;

public class WebPagesApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WebPagesApplication.class.getResource("webpages-view.fxml"));
        WebPagesController controller = new WebPagesController();
        fxmlLoader.setController(controller);
        Parent loadedroot = fxmlLoader.load();
        Scene scene = new Scene(loadedroot, 957, 714);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                controller.handleKeyEvent(event);
            }
        });
        stage.setTitle("List saved webpages of web browsers");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}