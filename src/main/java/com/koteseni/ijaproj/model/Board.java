package com.koteseni.ijaproj.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Board {

    private int rows;
    private int cols;

    private Tile[][] tiles;
    private Source source;
    private List<LightBulb> light_bulbs;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.tiles = new Tile[rows][cols];
        this.source = null;
        this.light_bulbs = new ArrayList<>();
        initializeEmptyBoard();
    }

    private void initializeEmptyBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tiles[i][j] = null;
            }
        }
    }

    public void turnTile(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return;
        }

        Tile tile = tiles[row][col];
        if (tile == null) {
            return;
        }

        tile.turn();

        propagatePower();
    }

    public void propagatePower() {
        resetPower();

        if (source == null) {
            return;
        }

        // BFS
        Queue<Tile> queue = new LinkedList<>();
        queue.add(source);

        while (!queue.isEmpty()) {
            Tile current = queue.poll();

            int row = current.getRow();
            int col = current.getCol();

            propagatePowerInDirection(queue, current, row - 1, col, Direction.NORTH);
            propagatePowerInDirection(queue, current, row, col + 1, Direction.EAST);
            propagatePowerInDirection(queue, current, row + 1, col, Direction.SOUTH);
            propagatePowerInDirection(queue, current, row, col - 1, Direction.WEST);
        }
    }

    void resetPower() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Tile tile = tiles[i][j];
                if (tile != null) {
                    tile.setPowered(false);
                }
            }
        }
    }

    private void propagatePowerInDirection(Queue<Tile> queue, Tile current_tile, int next_row, int next_col,
            Direction direction) {
        Tile next_tile = getTile(next_row, next_col);
        if (next_tile == null || next_tile.isPowered()) {
            return;
        }

        if (!current_tile.getConnections().contains(direction.getOpposite())) {
            return;
        }

        if (next_tile.getConnections().contains(direction)) {
            next_tile.setPowered(true);
            queue.add(next_tile);
        }
    }

    public Tile getTile(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return null;
        }

        return tiles[row][col];
    }

    public void setTile(int row, int col, Tile tile) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return;
        }

        tiles[row][col] = tile;

        switch (tile) {
            case Source temp_source -> {
                this.source = temp_source;
            }
            case LightBulb light_bulb -> {
                this.light_bulbs.add(light_bulb);
            }
            default -> {
                // do nothing
            }
        }
    }

    public boolean areAllLightBulbsPowered() {
        for (LightBulb light_bulb : light_bulbs) {
            if (!light_bulb.isPowered()) {
                return false;
            }
        }

        return !light_bulbs.isEmpty();
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Source getSource() {
        return source;
    }

    public List<LightBulb> getLightBulbs() {
        return light_bulbs;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public void setLightBulbs(List<LightBulb> light_bulbs) {
        this.light_bulbs = light_bulbs;
    }
}
