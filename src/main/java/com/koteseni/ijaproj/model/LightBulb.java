// Class representing a light bulb tile on the game board and it's methods.
// Authors: Lukas Pseja (xpsejal00), Vaclav Sovak (xsovakv00)

package com.koteseni.ijaproj.model;

import java.util.EnumSet;

/**
 * Represents a light bulb tile on the game board.
 * 
 * @author ≽^•⩊•^≼ The Koteseni Team ≽^•⩊•^≼
 */
public class LightBulb extends Tile {

    /**
     * Creates a new LightBulb at the specified position facing the given direction.
     * 
     * @param row       Row position of the light bulb
     * @param col       Column position of the light bulb
     * @param direction The direction in which the light bulb has a connection
     */
    public LightBulb(int row, int col, Direction direction) {
        super(row, col, EnumSet.of(direction));
    }

    /**
     * Checks if this light bulb is powered.
     * 
     * <p>
     * When all light bulbs on the board are powered, the player wins the game.
     * </p>
     *
     * @return true if the light bulb is powered, false otherwise
     */
    @Override
    public boolean isPowered() {
        return powered;
    }

    /**
     * Gets the direction in which this light bulb has a connection.
     * 
     * <p>
     * Light bulbs have only a single connection point.
     * </p>
     *
     * @return The direction of the light bulb's connection
     */
    public Direction getDirection() {
        return connections.iterator().next();
    }
}
