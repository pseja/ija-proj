package com.koteseni.ijaproj.model;

import java.util.EnumSet;

public abstract class Tile {

    protected int row;
    protected int col;

    protected EnumSet<Direction> connections;

    protected int rotation_count;

    protected boolean powered;

    public Tile(int row, int col, EnumSet<Direction> connections) {
        this.row = row;
        this.col = col;
        this.connections = connections;
        this.powered = false;
        this.rotation_count = 0;
    }

    public void turn() {
        EnumSet<Direction> new_connections = EnumSet.noneOf(Direction.class);

        for (Direction direction : connections) {
            new_connections.add(direction.getNext());
        }

        connections = new_connections;
        rotation_count = (rotation_count + 1) % 4;
    }

    public void turn(int count) {
        for (int i = 0; i < count; i++) {
            turn();
        }
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

    public boolean isPowered() {
        return powered;
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

    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    public int getRotationCount() {
        return rotation_count;
    }

    public void setRotationCount(int rotation_count) {
        this.rotation_count = rotation_count % 4;
    }
}
