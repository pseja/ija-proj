// Main menu controller class for the main menu and it's methods.
// Authors: Lukas Pseja (xpsejal00), Vaclav Sovak (xsovakv00)

package com.koteseni.ijaproj.controller;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller for the main menu.
 * 
 * @author ≽^•⩊•^≼ The Koteseni Team ≽^•⩊•^≼
 */
public class MainMenuController {

    /**
     * Button for starting a new game.
     */
    @FXML
    private Button new_game_button;

    /**
     * Button for going into the replay menu.
     */
    @FXML
    private Button replay_game_button;

    /**
     * Button for exiting the application.
     */
    @FXML
    private Button exit_button;

    /**
     * Handles clicking the "New Game" button.
     * 
     * <p>
     * Switches from the main menu to the difficulty selection menu using the
     * SceneController.
     * </p>
     */
    @FXML
    private void handleNewGameButton() {
        try {
            Stage stage = (Stage) new_game_button.getScene().getWindow();
            SceneController.changeScene("Koteseni - Difficulty Menu",
                    "/com/koteseni/ijaproj/view/difficulty-menu.fxml", stage);
        } catch (IOException e) {
            DialogUtils.showErrorBox("Error closing the main menu: " + e.getMessage());
        }
    }

    /**
     * Handles clicking the "Replay Game" button.
     * 
     * <p>
     * Switches from the main menu to the replay menu using the SceneController.
     * </p>
     */
    @FXML
    private void handleReplayGameButton() {
        try {
            Stage stage = (Stage) replay_game_button.getScene().getWindow();
            SceneController.changeScene("Koteseni - Replay Game",
                    "/com/koteseni/ijaproj/view/replay-view.fxml", stage);
        } catch (IOException e) {
            DialogUtils.showErrorBox("Error loading replay view: " + e.getMessage());
        }
    }

    /**
     * Handles clicking the "Exit" button.
     * 
     * <p>
     * Exits the application.
     * </p>
     */
    @FXML
    private void handleExitButton() {
        Platform.exit();
    }
}
