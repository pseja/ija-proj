package com.koteseni.ijaproj.model;

import java.util.EnumSet;

public class Wire extends Tile {

    private final WireShape shape;

    public Wire(int row, int col, WireShape shape) {
        super(row, col, EnumSet.noneOf(Direction.class));
        setConnections(shape.createConnections());
        this.shape = shape;
    }

    @Override
    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    public WireShape getShape() {
        return shape;
    }
}
