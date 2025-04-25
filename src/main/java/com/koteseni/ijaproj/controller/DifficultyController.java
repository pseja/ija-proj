package com.koteseni.ijaproj.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DifficultyController {

    @FXML
    private Button easy_button;

    @FXML
    private Button medium_button;

    @FXML
    private Button hard_button;

    @FXML
    private Button hardcore_button;

    @FXML
    private Button back_button;

    @FXML
    private void handleEasyButton(ActionEvent event) {
        startGame(1);
    }

    @FXML
    private void handleMediumButton(ActionEvent event) {
        startGame(2);
    }

    @FXML
    private void handleHardButton(ActionEvent event) {
        startGame(3);
    }

    @FXML
    private void handleHardcoreButton(ActionEvent event) {
        startGame(4);
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/koteseni/ijaproj/view/main-menu.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Koteseni - Main Menu");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) back_button.getScene().getWindow()).close();
        } catch (IOException e) {
            showErrorBox("Error returning to main menu: " + e.getMessage());
        }
    }

    private void startGame(int difficulty) {
        showInfoBox("Starting game with difficulty level: " + difficulty);
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
