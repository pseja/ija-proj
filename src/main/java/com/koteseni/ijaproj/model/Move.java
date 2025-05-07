package com.koteseni.ijaproj.model;

public class Move {
    private final int row;
    private final int col;
    private final long timestamp;

    public Move(int row, int col, long timestamp) {
        this.row = row;
        this.col = col;
        this.timestamp = timestamp;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
