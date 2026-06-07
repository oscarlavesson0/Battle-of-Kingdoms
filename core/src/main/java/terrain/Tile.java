package terrain;

import base.BaseStats;
import building.Building;
import unit.Unit;

/**
 * Represents a single tile on the map. A tile has a position, terrain type,
 * and may optionally contain a unit, a base, or a building.
 *
 * @author JoelAxel Olsson
 * @author Emil Hadzic
 * @author Enid Becarevic
 */
public class Tile {

    private final int x;
    private final int y;
    private Terrain terrain;
    private BaseStats base;
    private Unit unit;
    private Building building;

    /**
     * Creates a tile at the given coordinates with a terrain type.
     *
     * @param x tile X-oordinate
     * @param y tile Y-coordinate
     * @param terrain terrain type for this tile
     */
    public Tile(int x, int y, Terrain terrain) {
        this.x = x;
        this.y = y;
        this.terrain = terrain;
    }

    /**
     * Returns the terrain type of this tile.
     *
     * @return terrain
     */
    public Terrain getTerrain() {
        return terrain;
    }

    /**
     * Sets the terrain type of this tile.
     *
     * @param terrain new terrain type
     */
    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    /**
     * Returns the base located on this tile, if any.
     *
     * @return base or null
     */
    public BaseStats getBase() {
        return base;
    }

    /**
     * Places a base on this tile.
     *
     * @param base base to place
     */
    public void setBase(BaseStats base) {
        this.base = base;
    }

    /**
     * Returns the unit currently standing on this tile.
     *
     * @return unit or null
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * Places or removes a unit on this tile.
     *
     * @param unit unit to place, or null to clear
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * Returns the building on this tile, if any.
     *
     * @return building or null
     */
    public Building getBuilding() {
        return building;
    }

    /**
     * Places a building on this tile.
     *
     * @param building building to place
     */
    public void setBuilding(Building building) {
        this.building = building;
    }

    /**
     * @return X-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * @return Y-coordinate
     */
    public int getY() {
        return y;
    }
}
