package com.koteseni.ijaproj.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller for the difficulty selection menu.
 * 
 * @author ≽^•⩊•^≼ The Koteseni Team ≽^•⩊•^≼
 */
public class DifficultyController {

    /** Button for selecting the "Easy" difficulty. */
    @FXML
    private Button easy_button;

    /** Button for selecting the "Medium" difficulty. */
    @FXML
    private Button medium_button;

    /** Button for selecting the "Hard" difficulty. */
    @FXML
    private Button hard_button;

    /** Button for selecting the "Hardcore" difficulty. */
    @FXML
    private Button hardcore_button;

    /** Button for returning to the main menu. */
    @FXML
    private Button back_button;

    /**
     * Handles clicking the "Easy" button.
     * 
     * <p>
     * Starts a new game with difficulty level 1 (5x5 board).
     * </p>
     */
    @FXML
    private void handleEasyButton() {
        startGame(1);
    }

    /**
     * Handles clicking the "Medium" button.
     * 
     * <p>
     * Starts a new game with difficulty level 2 (7x7 board).
     * </p>
     */
    @FXML
    private void handleMediumButton() {
        startGame(2);
    }

    /**
     * Handles clicking the "Hard" button.
     * 
     * <p>
     * Starts a new game with difficulty level 3 (9x9 board).
     * </p>
     */
    @FXML
    private void handleHardButton() {
        startGame(3);
    }

    /**
     * Handles clicking the "Hardcore" button.
     * 
     * <p>
     * Starts a new game with difficulty level 4 (11x11 board).
     * </p>
     */
    @FXML
    private void handleHardcoreButton() {
        startGame(4);
    }

    /**
     * Handles clicking the "Back" button.
     * 
     * <p>
     * Returns to the main menu by changing the scene using the SceneController.
     * </p>
     */
    @FXML
    private void handleBackButton() {
        try {
            Stage stage = (Stage) back_button.getScene().getWindow();
            SceneController.changeScene("Koteseni - Main Menu", "/com/koteseni/ijaproj/view/main-menu.fxml", stage);
        } catch (IOException e) {
            showErrorBox("Error returning to main menu: " + e.getMessage());
        }
    }

    /**
     * Starts a new game with the specified difficulty level.
     * 
     * <p>
     * Switches from the difficulty menu to the game view using the SceneController.
     * </p>
     *
     * @param difficulty The difficulty level
     */
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

    /**
     * Displays an error dialog with the specified message.
     *
     * @param message The error message to display
     */
    private void showErrorBox(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
