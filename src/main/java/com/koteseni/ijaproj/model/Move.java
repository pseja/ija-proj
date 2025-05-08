package com.koteseni.ijaproj.model;

/**
 * Represents a single move made by the player in the game.
 * 
 * @author ≽^•⩊•^≼ The Koteseni Team ≽^•⩊•^≼
 */
public class Move {
    /** Row position of the tile that was rotated. */
    private final int row;

    /** Column position of the tile that was rotated. */
    private final int col;

    /** Timestamp when the move was made (in milliseconds since epoch). */
    private final long timestamp;

    /**
     * Creates a new Move with the specified parameters.
     *
     * @param row       Row position of the tile that was rotated
     * @param col       Column position of the tile that was rotated
     * @param timestamp Timestamp when the move was made
     */
    public Move(int row, int col, long timestamp) {
        this.row = row;
        this.col = col;
        this.timestamp = timestamp;
    }

    /**
     * Gets the row position of the tile that was rotated.
     *
     * @return The row position
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column position of the tile that was rotated.
     *
     * @return The column position
     */
    public int getCol() {
        return col;
    }

    /**
     * Gets the timestamp when the move was made.
     * 
     * <p>
     * The timestamp is measured in milliseconds since the Unix epoch
     * </p>
     *
     * @return The timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }
}
