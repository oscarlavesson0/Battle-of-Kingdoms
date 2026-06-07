package building;

import base.Player;
import terrain.TileController;
import unit.Unit;

/**
 * Abstract building type that affects units within a certain range.
 * Handles scanning nearby tiles and applying effects to units found.
 * Subclasses define the specific effect (heal, damage, etc.).
 *
 * Author:
 * JoelAxel Olsson
 */
public abstract class RangeBuilding extends Building {

    /** Range in tiles for the building's effect. */
    int range;

    /** Effect strength (positive = heal, negative = damage). */
    int affectPoints;

    /** Size of the map, used for bounds checking. */
    int mapRange;

    /** Controller used to access tiles and units. */
    TileController tileController;

    /**
     * Creates a range-based building with the given stats.
     *
     * @param x tile X position
     * @param y tile Y position
     * @param owner the player who owns the building
     * @param buildingType the type of building
     * @param tileController controller used to access the tile grid
     */
    public RangeBuilding(int x, int y, Player owner, BuildingType buildingType, TileController tileController) {
        super(x, y, owner, buildingType);
        this.tileController = tileController;
        this.range = buildingType.getRange();
        this.affectPoints = buildingType.getAffectPoints();
        this.mapRange = tileController.getTileGrid().length;
    }

    /**
     * Applies the building's effect to a unit.
     * Implemented by subclasses (Hospital, Tower).
     *
     * @param unit the unit to affect
     */
    public abstract void affectUnit(Unit unit);

    /**
     * Scans all tiles within the building's range and applies
     * the effect to any unit found.
     */
    @Override
    public void activateBuildingAbility() {
        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {

                int x = super.getX() + i;
                int y = super.getY() + j;

                if (x >= 0 && x < mapRange && y >= 0 && y < mapRange) {

                    Unit unit = tileController.getTileGrid()[y][x].getUnit();

                    if (unit != null) {
                        affectUnit(unit);
                    }
                }
            }
        }
    }
}
