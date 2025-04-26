package com.koteseni.ijaproj.model;

import java.util.EnumSet;

public class LightBulb extends Tile {

    public LightBulb(int row, int col, Direction direction) {
        super(row, col, EnumSet.of(direction));
    }

    @Override
    public boolean isPowered() {
        return powered;
    }

    public Direction getDirection() {
        return connections.iterator().next();
    }
}
