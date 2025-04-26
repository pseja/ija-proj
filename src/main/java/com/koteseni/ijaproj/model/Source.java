package com.koteseni.ijaproj.model;

import java.util.EnumSet;

public class Source extends Tile {

    public Source(int row, int col, EnumSet<Direction> connections) {
        super(row, col, connections);
        this.connected = true;
    }

    @Override
    public void setConnected(boolean connected) {
        // always set this to true just in case I try to set it to false somewhere on
        // accident
        this.connected = true;
    }
}
