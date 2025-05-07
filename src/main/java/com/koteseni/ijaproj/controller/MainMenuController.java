package com.koteseni.ijaproj.controller;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/koteseni/ijaproj/view/difficulty-menu.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Koteseni - Difficulty Menu");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) exit_button.getScene().getWindow()).close();
        } catch (IOException e) {
            showErrorBox("Error closing the main menu: " + e.getMessage());
        }
    }

    @FXML
    private void handleReplayGameButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/koteseni/ijaproj/view/replay-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Koteseni - Replay Game");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) exit_button.getScene().getWindow()).close();
        } catch (IOException e) {
            showErrorBox("Error loading replay view: " + e.getMessage());
        }
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

    private void showErrorBox(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
