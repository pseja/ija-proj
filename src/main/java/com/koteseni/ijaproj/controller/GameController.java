package com.koteseni.ijaproj.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.koteseni.ijaproj.model.Board;
import com.koteseni.ijaproj.model.Cell;
import com.koteseni.ijaproj.model.Direction;
import com.koteseni.ijaproj.model.GameLogger;
import com.koteseni.ijaproj.model.LightBulb;
import com.koteseni.ijaproj.model.Source;
import com.koteseni.ijaproj.model.Tile;
import com.koteseni.ijaproj.model.Wire;
import com.koteseni.ijaproj.model.WireShape;
import com.koteseni.ijaproj.view.BoardView;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller for the game view.
 * 
 * @author ≽^•⩊•^≼ The Koteseni Team ≽^•⩊•^≼
 */
public class GameController {

    /** The game board. */
    private Board board;

    /** The board view rendering the game board. */
    private BoardView board_view;

    /** Game logger for tracking moves and game state. */
    private GameLogger game_logger;

    /** Path to the last save file. */
    private String last_saved_game_path;

    /** Counter for the number of moves made by the player. */
    private long move_count;

    /** Flag for displaying the hints overlay. */
    private boolean hints_enabled = false;

    /** Timer for tracking game time. */
    private Timeline timer;

    /** Number of seconds since the game started. */
    private int elapsed_seconds;

    /** Random number generator for board randomization. */
    private final Random random = new Random();

    /** GridPane where the game board is rendered. */
    @FXML
    private GridPane board_grid;

    /** Label displaying the number of moves. */
    @FXML
    private Label move_counter_label;

    /** Label displaying the time since the game started. */
    @FXML
    private Label timer_label;

    /**
     * Starts a new game with the specified difficulty level.
     * 
     * <p>
     * Initializes the timer, move counter, board, logger, generates a random board
     * and visualizes it.
     * </p>
     *
     * @param difficulty The difficulty level
     */
    public void startNewGame(int difficulty) {
        move_count = 0;

        initializeTimer();
        updateMoveCounterLabel();

        // the same algorithm for board size that the LightBulb game uses, pretty cool
        int rows = 5 + (difficulty - 1) * 2;
        int cols = 5 + (difficulty - 1) * 2;

        board = new Board(rows, cols);
        board_view = new BoardView(board_grid, board, this);

        generateBoard();

        game_logger = new GameLogger(board, difficulty);
        last_saved_game_path = null;

        updateBoardView();
    }

    /**
     * Generates the game board.
     * 
     * <p>
     * Uses a spanning tree algorithm to connect all tiles, randomizes the rotations
     * and propagates power.
     * </p>
     */
    private void generateBoard() {
        int rows = board.getRows();
        int cols = board.getCols();

        // random source position avoiding edge tiles
        int source_row = 1 + random.nextInt(rows - 2);
        int source_col = 1 + random.nextInt(cols - 2);

        Map<String, Cell> tree_nodes = generateSpanningTree(source_row, source_col, rows, cols);

        placeLightBulbs(tree_nodes);

        placeWires(tree_nodes);

        rotateAllTiles180Degrees();

        randomizeBoardRotations();

        board.propagatePower();
    }

    /**
     * Generates a spanning tree for the game board.
     * 
     * <p>
     * Creates a connected graph where every cell is reachable from the source cell.
     * </p>
     * 
     * @param source_row Row position of the power source
     * @param source_col Column position of the power source
     * @param rows       Number of rows in the board
     * @param cols       Number of columns in the board
     * 
     * @return Map of cell positions to Cell objects representing the spanning tree
     */
    private Map<String, Cell> generateSpanningTree(int source_row, int source_col, int rows, int cols) {
        Map<String, Cell> cell_map = new HashMap<>();
        List<Cell> cells = new ArrayList<>();

        Cell source_cell = new Cell(source_row, source_col);
        source_cell.setVisited(true);
        cell_map.put(Cell.cellKey(source_row, source_col), source_cell);

        addUnvisitedNeighborsToCells(source_cell, cells, cell_map, rows, cols);

        while (!cells.isEmpty()) {
            int i = random.nextInt(cells.size());
            Cell current = cells.get(i);
            cells.remove(i);

            if (current.isVisited()) {
                continue;
            }

            current.setVisited(true);

            List<Cell> visited_neighbors = getVisitedNeighbors(current, cell_map, rows, cols);
            if (!visited_neighbors.isEmpty()) {
                Cell parent = visited_neighbors.get(random.nextInt(visited_neighbors.size()));

                Direction direction_to_parent = Cell.getDirection(current, parent);
                Direction directino_from_parent = direction_to_parent.getOpposite();

                current.addConnection(direction_to_parent);
                parent.addConnection(directino_from_parent);
            }

            addUnvisitedNeighborsToCells(current, cells, cell_map, rows, cols);
        }

        EnumSet<Direction> source_connections = EnumSet.noneOf(Direction.class);
        source_connections.addAll(source_cell.getConnections());
        WireShape source_shape = WireShape.fromConnections(source_connections);
        Source source = new Source(source_row, source_col, source_shape);
        source.setConnections(source_connections);
        board.setTile(source_row, source_col, source);

        return cell_map;
    }

