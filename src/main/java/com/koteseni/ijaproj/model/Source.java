package com.koteseni.ijaproj.model;

import java.util.EnumSet;

public class Source extends Tile {

    private final WireShape shape;

    public Source(int row, int col, WireShape shape) {
        super(row, col, EnumSet.noneOf(Direction.class));
        setConnections(shape.createConnections());
        this.shape = shape;
        this.powered = true;
    }

    public WireShape getShape() {
        return shape;
    }

    @Override
    public void setPowered(boolean powered) {
        // always set this to true just in case I try to set it to false somewhere on
        // accident
        this.powered = true;
    }
}
