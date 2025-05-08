package com.koteseni.ijaproj.controller;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    private Button new_game_button;

    @FXML
    private Button replay_game_button;

    @FXML
    private Button exit_button;

    @FXML
    private void handleNewGameButton() {
        try {
            Stage stage = (Stage) new_game_button.getScene().getWindow();
            SceneController.changeScene("Koteseni - Difficulty Menu",
                    "/com/koteseni/ijaproj/view/difficulty-menu.fxml", stage);
        } catch (IOException e) {
            showErrorBox("Error closing the main menu: " + e.getMessage());
        }
    }

    @FXML
    private void handleReplayGameButton() {
        try {
            Stage stage = (Stage) replay_game_button.getScene().getWindow();
            SceneController.changeScene("Koteseni - Replay Game",
                    "/com/koteseni/ijaproj/view/replay-view.fxml", stage);
        } catch (IOException e) {
            showErrorBox("Error loading replay view: " + e.getMessage());
        }
    }

    @FXML
    private void handleExitButton() {
        Platform.exit();
    }

    private void showErrorBox(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
