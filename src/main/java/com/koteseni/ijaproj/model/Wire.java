package com.koteseni.ijaproj.model;

import java.util.EnumSet;

public class Wire extends Tile {

    private final WireShape shape;

    public Wire(int row, int col, WireShape shape) {
        super(row, col, EnumSet.noneOf(Direction.class));
        setConnections(createConnectionsFromShape(shape));
        this.shape = shape;
    }

    private EnumSet<Direction> createConnectionsFromShape(WireShape shape) {
        // had to use temp because of shadowing apparently
        EnumSet<Direction> temp_connections = EnumSet.noneOf(Direction.class);

        switch (shape) {
            case I -> {
                temp_connections.add(Direction.NORTH);
                temp_connections.add(Direction.SOUTH);
            }
            case L -> {
                temp_connections.add(Direction.NORTH);
                temp_connections.add(Direction.EAST);
            }
            case T -> {
                temp_connections.add(Direction.NORTH);
                temp_connections.add(Direction.EAST);
                temp_connections.add(Direction.SOUTH);
            }
            default -> throw new IllegalArgumentException("Invalid wire shape: " + shape);
        }

        return temp_connections;
    }

    @Override
    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    public WireShape getShape() {
        return shape;
    }
}
