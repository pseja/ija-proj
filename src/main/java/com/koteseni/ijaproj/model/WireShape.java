package com.koteseni.ijaproj.model;

import java.util.EnumSet;

public enum WireShape {

    I,
    T,
    L;

    public EnumSet<Direction> createConnections() {
        EnumSet<Direction> connections = EnumSet.noneOf(Direction.class);

        switch (this) {
            case I -> {
                connections.add(Direction.NORTH);
                connections.add(Direction.SOUTH);
            }
            case L -> {
                connections.add(Direction.NORTH);
                connections.add(Direction.EAST);
            }
            case T -> {
                connections.add(Direction.NORTH);
                connections.add(Direction.EAST);
                connections.add(Direction.SOUTH);
            }
        }

        return connections;
    }
}
