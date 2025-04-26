package com.koteseni.ijaproj.model;

import java.util.EnumSet;

public abstract class Tile {

    protected int row;
    protected int col;

    protected EnumSet<Direction> connections;

    protected boolean connected;

    public Tile(int row, int col, EnumSet<Direction> connections) {
        this.row = row;
        this.col = col;
        this.connections = connections;
        this.connected = false;
    }

    public void turn() {
        EnumSet<Direction> new_connections = EnumSet.noneOf(Direction.class);

        for (Direction direction : connections) {
            new_connections.add(direction.getNext());
        }

        connections = new_connections;
    }

    public boolean hasConnection(Direction direction) {
        return connections.contains(direction);
    }

    public boolean canConnect(Tile other, Direction direction) {
        if (other == null) {
            return false;
        }

        return hasConnection(direction) && other.hasConnection(direction.getOpposite());
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public EnumSet<Direction> getConnections() {
        return connections;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setConnections(EnumSet<Direction> connections) {
        this.connections = connections;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
