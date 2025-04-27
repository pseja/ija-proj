package com.koteseni.ijaproj.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.koteseni.ijaproj.model.Board;
import com.koteseni.ijaproj.model.Direction;
import com.koteseni.ijaproj.model.LightBulb;
import com.koteseni.ijaproj.model.Source;
import com.koteseni.ijaproj.model.Wire;
import com.koteseni.ijaproj.model.WireShape;
import com.koteseni.ijaproj.view.BoardView;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameController {

    private Board board;
    private BoardView board_view;
    int difficulty;

    // for tracking stats since the game started
    private List<String> logs;
    private long start_time;
    private long move_count;

    private final Random random = new Random();

    @FXML
    private GridPane board_grid;

    public void initialize() {
        logs = new ArrayList<>();
        move_count = 0;
    }

    public void startNewGame(int difficulty) {
        this.difficulty = difficulty;
        logs.clear();
        start_time = System.currentTimeMillis();
        move_count = 0;

        // the same algorithm for board size that the LightBulb game uses, pretty cool
        int rows = 5 + (difficulty - 1) * 2;
        int cols = 5 + (difficulty - 1) * 2;

        board = new Board(rows, cols);
        board_view = new BoardView(board_grid, board, this);

        generateBoard();

        updateBoardView();
    }

    private void generateBoard() {
        // FIXME: just a random board for now
        // wave function collapse coming :peepoClap:
        int rows = board.getRows();
        int cols = board.getCols();

        int source_row = random.nextInt(rows);
        int source_col = random.nextInt(cols);
        WireShape source_shape = WireShape.values()[random.nextInt(WireShape.values().length)];
        Source source = new Source(source_row, source_col, source_shape);
        board.setTile(source_row, source_col, source);

        int num_of_bulbs = difficulty + 2;
        int num_of_bulbs_placed = 0;
        while (num_of_bulbs_placed < num_of_bulbs) {
            int bulb_row = random.nextInt(rows);
            int bulb_col = random.nextInt(cols);

            if (board.getTile(bulb_row, bulb_col) == null) {
                Direction bulb_direction = Direction.values()[random.nextInt(Direction.values().length)];
                LightBulb bulb = new LightBulb(bulb_row, bulb_col, bulb_direction);
                board.setTile(bulb_row, bulb_col, bulb);
                num_of_bulbs_placed++;
            }
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board.getTile(r, c) == null) {
                    WireShape wire_shape = WireShape.values()[random.nextInt(WireShape.values().length)];
                    Wire wire = new Wire(r, c, wire_shape);
                    board.setTile(r, c, wire);
                }
            }
        }

        board.propagatePower();
    }

    private void updateBoardView() {
        if (board_view == null) {
            return;
        }

        board_view.updateView();
    }

    public void handleTileClick(int row, int col) {
        if (board == null) {
            return;
        }

        board.turnTile(row, col);
        move_count++;

        updateBoardView();

        if (board.areAllLightBulbsPowered()) {
            System.out.println("Game Won!");
            ((Stage) board_grid.getScene().getWindow()).close();
        }
    }
}
