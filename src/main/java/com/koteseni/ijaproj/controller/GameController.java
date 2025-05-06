package com.koteseni.ijaproj.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.koteseni.ijaproj.model.Board;
import com.koteseni.ijaproj.model.Cell;
import com.koteseni.ijaproj.model.Direction;
import com.koteseni.ijaproj.model.LightBulb;
import com.koteseni.ijaproj.model.Source;
import com.koteseni.ijaproj.model.Tile;
import com.koteseni.ijaproj.model.Wire;
import com.koteseni.ijaproj.model.WireShape;
import com.koteseni.ijaproj.view.BoardView;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameController {

    private Board board;
    private BoardView board_view;

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
        int rows = board.getRows();
        int cols = board.getCols();

        // random source position avoiding edge tiles
        int source_row = 1 + random.nextInt(rows - 2);
        int source_col = 1 + random.nextInt(cols - 2);

        Map<String, Cell> tree_nodes = generateSpanningTree(source_row, source_col, rows, cols);

        placeLightBulbs(tree_nodes);

        placeWires(tree_nodes);

        // HACK: this is a hack to fix all the tiles being rotated by -180 degrees for
        // some reason xdddd
        rotateAllTiles180Degrees();

        randomizeBoardRotations();

        board.propagatePower();
    }

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

    private void addUnvisitedNeighborsToCells(Cell cell, List<Cell> frontier, Map<String, Cell> cell_map, int rows,
            int cols) {

        addNeighbor(cell.getRow() - 1, cell.getCol(), frontier, cell_map, rows, cols); // North
        addNeighbor(cell.getRow(), cell.getCol() + 1, frontier, cell_map, rows, cols); // East
        addNeighbor(cell.getRow() + 1, cell.getCol(), frontier, cell_map, rows, cols); // South
        addNeighbor(cell.getRow(), cell.getCol() - 1, frontier, cell_map, rows, cols); // West
    }

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

    private List<Cell> getVisitedNeighbors(Cell cell, Map<String, Cell> cell_map, int rows, int cols) {
        List<Cell> neighbors = new ArrayList<>();

        addVisitedNeighbor(cell.getRow() - 1, cell.getCol(), neighbors, cell_map, rows, cols);
        addVisitedNeighbor(cell.getRow(), cell.getCol() + 1, neighbors, cell_map, rows, cols);
        addVisitedNeighbor(cell.getRow() + 1, cell.getCol(), neighbors, cell_map, rows, cols);
        addVisitedNeighbor(cell.getRow(), cell.getCol() - 1, neighbors, cell_map, rows, cols);

        return neighbors;
    }

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

    private List<Cell> getLeafCells(Map<String, Cell> cell_map) {
        List<Cell> leaf_cells = new ArrayList<>();

        for (Cell cell : cell_map.values()) {
            if (cell.getConnections().size() == 1) {
                leaf_cells.add(cell);
            }
        }

        return leaf_cells;
    }

    private void placeLightBulbs(Map<String, Cell> cell_map) {
        List<Cell> leaf_cells = getLeafCells(cell_map);
        Collections.shuffle(leaf_cells);

        for (int i = 0; i < leaf_cells.size(); i++) {
            Cell cell = leaf_cells.get(i);
            leaf_cells.remove(i);

            Direction direction = cell.getConnections().iterator().next();
            LightBulb bulb = new LightBulb(cell.getRow(), cell.getCol(), direction);
            board.setTile(cell.getRow(), cell.getCol(), bulb);
        }
    }

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

    private void randomizeBoardRotations() {
        int rows = board.getRows();
        int cols = board.getCols();

        // TODO: save the original state of the board here

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int rotations = random.nextInt(4);

                for (int i = 0; i < rotations; i++) {
                    board.getTile(row, col).turn();
                }
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
            System.out.println("Moves: " + move_count);
            System.out.println("Time: " + (System.currentTimeMillis() - start_time) / 1000.0 + " seconds");
            ((Stage) board_grid.getScene().getWindow()).close();
        }
    }
}
