package com.koteseni.ijaproj.model;

import java.util.EnumSet;

public abstract class Tile {

    protected int row;
    protected int col;
    protected EnumSet<Direction> connections;
    protected boolean powered;
    protected int rotation_count;
    protected int player_rotation_count;
    protected int correct_rotation;

    public Tile(int row, int col, EnumSet<Direction> connections) {
        this.row = row;
        this.col = col;
        this.connections = connections;
        this.powered = false;
        this.rotation_count = 0;
        this.player_rotation_count = 0;
        this.correct_rotation = 0;
    }

    public void turn() {
        turn(1);
    }

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

    public int getRotationsToCorrect() {
        int diff = (correct_rotation - rotation_count) % 4;

        if (diff < 0) {
            diff += 4;
        }

        return diff;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isPowered() {
        return powered;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    public EnumSet<Direction> getConnections() {
        return connections;
    }

    public void setConnections(EnumSet<Direction> connections) {
        this.connections = connections;
    }

    public int getRotationCount() {
        return rotation_count;
    }

    public int getPlayerRotationCount() {
        return player_rotation_count;
    }

    public void setPlayerRotationCount(int player_rotation_count) {
        this.player_rotation_count = player_rotation_count;
    }

    public void setRotationCount(int rotation_count) {
        this.rotation_count = rotation_count;
    }

    public void setCorrectRotation(int correct_rotation) {
        this.correct_rotation = correct_rotation;
    }
}
