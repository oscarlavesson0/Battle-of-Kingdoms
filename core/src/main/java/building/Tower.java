package building;

import base.Player;
import terrain.TileController;
import unit.Unit;

/**
 * A damaging building that reduces HP of enemy units
 * within its effect range. Uses the affectPoints value
 * from its BuildingType to determine damage amount.
 *
 * Author:
 * JoelAxel Olsson
 */
public class Tower extends RangeBuilding {

    /**
     * Creates a Tower building at the given tile position.
     *
     * @param x tile X position
     * @param y tile Y position
     * @param owner the player who owns the building
     * @param buildingType the building type (Tower)
     * @param tileController controller used to access tiles
     */
    public Tower(int x, int y, Player owner, BuildingType buildingType, TileController tileController) {
        super(x, y, owner, buildingType, tileController);
    }

    /**
     * Applies the Tower's damaging effect to a unit.
     * Only damages units belonging to the opposing player.
     *
     * @param unit the unit to affect
     */
    public void affectUnit(Unit unit) {
        if (unit.getPlayer() != getOwner()) {
            unit.addToCurrentHP(affectPoints);
        }
    }
}
