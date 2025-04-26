package com.koteseni.ijaproj.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class GameController {

    // private Board board;
    int difficulty;

    // for tracking stats since the game started
    private List<String> logs;
    private long start_time;
    private long move_count;

    @FXML
    private GridPane board_grid;

    public void initialize() {
        logs = new ArrayList<>();
        move_count = 0;
    }

    public void startNewGame(int difficulty) {
        // INFO: just initializing the board for now, nothing interesting is happening
        // yet
        this.difficulty = difficulty;
        logs.clear();
        start_time = System.currentTimeMillis();
        move_count = 0;

        // TODO: remove this later and put it in the logs, here just for testing if the
        // game launches properly
        System.out.println("Started a new game with difficulty: " + difficulty);

        // the same algorithm for board size that the LightBulb game uses, pretty cool
        // int rows = 5 + (difficulty - 1) * 2;
        // int cols = 5 + (difficulty - 1) * 2;

        // TODO: implement the board and the components
        // board = new Board(rows, cols);

        // TODO: implement this with wave function collapse
        // generateBoard();

        // TODO: implement this when the board is generating properly
        // randomizeTileRotation();

        // TODO: uncomment this when working on the visuals
        // updateBoardView();
    }
}
