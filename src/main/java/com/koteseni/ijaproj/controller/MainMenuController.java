package com.koteseni.ijaproj.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class MainMenuController {

    @FXML
    private Button new_game_button;

    @FXML
    private Button load_game_button;

    @FXML
    private Button settings_button;

    @FXML
    private Button exit_button;

    @FXML
    private void handleNewGameButton(ActionEvent event) {
        showInfoBox("New game not implemented yet.");
    }

    @FXML
    private void handleLoadGameButton(ActionEvent event) {
        showInfoBox("Load game not implemented yet.");
    }

    @FXML
    private void handleSettingsButton(ActionEvent event) {
        showInfoBox("Settings not implemented yet.");
    }

    @FXML
    private void handleExitButton(ActionEvent event) {
        Platform.exit();
    }

    private void showInfoBox(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // TODO: error alert if needed
}
