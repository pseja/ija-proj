package com.koteseni.ijaproj.model;

/**
 * Enum representing the cardinal directions on the game board.
 * 
 * @author ≽^•⩊•^≼ The Koteseni Team ≽^•⩊•^≼
 */
public enum Direction {

    /** North direction */
    NORTH(0),

    /** East direction */
    EAST(1),

    /** South direction */
    SOUTH(2),

    /** West direction */
    WEST(3);

    /** Numeric value associated with the direction used for calculations */
    private final int value;

    /**
     * Creates a Direction with the specified value.
     *
     * @param value Numeric value of the direction
     */
    Direction(int value) {
        this.value = value;
    }

    /**
     * Gets the numeric direction value.
     *
     * @return The numeric value
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the next direction in a clockwise order.
     * 
     * @return The next direction
     */
    public Direction getNext() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    /**
     * Gets the opposite direction.
     * 
     * @return The opposite direction
     */
    public Direction getOpposite() {
        return values()[(this.ordinal() + 2) % values().length];
    }
}
