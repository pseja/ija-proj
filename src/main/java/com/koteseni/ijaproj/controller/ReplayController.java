package com.koteseni.ijaproj.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.koteseni.ijaproj.Main;
import com.koteseni.ijaproj.model.Board;
import com.koteseni.ijaproj.model.GameLogger;
import com.koteseni.ijaproj.model.GameState;
import com.koteseni.ijaproj.view.BoardView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ReplayController {

    @FXML
    private ListView<String> saved_games_list;

    @FXML
    private Button replay_selected_game_button;

    @FXML
    private Button browse_button;

    @FXML
    private Button back_button;

    @FXML
    private GridPane board_grid;

    @FXML
    private Button step_back_button;

    @FXML
    private Button take_over_button;

    @FXML
    private Button step_forward_button;

    @FXML
    private Label move_counter_label;

    @FXML
    private Label save_name_label;

    private final DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Board board;
    private BoardView board_view;
    private GameState current_game_state;
    private int current_move_index = -1;

    public void initialize() {
        step_back_button.setDisable(true);
        step_forward_button.setDisable(true);
        take_over_button.setDisable(true);
        move_counter_label.setText("");
        save_name_label.setText("");

        refreshSavedGamesList();
    }

    @FXML
    private void handleReplaySelectedGameButton(ActionEvent event) {
        String selected = saved_games_list.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showErrorBox("Select a saved game to load first or browse for a save file.");
            return;
        }

        loadGameFromFile("data/saves/" + selected.replace(' ', '_').replace(':', '-') + ".json");
    }

    @FXML
    private void handleBrowseButton(ActionEvent event) {
        FileChooser file_chooser = new FileChooser();
        file_chooser.setTitle("Open Saved Game");
        file_chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        File file = file_chooser.showOpenDialog(board_grid.getScene().getWindow());
        if (file != null) {
            loadGameFromFile(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/main-menu.fxml"));
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

    @FXML
    private void handleStepBackButton(ActionEvent event) {
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

    @FXML
    private void handleStepForwardButton(ActionEvent event) {
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

    @FXML
    private void handleTakeOverButton(ActionEvent event) {
        if (current_game_state == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/koteseni/ijaproj/view/game-view.fxml"));
            Parent root = loader.load();

            Board new_board = current_game_state.createInitialBoard();
            if (current_move_index >= 0) {
                current_game_state.applyMoves(new_board, current_move_index);
            }

            GameController controller = loader.getController();
            controller.takeOver(new_board, current_game_state.getDifficulty());

            Stage stage = new Stage();
            stage.setTitle("Koteseni");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) back_button.getScene().getWindow()).close();
        } catch (IOException e) {
            showErrorBox("Error starting game in play mode: " + e.getMessage());
        }
    }

    private void loadGameFromFile(String filePath) {
        try {
            current_move_index = -1;

            current_game_state = GameLogger.loadGame(filePath);
            board = current_game_state.createInitialBoard();

            board_view = new BoardView(board_grid, board, null);
            board_view.updateView();

            save_name_label.setText(current_game_state.getStartTime().format(date_formatter));

            step_back_button.setDisable(true);
            step_forward_button.setDisable(false);
            take_over_button.setDisable(false);

            updateMoveCounter();
        } catch (IOException e) {
            showErrorBox("Failed to load game from file: " + e.getMessage());
        }
    }

    private void replayMovesUpTo(int moveIndex) {
        board = current_game_state.createInitialBoard();

        if (moveIndex >= 0) {
            current_game_state.applyMoves(board, moveIndex);
        }

        board.propagatePower();

        board_view = new BoardView(board_grid, board, null);
        board_view.updateView();
    }

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
            showErrorBox("Failed to load saved games list: " + e.getMessage());
        }
    }

    private void updateMoveCounter() {
        if (current_game_state == null) {
            return;
        }

        int total_moves = current_game_state.getTotalMoves();
        int current_move = current_move_index + 1;

        move_counter_label.setText("Move " + current_move + "/" + total_moves);
    }

    private void showErrorBox(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
