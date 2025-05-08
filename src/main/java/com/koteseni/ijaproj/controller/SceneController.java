package com.koteseni.ijaproj.controller;

import java.io.IOException;

import com.koteseni.ijaproj.Main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {

    public static FXMLLoader changeScene(String scene_name, String fxml_path, Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml_path));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle(scene_name);
        stage.setScene(scene);
        return loader;
    }
}
