package unit;

import GuiMainGame.UnitType;
import base.Player;

/**
 * Represents a game unit with stats, position and equipment.
 *
 * @author Enid Becarevic
 * @author Emil Hadzic
 * @author Oscar Lavesson
 * @author JoelAxel Olsson
 */
public class Unit {

    private int maxHP;
    private int currentHP;
    private int attack;
    private int speed;
    private int defence;
    private int x;
    private int y;
    private Weapon weapon;
    private Player player;
    private UnitType unitType;
    private boolean alive = true;

    /**
     * Creates a new unit with the given stats and starting position.
     *
     * @param maxHp    maximum health
     * @param attack   attack value
     * @param speed    movement speed
     * @param defence  defence value
     * @param weapon   weapon used by the unit
     * @param startX   starting X position
     * @param startY   starting Y position
     * @param player   owner of the unit
     * @param unitType type of the unit
     */
    public Unit(int maxHp, int attack, int speed, int defence, Weapon weapon, int startX, int startY, Player player, UnitType unitType) {
        this.maxHP = maxHp;
        this.currentHP = maxHp;
        this.attack = attack;
        this.speed = speed;
        this.defence = defence;
        this.weapon = weapon;
        this.x = startX;
        this.y = startY;
        this.player = player;
        this.unitType = unitType;
    }

    public int getMaxHp() {
        return maxHP;
    }

    public int getCurrentHp() {
        return currentHP;
    }

    public int getAttack() {
        return attack;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDefence() {
        return defence;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isAlive() {
        return alive;
    }

    /**
     * Applies damage to the unit. Damage is reduced by defence but at least 1.
     *
     * @param damage incoming damage
     */
    public void takeDamage(int damage) {
        int actualDamage = Math.max(1, damage - defence);
        currentHP -= actualDamage;
        if (currentHP <= 0) {
            currentHP = 0;
            alive = false;
        }
    }

    /**
     * Adds or subtracts health points. Clamps between 0 and maxHP.
     *
     * @param healthPoints positive to heal, negative to damage
     */
    public void addToCurrentHP(int healthPoints) {
        if (currentHP + healthPoints >= maxHP) {
            currentHP = maxHP;
        } else if (currentHP + healthPoints <= 0) {
            currentHP = 0;
            alive = false;
        } else {
            currentHP += healthPoints;
        }
    }


    /**
     * Sets the unit position without movement checks.
     *
     * @param newX new x-coordinate
     * @param newY new y-coordinate
     */
    public void setPosition(int newX, int newY) {
        x = newX;
        y = newY;
    }

    public UnitType getUnitType() {
        return unitType;
    }
}
