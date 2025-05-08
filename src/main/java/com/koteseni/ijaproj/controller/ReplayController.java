package com.koteseni.ijaproj.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.koteseni.ijaproj.model.Board;
import com.koteseni.ijaproj.model.GameLogger;
import com.koteseni.ijaproj.model.GameState;
import com.koteseni.ijaproj.view.BoardView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controller for the replay mode of the game.
 * 
 * @author ≽^•⩊•^≼ The Koteseni Team ≽^•⩊•^≼
 */
public class ReplayController {

    /** List view displaying saved games in the data/saves directory. */
    @FXML
    private ListView<String> saved_games_list;

    /** Button to replay the selected game from the list view. */
    @FXML
    private Button replay_selected_game_button;

    /** Button to browse for save files outside of the data/saves directory. */
    @FXML
    private Button browse_button;

    /** Button to return to the main menu. */
    @FXML
    private Button back_button;

    /** Grid pane for the game board. */
    @FXML
    private GridPane board_grid;

    /** Button to step back in the replay. */
    @FXML
    private Button step_back_button;

    /** Button to take over the game from the current move. */
    @FXML
    private Button take_over_button;

    /** Button to step forward in the replay. */
    @FXML
    private Button step_forward_button;

    /** Label showing the current move number. */
    @FXML
    private Label move_counter_label;

    /** Label showing the name of the currently loaded save file. */
    @FXML
    private Label save_name_label;

    /** Date time formatter for displaying dates like "yyyy-MM-dd HH:mm:ss". */
    private final DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** The replay board. */
    private Board board;

    /** Board view for rendering the board. */
    private BoardView board_view;

    /** Game state of the loaded save file. */
    private GameState current_game_state;

    /** Index of the current move. */
    private int current_move_index = -1;

    /**
     * Initializes the replay controller.
     * 
     * <p>
     * Sets the buttons to disabled, clears the move counter and the save name
     * labels and refreshes the list of saved games.
     * </p>
     */
    public void initialize() {
        step_back_button.setDisable(true);
        step_forward_button.setDisable(true);
        take_over_button.setDisable(true);
        move_counter_label.setText("");
        save_name_label.setText("");

        refreshSavedGamesList();
    }

    /**
     * Handles clicking the "Replay selected game" button.
     * 
     * <p>
     * Loads and displays the selected game and if no game is selected, shows an
     * error message.
     * </p>
     */
    @FXML
    private void handleReplaySelectedGameButton() {
        String selected = saved_games_list.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogUtils.showErrorBox("Select a saved game to load first or browse for a save file.");
            return;
        }

