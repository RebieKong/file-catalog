package com.rebiekong.tools.file.catalog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL fxml = getClass().getResource("sample.fxml");
        Parent root = FXMLLoader.load(fxml);
        primaryStage.setTitle("File manger");
        primaryStage.setScene(new Scene(root, 1200, 300));
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