    /**
     * Adds unvisited neighboring cells to the list of cells to visit.
     *
     * @param cell     The current cell
     * @param cells    List of cells to add the neighbors to
     * @param cell_map Map of cell positions to Cell objects
     * @param rows     Number of rows in the board
     * @param cols     Number of columns in the board
     */
    private void addUnvisitedNeighborsToCells(Cell cell, List<Cell> cells, Map<String, Cell> cell_map, int rows,
            int cols) {

        addNeighbor(cell.getRow() - 1, cell.getCol(), cells, cell_map, rows, cols); // North
        addNeighbor(cell.getRow(), cell.getCol() + 1, cells, cell_map, rows, cols); // East
        addNeighbor(cell.getRow() + 1, cell.getCol(), cells, cell_map, rows, cols); // South
        addNeighbor(cell.getRow(), cell.getCol() - 1, cells, cell_map, rows, cols); // West
    }

    /**
     * Adds a neighbor cell to the list of cells if it's in bounds.
     * 
     * @param row      Row position of the neighbor
     * @param col      Column position of the neighbor
     * @param cells    List of cells to add the neighbor to
     * @param cell_map Map of cell positions to Cell objects
     * @param rows     Total number of rows in the board
     * @param cols     Total number of columns in the board
     */
    private void addNeighbor(int row, int col, List<Cell> cells, Map<String, Cell> cell_map, int rows,
            int cols) {

        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return;
        }

        String key = Cell.cellKey(row, col);
        if (cell_map.containsKey(key)) {
            return;
        }

