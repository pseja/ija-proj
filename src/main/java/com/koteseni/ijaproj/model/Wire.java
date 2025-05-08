// Class representing a wire tile on the game board and it's methods.
// Authors: Lukas Pseja (xpsejal00), Vaclav Sovak (xsovakv00)

package com.koteseni.ijaproj.model;

import java.util.EnumSet;

/**
 * Represents a wire tile on the game board.
 * 
 * @author ≽^•⩊•^≼ The Koteseni Team ≽^•⩊•^≼
 */
public class Wire extends Tile {

    /** The wire shape of this wire. */
    private final WireShape shape;

    /**
     * Creates a new Wire at the specified position with the given shape.
     *
     * @param row   Row position of the wire
     * @param col   Column position of the wire
     * @param shape The wire shape defining the wire's connections
     */
    public Wire(int row, int col, WireShape shape) {
        super(row, col, EnumSet.noneOf(Direction.class));
        setConnections(shape.createConnections());
        this.shape = shape;
    }

    /**
     * Sets the power state of this wire.
     * 
     * @param powered true to power the wire, false to unpower it
     */
    @Override
    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    /**
     * Gets the shape of this wire.
     *
     * @return The shape of the wire
     */
    public WireShape getShape() {
        return shape;
    }
}
