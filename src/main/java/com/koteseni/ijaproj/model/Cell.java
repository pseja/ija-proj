package com.koteseni.ijaproj.model;

import java.util.HashSet;
import java.util.Set;

public class Cell {
    private int row;
    private int col;
    private boolean visited;
    private Set<Direction> connections;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.visited = false;
        this.connections = new HashSet<>();
    }

    public static String cellKey(int row, int col) {
        return row + "," + col;
    }

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

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Set<Direction> getConnections() {
        return connections;
    }

    public void setConnections(Set<Direction> connections) {
        this.connections = connections;
    }

    public void addConnection(Direction direction) {
        connections.add(direction);
    }
}