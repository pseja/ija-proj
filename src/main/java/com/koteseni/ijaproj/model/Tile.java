// Abstract base class for all tiles in the game board and it's methods.
// Authors: Lukas Pseja (xpsejal00), Vaclav Sovak (xsovakv00)

package com.koteseni.ijaproj.model;

import java.util.EnumSet;

/**
 * Abstract base class for all tiles in the game board.
 * 
 * @author ≽^•⩊•^≼ The Koteseni Team ≽^•⩊•^≼
 */
public abstract class Tile {

    /** Row position of this tile on the board. */
    protected int row;

    /** Column position of this tile on the board. */
    protected int col;

    /** A set of directions this tile has. */
    protected EnumSet<Direction> connections;

    /** Flag if this tile is currently powered. */
    protected boolean powered;

    /** Current rotation count from 0 to 3. */
    protected int rotation_count;

    /** Number of times the player has rotated this tile. */
    protected int player_rotation_count;

    /** The correct rotation count for completion. */
    protected int correct_rotation;

    /**
     * Creates a new tile with the specified position and connections.
     *
     * @param row         Row position of the tile
     * @param col         Column position of the tile
     * @param connections Set of directions in which this tile has connections
     */
    public Tile(int row, int col, EnumSet<Direction> connections) {
        this.row = row;
        this.col = col;
        this.connections = connections;
        this.powered = false;
        this.rotation_count = 0;
        this.player_rotation_count = 0;
        this.correct_rotation = 0;
    }

    /**
     * Rotates the tile by 90 degrees clockwise.
     */
    public void turn() {
        turn(1);
    }

    /**
     * Rotates the tile by the specified number times clockwise.
     *
     * @param count Number of rotations
     */
    public void turn(int count) {
        for (int i = 0; i < count; i++) {
            EnumSet<Direction> newConnections = EnumSet.noneOf(Direction.class);
            for (Direction direction : connections) {
                newConnections.add(direction.getNext());
            }
            connections = newConnections;

            rotation_count = (rotation_count + 1) % 4;
        }
    }

    /**
     * Gets the number of rotations needed to reach the correct orientation.
     * 
     * <p>
     * Used for the hint overlay.
     * </p>
     *
     * @return Number of clockwise rotations needed to reach the correct orientation
     */
    public int getRotationsToCorrect() {
        int diff = (correct_rotation - rotation_count) % 4;

        if (diff < 0) {
            diff += 4;
        }

        return diff;
    }

    /**
     * Gets the row position of this tile.
     *
     * @return The row position
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column position of this tile.
     *
     * @return The column position
     */
    public int getCol() {
        return col;
    }

    /**
     * Checks if this tile is currently powered.
     *
     * @return true if the tile is powered, false otherwise
     */
    public boolean isPowered() {
        return powered;
    }

    /**
     * Sets the power state of this tile.
     *
     * @param powered true to power the tile, false to unpower it
     */
    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    /**
     * Gets the set of directions in which this tile has connections.
     *
     * @return Set of directions with connections
     */
    public EnumSet<Direction> getConnections() {
        return connections;
    }

    /**
     * Sets the connections for this tile.
     *
     * @param connections Set of directions in which this tile has connections
     */
    public void setConnections(EnumSet<Direction> connections) {
        this.connections = connections;
    }

    /**
     * Gets the current rotation count of this tile.
     *
     * @return The current rotation count (0-3)
     */
    public int getRotationCount() {
        return rotation_count;
    }

    /**
     * Gets the number of times the player has rotated this tile.
     *
     * @return The player rotation count
     */
    public int getPlayerRotationCount() {
        return player_rotation_count;
    }

    /**
     * Sets the player rotation count for this tile.
     *
     * @param player_rotation_count The new player rotation count
     */
    public void setPlayerRotationCount(int player_rotation_count) {
        this.player_rotation_count = player_rotation_count;
    }

    /**
     * Sets the rotation count for this tile.
     *
     * @param rotation_count The new rotation count (0-3)
     */
    public void setRotationCount(int rotation_count) {
        this.rotation_count = rotation_count;
    }

    /**
     * Sets the correct rotation for this tile.
     *
     * @param correct_rotation The correct rotation value (0-3)
     */
    public void setCorrectRotation(int correct_rotation) {
        this.correct_rotation = correct_rotation;
    }
}
