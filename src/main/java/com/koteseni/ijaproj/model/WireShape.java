package com.koteseni.ijaproj.model;

import java.util.EnumSet;

public enum WireShape {

    I,
    T,
    L,
    X;

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
            case X -> {
                connections.add(Direction.NORTH);
                connections.add(Direction.EAST);
                connections.add(Direction.SOUTH);
                connections.add(Direction.WEST);
            }
        }

        return connections;
    }

    public static WireShape fromConnections(EnumSet<Direction> connections) {
        return switch (connections.size()) {
            case 2 -> {
                if ((connections.contains(Direction.NORTH) && connections.contains(Direction.SOUTH)) ||
                        (connections.contains(Direction.EAST) && connections.contains(Direction.WEST))) {
                    yield I;
                } else {
                    yield L;
                }
            }
            case 3 -> T;
            case 4 -> X;
            default -> I;
        };
    }
}
