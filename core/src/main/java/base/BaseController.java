package base;

import GuiMainGame.UnitType;
import unit.*;

/**
 * Handles creation of units from a base, deducting the unit cost from the
 * owning player's gold and spawning the unit near its base.
 *
 * @author Enid Becarevic
 * @author Emil Hadzic
 * @author Stefan Rajkovic
 */
public class BaseController {
    public final int unitCost = 100;
    private UnitController unitController;
    private UnitSpawnListener spawnListener;

    /**
     * Creates a BaseController.
     *
     * @param unitController the controller used to spawn and track units
     */
    public BaseController(UnitController unitController) {
        this.unitController = unitController;
    }

    /**
     * Sets the listener notified when a unit is spawned.
     *
     * @param listener the spawn listener
     */
    public void setSpawnListener(UnitSpawnListener listener) {
        this.spawnListener = listener;
    }

    /**
     * Creates a unit with the given stats if the owner can afford it.
     * The unit cost is deducted from the owner's gold and the unit is
     * spawned near the base.
     *
     * @param base     the base creating the unit
     * @param hp       the unit's max HP
     * @param attack   the unit's attack value
     * @param speed    the unit's speed value
     * @param defence  the unit's defence value
     * @param unitType the type of unit to create
     * @return the created unit, or null if the base/owner is invalid or the owner lacks gold
     */
    public CustomUnit createUnitFromChoice(BaseStats base, int hp, int attack,
                                           int speed, int defence, UnitType unitType) {
        if (base == null) return null;
        Player owner = base.getOwner();
        if (owner == null) return null;
        if (owner.getGold() < unitCost) return null;
        owner.subtractGold(unitCost);

        Weapon weapon = (unitType == UnitType.KNIGHT) ? Weapon.SWORD : Weapon.AXE;

        CustomUnit unit = new UnitSkapare()
            .maxHP(hp)
            .attack(attack)
            .speed(speed)
            .defence(defence)
            .weapon(weapon)
            .unitType(unitType)
            .player(owner)
            .startPosition(base.getPosition().getX(), base.getPosition().getY())
            .build();

        unitController.spawnUnitNearBase(base, unit);
        if (spawnListener != null) spawnListener.onUnitSpawned(unit);
        return unit;
    }

    /**
     * Backward-compatible overload that defaults the unit type to AXEMAN.
     *
     * @param base    the base creating the unit
     * @param hp      the unit's max HP
     * @param attack  the unit's attack value
     * @param speed   the unit's speed value
     * @param defence the unit's defence value
     * @return the created unit, or null if creation fails
     */
    public CustomUnit createUnitFromChoice(BaseStats base, int hp, int attack,
                                           int speed, int defence) {
        return createUnitFromChoice(base, hp, attack, speed, defence, UnitType.AXEMAN);
    }
}
