package building;

import base.Player;
import terrain.TileController;
import unit.Unit;

/**
 * A healing building that restores HP to friendly units
 * within its effect range. Uses the affectPoints value
 * from its BuildingType to determine healing strength.
 *
 * Author:
 * JoelAxel Olsson
 */
public class Hospital extends RangeBuilding {

    /**
     * Creates a Hospital building at the given tile position.
     *
     * @param x tile X position
     * @param y tile Y position
     * @param owner the player who owns the building
     * @param buildingType the building type (Hospital)
     * @param tileController controller used to access tiles
     */
    public Hospital(int x, int y, Player owner, BuildingType buildingType, TileController tileController) {
        super(x, y, owner, buildingType, tileController);
    }

    /**
     * Applies the Hospital's healing effect to a unit.
     * Only heals units belonging to the same player.
     *
     * @param unit the unit to affect
     */
    public void affectUnit(Unit unit) {
        if (unit.getPlayer() == getOwner()) {
            unit.addToCurrentHP(affectPoints);
        }
    }
}
