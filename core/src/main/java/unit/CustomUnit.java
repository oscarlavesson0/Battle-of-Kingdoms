package unit;

import GuiMainGame.UnitType;
import base.Player;

/**
 * A unit with custom stats and weapon.
 *
 * @author Enid Becarevic
 */
public class CustomUnit extends Unit {

    /**
     * Creates a new custom unit with the given stats and position.
     *
     * @param maxHP maximum health
     * @param attack attack value
     * @param speed movement speed
     * @param defence defence value
     * @param weapon weapon used by the unit
     * @param startX starting X position
     * @param startY starting Y position
     * @param player owner of the unit
     * @param unitType type of the unit
     */
    public CustomUnit(
        int maxHP,
        int attack,
        int speed,
        int defence,
        Weapon weapon,
        int startX,
        int startY,
        Player player,
        UnitType unitType) {

        super(maxHP, attack, speed, defence, weapon, startX, startY, player, unitType);
    }

    /**
     * Returns a short text description of the unit.
     *
     * @return formatted unit info
     */
    @Override
    public String toString() {
        return String.format(
            "CustomUnit{hp=%d, atk=%d, spd=%d, def=%d, weapon=%s, pos=(%d,%d)}",
            getMaxHp(),
            getAttack(),
            getSpeed(),
            getDefence(),
            getWeapon(),
            getX(),
            getY());
    }
}
