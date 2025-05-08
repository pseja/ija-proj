// Class representing a cell in the spanning tree (STA) board generation algorithm and it's methods.
// Authors: Lukas Pseja (xpsejal00), Vaclav Sovak (xsovakv00)

package com.koteseni.ijaproj.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a cell in the spanning tree (STA) board generation algorithm.
 * 
 * @author ≽^•⩊•^≼ The Koteseni Team ≽^•⩊•^≼
 */
public class Cell {
    /** Row position of this cell on the board. */
    private int row;

    /** Column position of this cell on the board. */
    private int col;

    /** Flag if this cell has been visited during the STA. */
    private boolean visited;

    /** Set of directions in which this cell has connections to neighbors. */
    private Set<Direction> connections;

    /**
     * Creates a new Cell at the specified position.
     * 
     * @param row Row position of the cell
     * @param col Column position of the cell
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.visited = false;
        this.connections = new HashSet<>();
    }

    /**
     * Generates a unique string key for a cell position.
     * 
     * <p>
     * This key is used as a map key to identify cells by position.
     * </p>
     *
     * @param row Row position
     * @param col Column position
     * 
     * @return A string key in the format "row,col"
     */
    public static String cellKey(int row, int col) {
        return row + "," + col;
    }

    /**
     * Determines the direction from one cell to another.
     * 
     * <p>
     * Used during board generation to identify the direction in which a connection
     * should be made between two neighboring cells.
     * </p>
     * 
     * @param from The source cell
     * @param to   The destination cell
     * 
     * @return The direction from the source cell to the destination cell
     */
    public static Direction getDirection(Cell from, Cell to) {
        if (from.getRow() < to.getRow()) {
            return Direction.SOUTH;
        }
        if (from.getRow() > to.getRow()) {
            return Direction.NORTH;
        }
        if (from.getCol() < to.getCol()) {
            return Direction.EAST;
        }

        return Direction.WEST;
    }

    /**
     * Gets the row position of this cell.
     * 
     * @return The row position
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the row position of this cell.
     * 
     * @param row The new row position
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Gets the column position of this cell.
     * 
     * @return The column position
     */
    public int getCol() {
        return col;
    }

    /**
     * Sets the column position of this cell.
     * 
     * @param col The new column position
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Checks if this cell has been visited during the STA.
     * 
     * @return true if the cell has been visited, false otherwise
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Sets the visited state of this cell.
     * 
     * @param visited true to mark the cell as visited, false otherwise
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Gets the set of directions in which this cell has connections.
     * 
     * @return Set of directions with connections
     */
    public Set<Direction> getConnections() {
        return connections;
    }

    /**
     * Sets the connections for this cell.
     * 
     * @param connections Set of directions
     */
    public void setConnections(Set<Direction> connections) {
        this.connections = connections;
    }

    /**
     * Adds a connection in the specified direction.
     * 
     * @param direction The direction in which to add a connection
     */
    public void addConnection(Direction direction) {
        connections.add(direction);
    }
}
