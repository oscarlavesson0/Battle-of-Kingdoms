package unit;

import GuiMainGame.UnitType;
import base.Player;

/**
 * Builder for creating custom units with validated stats and settings.
 *
 * @author Enid Becarevic
 * @author Oscar Lavesson
 */
public class UnitSkapare {

    private int maxHP   = 10;
    private int attack  = 0;
    private int speed   = 0;
    private int defence = 0;
    private Weapon weapon = Weapon.AXE;
    private int startX  = 0;
    private int startY  = 0;
    private UnitType unitType = UnitType.AXEMAN;

    private static final int MIN_STAT    = 1;
    private static final int MAX_HP      = 25;
    private static final int MAX_ATTACK  = 5;
    private static final int MAX_SPEED   = 5;
    private static final int MAX_DEFENCE = 5;

    private Player player;

    /**
     * Sets the maximum HP for the unit.
     *
     * @param maxHP desired HP value
     * @return this builder
     */
    public UnitSkapare maxHP(int maxHP) {
        this.maxHP = clamp(maxHP, MIN_STAT, MAX_HP);
        return this;
    }

    /**
     * Sets the attack value for the unit.
     *
     * @param attack desired attack value
     * @return this builder
     */
    public UnitSkapare attack(int attack) {
        this.attack = clamp(attack, MIN_STAT, MAX_ATTACK);
        return this;
    }

    /**
     * Sets the movement speed for the unit.
     *
     * @param speed desired speed value
     * @return this builder
     */
    public UnitSkapare speed(int speed) {
        this.speed = clamp(speed, MIN_STAT, MAX_SPEED);
        return this;
    }

    /**
     * Sets the defence value for the unit.
     *
     * @param defence desired defence value
     * @return this builder
     */
    public UnitSkapare defence(int defence) {
        this.defence = clamp(defence, MIN_STAT, MAX_DEFENCE);
        return this;
    }

    /**
     * Sets the weapon for the unit.
     *
     * @param weapon weapon type
     * @return this builder
     * @throws IllegalArgumentException if weapon is null
     */
    public UnitSkapare weapon(Weapon weapon) {
        if (weapon == null) {
            throw new IllegalArgumentException("Vapen kan inte vara null.");
        }
        this.weapon = weapon;
        return this;
    }

    /**
     * Sets the starting position for the unit.
     *
     * @param x start X coordinate
     * @param y start Y coordinate
     * @return this builder
     */
    public UnitSkapare startPosition(int x, int y) {
        this.startX = x;
        this.startY = y;
        return this;
    }

    /**
     * Sets the owning player for the unit.
     *
     * @param player the player
     * @return this builder
     * @throws IllegalArgumentException if player is null
     */
    public UnitSkapare player(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player kan inte vara null.");
        }
        this.player = player;
        return this;
    }

    /**
     * Sets the unit type (visual/logic type).
     *
     * @param type the unit type
     * @return this builder
     */
    public UnitSkapare unitType(UnitType type) {
        if (type != null) {
            this.unitType = type;
        }
        return this;
    }

    /**
     * Builds and returns a fully validated {@link CustomUnit}.
     *
     * @return a new CustomUnit instance
     * @throws IllegalStateException if required fields are missing or invalid
     */
    public CustomUnit build() {
        validate();
        return new CustomUnit(maxHP, attack, speed, defence, weapon, startX, startY, player, unitType);
    }

    /**
     * Validates required fields before building.
     *
     * @throws IllegalStateException if any required value is invalid
     */
    private void validate() {
        if (maxHP < MIN_STAT) {
            throw new IllegalStateException("maxHP måste vara minst " + MIN_STAT);
        }
        if (speed < MIN_STAT) {
            throw new IllegalStateException("speed måste vara minst " + MIN_STAT);
        }
        if (player == null) {
            throw new IllegalStateException("Player måste anges innan build().");
        }
    }

    /**
     * Clamps a value between a minimum and maximum.
     *
     * @param value the value to clamp
     * @param min   minimum allowed
     * @param max   maximum allowed
     * @return clamped value
     */
    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    /** @return maximum allowed HP */
    public static int getMaxHP() {
        return MAX_HP;
    }

    /** @return maximum allowed attack */
    public static int getMaxAttack() {
        return MAX_ATTACK;
    }

    /** @return maximum allowed speed */
    public static int getMaxSpeed() {
        return MAX_SPEED;
    }

    /** @return maximum allowed defence */
    public static int getMaxDefence() {
        return MAX_DEFENCE;
    }

    /** @return minimum allowed stat value */
    public static int getMinStat() {
        return MIN_STAT;
    }
}
