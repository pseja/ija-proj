// Scene controller class for handling seamless scene transitions and it's methods.
// Authors: Lukas Pseja (xpsejal00), Vaclav Sovak (xsovakv00)

package com.koteseni.ijaproj.controller;

import java.io.IOException;

import com.koteseni.ijaproj.Main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller for handling seamless scene transitions.
 * 
 * @author ≽^•⩊•^≼ The Koteseni Team ≽^•⩊•^≼
 */
public class SceneController {

    /**
     * Seamlessly changes the current scene to a new one.
     *
     * @param scene_name The window title
     * @param fxml_path  The path to the FXML file where the new scene is defined
     * @param stage      The stage to update with the new scene
     * 
     * @return The FXMLLoader used to load the scene
     * 
     * @throws IOException For errors loading the FXML file
     */
    public static FXMLLoader changeScene(String scene_name, String fxml_path, Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml_path));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle(scene_name);
        stage.setScene(scene);
        return loader;
    }
}
