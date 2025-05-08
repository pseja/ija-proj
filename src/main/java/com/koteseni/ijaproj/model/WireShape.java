// Enum class of the possible wire shapes.
// Authors: Lukas Pseja (xpsejal00), Vaclav Sovak (xsovakv00)

package com.koteseni.ijaproj.model;

import java.util.EnumSet;

/**
 * Enumeration of the possible wire shapes.
 * 
 * @author ≽^•⩊•^≼ The Koteseni Team ≽^•⩊•^≼
 */
public enum WireShape {

    /** Straight wire e.g. NORTH to SOUTH */
    I,

    /** T wire with connections in three directions, e.g. NORTH, EAST and SOUTH */
    T,

    /** Corner wire with two connections, e.g. NORTH and EAST */
    L,

    /** Cross wire with connections in all four directions */
    X;

    /**
     * Creates a set of directions based on this wire shape.
     * 
     * @return Set of directions in which this shape has connections
     */
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

    /**
     * Picks the appropriate wire shape based on a set of connections.
     * 
     * @param connections The set of directions in which connections exist
     * 
     * @return The wire shape that matches the given connection pattern
     */
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
