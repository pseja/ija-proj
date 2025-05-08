package com.koteseni.ijaproj.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private void handleEasyButton() {
        startGame(1);
    }

    @FXML
    private void handleMediumButton() {
        startGame(2);
    }

    @FXML
    private void handleHardButton() {
        startGame(3);
    }

    @FXML
    private void handleHardcoreButton() {
        startGame(4);
    }

    @FXML
    private void handleBackButton() {
        try {
            Stage stage = (Stage) back_button.getScene().getWindow();
            SceneController.changeScene("Koteseni - Main Menu", "/com/koteseni/ijaproj/view/main-menu.fxml", stage);
        } catch (IOException e) {
            showErrorBox("Error returning to main menu: " + e.getMessage());
        }
    }

    private void startGame(int difficulty) {
        try {
            Stage stage = (Stage) back_button.getScene().getWindow();
            FXMLLoader loader = SceneController.changeScene("Koteseni", "/com/koteseni/ijaproj/view/game-view.fxml",
                    stage);

            GameController controller = loader.getController();
            controller.startNewGame(difficulty);
        } catch (IOException e) {
            showErrorBox("Error starting the game: " + e.getMessage());
        }
    }

    private void showErrorBox(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
