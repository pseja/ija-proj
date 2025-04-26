package com.koteseni.ijaproj.model;

public enum Direction {
    NORTH(0),
    EAST(1),
    SOUTH(2),
    WEST(3);

    private final int value;

    Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Direction getNext() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    public Direction getOpposite() {
        return values()[(this.ordinal() + 2) % values().length];
    }
}