        loadGameFromFile("data/saves/" + selected.replace(' ', '_').replace(':', '-') + ".json");
    }

    /**
     * Handles clicking the "Browse" button.
     * 
     * <p>
     * Opens a file chooser to select a save file from the users computer and when
     * selecting a file loads and diplays it.
     * </p>
     */
    @FXML
    private void handleBrowseButton() {
        FileChooser file_chooser = new FileChooser();
        file_chooser.setTitle("Open Saved Game");
        file_chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        File file = file_chooser.showOpenDialog(board_grid.getScene().getWindow());
        if (file != null) {
            loadGameFromFile(file.getAbsolutePath());
        }
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
            SceneController.changeScene("Koteseni - Main Menu",
                    "/com/koteseni/ijaproj/view/main-menu.fxml", stage);
        } catch (IOException e) {
            DialogUtils.showErrorBox("Error returning to main menu: " + e.getMessage());
        }
    }

    /**
     * Handles clicking the "<" button.
     * 
     * <p>
     * Moves the replay one step back. The button is disabled on the 0th move.
     * </p>
     */
    @FXML
    private void handleStepBackButton() {
        if (current_game_state == null) {
            return;
        }

        if (current_move_index > -1) {
            current_move_index--;

            replayMovesUpTo(current_move_index);
            updateMoveCounter();

            step_back_button.setDisable(current_move_index < 0);
            take_over_button.setDisable(current_move_index >= current_game_state.getTotalMoves() - 1);
            step_forward_button.setDisable(false);
        }
    }

    /**
     * Handles clicking the ">" button.
     * 
     * <p>
     * Moves the replay one step forward. The button is disabled on the last move.
     * </p>
     */
    @FXML
    private void handleStepForwardButton() {
        if (current_game_state == null) {
            return;
        }

        int total_moves = current_game_state.getTotalMoves() - 1;

        if (current_move_index < total_moves) {
            current_move_index++;

            replayMovesUpTo(current_move_index);
            updateMoveCounter();

            step_back_button.setDisable(false);
            take_over_button.setDisable(current_move_index >= total_moves);
            step_forward_button.setDisable(current_move_index >= total_moves);
        }
    }

    /**
     * Handles clicking the "Take over" button.
     * 
     * <p>
     * Switches from replay mode to play mode using the SceneController starting
     * the game from the current move.
     * </p>
     */
    @FXML
    private void handleTakeOverButton() {
        if (current_game_state == null) {
            return;
        }

        try {
            Stage stage = (Stage) back_button.getScene().getWindow();
            FXMLLoader loader = SceneController.changeScene("Koteseni",
                    "/com/koteseni/ijaproj/view/game-view.fxml", stage);

            Board new_board = current_game_state.createInitialBoard();
            if (current_move_index >= 0) {
                current_game_state.applyMoves(new_board, current_move_index);
            }

            GameController controller = loader.getController();
            controller.takeOver(new_board, current_game_state.getDifficulty());
        } catch (IOException e) {
            DialogUtils.showErrorBox("Error starting game in play mode: " + e.getMessage());
        }
    }

    /**
     * Loads a game from a save file.
     * 
     * <ol>
     * <li>Parses the save file</li>
     * <li>Creates the initial board</li>
     * <li>Displays the board</li>
     * <li>Enables step forward and take over buttons</li>
     * </ol>
     *
     * @param file_path Path to the save file
     */
    private void loadGameFromFile(String file_path) {
        try {
            current_move_index = -1;

            current_game_state = GameLogger.loadGame(file_path);
            board = current_game_state.createInitialBoard();

            board_view = new BoardView(board_grid, board, null);
            board_view.updateView();

            save_name_label.setText(current_game_state.getStartTime().format(date_formatter));

            step_back_button.setDisable(true);
            step_forward_button.setDisable(false);
            take_over_button.setDisable(false);

            updateMoveCounter();
        } catch (IOException e) {
            DialogUtils.showErrorBox("Failed to load game from file: " + e.getMessage());
        }
    }

    /**
     * Replays the saved game up to the specified move index.
     * 
     * <ol>
     * <li>Resets the board to the initial state</li>
     * <li>Applies all moves up to the specified move index</li>
     * <li>Updates the board view</li>
     * </ol>
     *
     * @param move_index Index of the last move to apply
     */
    private void replayMovesUpTo(int move_index) {
        board = current_game_state.createInitialBoard();

        if (move_index >= 0) {
            current_game_state.applyMoves(board, move_index);
        }

        board.propagatePower();

        board_view = new BoardView(board_grid, board, null);
        board_view.updateView();
    }

    /**
     * Refreshes the list of saved games.
     * 
     * <ol>
     * <li>Gets all save files from the data/saves</li>
     * <li>Loads the date from each file as a display name</li>
     * <li>Shows the games in the list view</li>
     * </ol>
     */
    private void refreshSavedGamesList() {
        try {
            List<Path> saved_games = GameLogger.getSavedGames();
            saved_games_list.getItems().clear();

            for (Path path : saved_games) {
                GameState game_state = GameLogger.loadGame(path.toString());
                String display_name = game_state.getStartTime().format(date_formatter);
                saved_games_list.getItems().add(display_name);
            }
        } catch (IOException e) {
            DialogUtils.showErrorBox("Failed to load saved games list: " + e.getMessage());
        }
    }

    /**
     * Updates the move counter label.
     * 
     * <p>
     * Shows the current move index and the total number of moves.
     * </p>
     */
    private void updateMoveCounter() {
        if (current_game_state == null) {
            return;
        }

        int total_moves = current_game_state.getTotalMoves();
        int current_move = current_move_index + 1;

        move_counter_label.setText("Move " + current_move + "/" + total_moves);
    }
}
