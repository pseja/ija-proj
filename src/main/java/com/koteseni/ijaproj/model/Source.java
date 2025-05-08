// Class representing a power source tile on the game board and it's methods.
// Authors: Lukas Pseja (xpsejal00), Vaclav Sovak (xsovakv00)

package com.koteseni.ijaproj.model;

import java.util.EnumSet;

/**
 * Represents a power source tile on the game board.
 * 
 * @author ≽^•⩊•^≼ The Koteseni Team ≽^•⩊•^≼
 */
public class Source extends Tile {

    /** The wire shape of this source. */
    private final WireShape shape;

    /**
     * Creates a new Source tile at the specified position with the given wire
     * shape.
     * 
     * <p>
     * Only one source can be on the board and it is always powered.
     * </p>
     *
     * @param row   Row position of the source
     * @param col   Column position of the source
     * @param shape The wire shape defining the source's connections
     */
    public Source(int row, int col, WireShape shape) {
        super(row, col, EnumSet.noneOf(Direction.class));
        setConnections(shape.createConnections());
        this.shape = shape;
        this.powered = true;
    }

    /**
     * Gets the wire shape of this source.
     *
     * @return The wire shape of this source
     */
    public WireShape getShape() {
        return shape;
    }

    /**
     * Override of the setPowered method to ensure that a Source is always powered.
     *
     * @param powered Ignored - source is always powered
     */
    @Override
    public void setPowered(boolean powered) {
        this.powered = true;
    }
}