        Cell neighbor = new Cell(row, col);
        cell_map.put(key, neighbor);
        cells.add(neighbor);
    }

    /**
     * Gets all visited neighbors of a cell.
     * 
     * @param cell     The current cell
     * @param cell_map Map of cell positions to Cell objects
     * @param rows     Number of rows in the board
     * @param cols     Number of columns in the board
     * 
     * @return List of visited neighboring cells
     */
    private List<Cell> getVisitedNeighbors(Cell cell, Map<String, Cell> cell_map, int rows, int cols) {
        List<Cell> neighbors = new ArrayList<>();

        addVisitedNeighbor(cell.getRow() - 1, cell.getCol(), neighbors, cell_map, rows, cols);
        addVisitedNeighbor(cell.getRow(), cell.getCol() + 1, neighbors, cell_map, rows, cols);
        addVisitedNeighbor(cell.getRow() + 1, cell.getCol(), neighbors, cell_map, rows, cols);
        addVisitedNeighbor(cell.getRow(), cell.getCol() - 1, neighbors, cell_map, rows, cols);

        return neighbors;
    }

    /**
     * Adds a visited neighbor cell to the list of neighbors if in bounds.
     * 
     * @param row       Row position of the neighbor
     * @param col       Column position of the neighbor
     * @param neighbors List of visited neighbors
     * @param cell_map  Map of cell positions to Cell objects
     * @param rows      Total number of rows in the board
     * @param cols      Total number of columns in the board
     */
    private void addVisitedNeighbor(int row, int col, List<Cell> neighbors, Map<String, Cell> cell_map, int rows,
            int cols) {

        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return;
        }

        String key = Cell.cellKey(row, col);
        Cell neighbor = cell_map.get(key);

        if (neighbor != null && neighbor.isVisited()) {
            neighbors.add(neighbor);
        }
    }

    /**
     * Gets all leaf cells in the spanning tree.
     * 
     * <p>
     * Leaf cells have one connection which is perfect for light bulbs.
     * </p>
     *
     * @param cell_map Map of cell positions to Cell objects
     * 
     * @return List of leaf cells
     */
    private List<Cell> getLeafCells(Map<String, Cell> cell_map) {
        List<Cell> leaf_cells = new ArrayList<>();

        for (Cell cell : cell_map.values()) {
            if (cell.getConnections().size() == 1) {
                leaf_cells.add(cell);
            }
        }

        return leaf_cells;
    }

    /**
     * Places light bulbs on the leaf cells.
     * 
     * <p>
     * Light bulbs are placed on cells that have one connection orienting them to
     * face that connection.
     * </p>
     * 
     * @param cell_map Map of cell positions to Cell objects
     */
    private void placeLightBulbs(Map<String, Cell> cell_map) {
        List<Cell> leaf_cells = getLeafCells(cell_map);

        for (int i = 0; i < leaf_cells.size(); i++) {
            Cell cell = leaf_cells.get(i);
            leaf_cells.remove(i);

            Direction direction = cell.getConnections().iterator().next();
            LightBulb bulb = new LightBulb(cell.getRow(), cell.getCol(), direction);
            board.setTile(cell.getRow(), cell.getCol(), bulb);
        }
    }

    /**
     * Places wires on cells which are not light bulbs or the source.
     * 
     * @param cell_map Map of cell positions to Cell objects
     */
    private void placeWires(Map<String, Cell> cell_map) {
        for (Cell cell : cell_map.values()) {
            int row = cell.getRow();
            int col = cell.getCol();
            if (board.getTile(row, col) != null) {
                continue;
            }

            EnumSet<Direction> connections = EnumSet.noneOf(Direction.class);
            connections.addAll(cell.getConnections());

            // this converts single connections to a light bulb to avoid trouble with wires
            if (connections.size() == 1) {
                Direction direction = connections.iterator().next();
                LightBulb bulb = new LightBulb(row, col, direction);
                board.setTile(row, col, bulb);

                continue;
            }

            Wire wire = new Wire(row, col, WireShape.fromConnections(connections));
            wire.setConnections(connections);
            board.setTile(row, col, wire);
        }
    }

    /**
     * Randomizes the rotations of tiles on the board.
     * 
     * <ol>
     * <li>Sets the current rotation of each tile as its correct rotation</li>
     * <li>Randomly rotates each tile</li>
     * <li>Fail-safe rotating tiles so that not all light bulbs are powered</li>
     * </ol>
     */
    private void randomizeBoardRotations() {
        int rows = board.getRows();
        int cols = board.getCols();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = board.getTile(row, col);
                if (tile == null) {
                    continue;
                }

                tile.setCorrectRotation(tile.getRotationCount());

                randomizeTileRotation(tile);
            }
        }

        board.propagatePower();

        // rotate randomly until not every light bulb is powered
        while (board.areAllLightBulbsPowered()) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            Tile tile = board.getTile(row, col);

            if (tile instanceof Wire) {
                tile.turn();
                board.propagatePower();
            }
        }
    }

    /**
     * Randomly rotates a single tile.
     * 
     * @param tile The tile to rotate
     */
    private void randomizeTileRotation(Tile tile) {
        if (tile == null) {
            return;
        }

        int rotations = random.nextInt(4);
        for (int i = 0; i < rotations; i++) {
            tile.turn();
        }
    }

    /**
     * Rotates all tiles on the board by 180 degrees.
     * 
     * <p>
     * This is a workaround for an issue where tiles are initially
     * rotated -180 degrees from their expected orientation.
     * </p>
     */
    private void rotateAllTiles180Degrees() {
        int rows = board.getRows();
        int cols = board.getCols();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = board.getTile(row, col);
                if (tile != null) {
                    tile.turn(2);
                }
            }
        }
    }

    /**
     * Updates the board view to show the current state of the board.
     */
    private void updateBoardView() {
        if (board_view == null) {
            return;
        }

        board_view.updateView();
    }

    /**
     * Handles clicking on tiles.
     * 
     * <ol>
     * <li>Rotates the clicked tile</li>
     * <li>Updates the move counter and label</li>
     * <li>Logs the move</li>
     * <li>Checks the win condition</li>
     * </ol>
     *
     * @param row Row position of the clicked tile
     * @param col Column position of the clicked tile
     */
    public void handleTileClick(int row, int col) {
        if (board == null) {
            return;
        }

        Tile tile = board.getTile(row, col);
        if (tile == null) {
            return;
        }

        board.turnTile(row, col);
        move_count++;
        updateMoveCounterLabel();
        tile.setPlayerRotationCount(tile.getPlayerRotationCount() + 1);

        // log moves in play mode
        if (game_logger != null) {
            game_logger.logMove(row, col);
        }

        updateBoardView();

        if (board.areAllLightBulbsPowered()) {
            handleWin();
        }
    }

    /**
     * Handles the win when all light bulbs are powered.
     * 
     * <ol>
     * <li>Stops the timer</li>
     * <li>Automatically saves the completed game</li>
     * <li>Shows a win message</li>
     * <li>Returns to the main menu using the SceneController</li>
     * </ol>
     */
    private void handleWin() {
        if (timer != null) {
            timer.stop();
        }

        // auto save in play mode
        if (game_logger != null) {
            try {
                last_saved_game_path = game_logger.saveGame();
                showInfoBox("Game Won!", "Moves: " + move_count + "\nTime: " + elapsed_seconds + "s\nSaved to: "
                        + last_saved_game_path);
            } catch (IOException e) {
                showErrorBox("Failed to save completed game: " + e.getMessage());
            }
        }

        try {
            Stage stage = (Stage) board_grid.getScene().getWindow();
            SceneController.changeScene("Koteseni - Main Menu", "/com/koteseni/ijaproj/view/main-menu.fxml", stage);
        } catch (IOException e) {
            showErrorBox("Error returning to main menu: " + e.getMessage());
        }
    }

    /**
     * Takes over a game that is being replayed.
     * 
     * @param board      The board state to take over
     * @param difficulty The difficulty level of the game
     */
    public void takeOver(Board board, int difficulty) {
        this.board = board;
        move_count = 0;

        initializeTimer();
        updateMoveCounterLabel();

        board_view = new BoardView(board_grid, board, this);
        game_logger = new GameLogger(board, difficulty);

        updateBoardView();
    }

    /**
     * Handles clicking on the "Hints" button.
     * 
     * <p>
     * Toggles the hint overlay.
     * </p>
     */
    @FXML
    public void handleHintsButton() {
        hints_enabled = !hints_enabled;
        updateBoardView();
    }

    /**
     * Checks if the hint overlay is enabled.
     *
     * @return true if hints are enabled, false otherwise
     */
    public boolean areHintsEnabled() {
        return hints_enabled;
    }

    /**
     * Handles clicking on the "Save" button.
     * 
     * <p>
     * Saves the current game state to a file.
     * </p>
     */
    @FXML
    public void handleSaveButton() {
        try {
            last_saved_game_path = game_logger.saveGame();
            showInfoBox("Saved to: " + last_saved_game_path);
        } catch (IOException e) {
            showErrorBox("Failed to save game: " + e.getMessage());
        }
    }

    /**
     * Handles clicking on the "Back" button.
     * 
     * <p>
     * Prompts the user if he wants to save the game before returning to the main
     * menu using the SceneController.
     * </p>
     */
    @FXML
    public void handleBackButton() {
        if (timer != null) {
            timer.stop();
        }

        Alert confirm_alert = new Alert(AlertType.CONFIRMATION);
        confirm_alert.setTitle("Want to save?");
        confirm_alert.setContentText("Would you like to save the game before returning to the main menu?");

        ButtonType save_button = new ButtonType("Yes");
        ButtonType dont_save_button = new ButtonType("No");
        ButtonType cancel_button = new ButtonType("Cancel",
                ButtonData.CANCEL_CLOSE);

        confirm_alert.getButtonTypes().setAll(save_button, dont_save_button, cancel_button);

        Optional<ButtonType> result = confirm_alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == save_button) {
                try {
                    last_saved_game_path = game_logger.saveGame();
                    showInfoBox("Saved to: " + last_saved_game_path);
                    returnToMainMenu();
                } catch (IOException e) {
                    showErrorBox("Failed to save game: " + e.getMessage());
                }
            } else if (result.get() == dont_save_button) {
                returnToMainMenu();
            } else {
                timer.play();
            }
        }
    }

    /**
     * Returns to the main menu.
     * 
     * <p>
     * Switches the scene using the SceneController.
     * </p>
     */
    private void returnToMainMenu() {
        try {
            Stage stage = (Stage) board_grid.getScene().getWindow();
            SceneController.changeScene("Koteseni - Main Menu",
                    "/com/koteseni/ijaproj/view/main-menu.fxml", stage);
        } catch (IOException e) {
            showErrorBox("Error returning to main menu: " + e.getMessage());
        }
    }

    /**
     * Initializes the game timer.
     * 
     * <p>
     * Sets up a Timeline that updates the timer label every second.
     * </p>
     */
    private void initializeTimer() {
        elapsed_seconds = 0;
        if (timer != null) {
            timer.stop();
        }

        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            elapsed_seconds++;
            updateTimerLabel();
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    /**
     * Updates the move counter label.
     */
    private void updateMoveCounterLabel() {
        move_counter_label.setText("Moves\n" + move_count);
    }

    /**
     * Updates the timer label.
     */
    private void updateTimerLabel() {
        timer_label.setText("Time\n" + elapsed_seconds + "s");
    }

    /**
     * Shows an information dialog with the specified message.
     *
     * @param message The message to display
     */
    private void showInfoBox(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an information dialog with the specified header and message.
     *
     * @param header_message The header message
     * @param message        The content message
     */
    private void showInfoBox(String header_message, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(header_message);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an error dialog with the specified message.
     *
     * @param message The error message to display
     */
    private void showErrorBox(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
