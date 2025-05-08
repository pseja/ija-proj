// Class representing the game board and it's methods.
// Authors: Lukas Pseja (xpsejal00), Vaclav Sovak (xsovakv00)

package com.koteseni.ijaproj.model;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Represents the game board.
 * 
 * @author ≽^•⩊•^≼ The Koteseni Team ≽^•⩊•^≼
 */
public class Board {

    /** Number of rows in the board grid. */
    private int rows;

    /** Number of columns in the board grid. */
    private int cols;

    /** 2D array of tiles representing the game board. */
    private Tile[][] tiles;

    /** The power source on the board. */
    private Source source;

    /** List of all light bulbs on the board. */
    private List<LightBulb> light_bulbs;

    /**
     * Creates a new empty board with the specified dimensions.
     *
     * @param rows Number of rows in the board
     * @param cols Number of columns in the board
     */
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.tiles = new Tile[rows][cols];
        this.source = null;
        this.light_bulbs = new ArrayList<>();
        initializeEmptyBoard();
    }

    /**
     * Initializes the board with null values.
     */
    private void initializeEmptyBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tiles[i][j] = null;
            }
        }
    }

    /**
     * Rotates the tile at the specified position and propagates power.
     * 
     * @param row Row index of the tile to turn
     * @param col Column index of the tile to turn
     */
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

    /**
     * Propagates power from the source through connected tiles.
     * 
     * <p>
     * Uses a BFS algorithm for traversing the board.
     * </p>
     */
    public void propagatePower() {
        resetPower();

        if (source == null) {
            return;
        }

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

    /**
     * Resets the power state of all tiles on the board.
     */
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

    /**
     * Helper method for propagating power in a specific direction.
     * 
     * @param queue        Queue used in the BFS algorithm
     * @param current_tile The current tile propagating power
     * @param next_row     Row index of the next tile
     * @param next_col     Column index of the next tile
     * @param direction    Direction from the current tile to the next tile
     */
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

    /**
     * Gets the tile at the specified position on the board.
     * 
     * @param row Row index
     * @param col Column index
     * 
     * @return The tile, null if out of bounds
     */
    public Tile getTile(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return null;
        }

        return tiles[row][col];
    }

    /**
     * Sets a tile at the specified position on the board.
     * 
     * @param row  Row index
     * @param col  Column index
     * @param tile The tile to place
     */
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

    /**
     * Checks if all light bulbs on the board are powered.
     * 
     * <p>
     * This method is used to determine if the player has won the game.
     * </p>
     * 
     * @return true if all light bulbs are powered, false otherwise
     */
    public boolean areAllLightBulbsPowered() {
        for (LightBulb light_bulb : light_bulbs) {
            if (!light_bulb.isPowered()) {
                return false;
            }
        }

        return !light_bulbs.isEmpty();
    }

    /**
     * Gets the number of rows in the board grid.
     *
     * @return The number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Gets the number of columns in the board grid.
     *
     * @return The number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Gets the 2D array of tiles representing the board.
     *
     * @return The 2D array of tiles
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Gets the power source tile on the board.
     *
     * @return The power source tile
     */
    public Source getSource() {
        return source;
    }

    /**
     * Gets the list of all light bulb tiles on the board.
     *
     * @return The list of light bulbs
     */
    public List<LightBulb> getLightBulbs() {
        return light_bulbs;
    }

    /**
     * Sets the number of rows in the board grid.
     *
     * @param rows The number of rows
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * Sets the number of columns in the board grid.
     *
     * @param cols The number of columns
     */
    public void setCols(int cols) {
        this.cols = cols;
    }

    /**
     * Sets the 2D array of tiles representing the board.
     *
     * @param tiles The 2D array of tiles
     */
    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    /**
     * Sets the power source on the board.
     *
     * @param source The power source tile
     */
    public void setSource(Source source) {
        this.source = source;
    }

    /**
     * Sets the list of light bulbs on the board.
     *
     * @param light_bulbs The list of light bulbs
     */
    public void setLightBulbs(List<LightBulb> light_bulbs) {
        this.light_bulbs = light_bulbs;
    }

    /**
     * Creates a deep copy of the current board.
     * 
     * <p>
     * Creates a new Board instance with copies of all tiles with their power and
     * rotation states, connections and positions.
     * </p>
     * 
     * @return A new deep copy Board instance
     */
    public Board deepCopy() {
        Board board_copy = new Board(rows, cols);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = tiles[row][col];
                if (tile == null) {
                    continue;
                }

                Tile tile_copy = null;

                switch (tile) {
                    case Source local_source -> {
                        tile_copy = new Source(row, col, local_source.getShape());
                        board_copy.source = (Source) tile_copy;
                    }
                    case LightBulb bulb -> {
                        tile_copy = new LightBulb(row, col, bulb.getDirection());
                        board_copy.light_bulbs.add((LightBulb) tile_copy);
                    }
                    case Wire wire -> {
                        tile_copy = new Wire(row, col, wire.getShape());
                    }
                    default -> {
                    }
                }

                if (tile_copy == null) {
                    continue;
                }

                tile_copy.setConnections(EnumSet.copyOf(tile.getConnections()));
                tile_copy.setRotationCount(tile.getRotationCount());
                tile_copy.setPowered(tile.isPowered());
                board_copy.tiles[row][col] = tile_copy;
            }
        }

        return board_copy;
    }
}
