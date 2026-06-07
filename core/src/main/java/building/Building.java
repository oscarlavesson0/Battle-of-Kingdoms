package building;

import base.Player;

/**
 * Abstract base class for all buildings in the game.
 * Stores owner, position, health, type and handles updating
 * and activation of building-specific abilities.
 *
 * Author:
 * JoelAxel Olsson
 */
public abstract class Building {

    /** Whether the building is still active (health > 0). */
    private boolean isBuilt;

    /** Current health of the building. */
    private int health;

    /** Tile X position of the building. */
    private int x;

    /** Tile Y position of the building. */
    private int y;

    /** The player who owns the building. */
    private Player owner;

    /** The type of building (defines stats and abilities). */
    private BuildingType buildingType;

    /**
     * Creates a new building at the given tile position.
     *
     * @param x tile X position
     * @param y tile Y position
     * @param owner the player who owns the building
     * @param buildingType the type of building
     */
    public Building(int x, int y, Player owner, BuildingType buildingType) {
        this.x = x;
        this.y = y;
        this.owner = owner;
        this.buildingType = buildingType;
        this.health = buildingType.getMaxHealth();
        this.isBuilt = true;
    }

    /**
     * Updates the building each turn.
     * If health reaches zero, the building becomes inactive.
     * If still active, its ability is triggered.
     */
    public void updateBuilding() {
        if (health <= 0) {
            isBuilt = false;
        }
        if (isBuilt) {
            activateBuildingAbility();
        }
    }

    /**
     * Activates the building's special ability.
     * Implemented by subclasses.
     */
    public abstract void activateBuildingAbility();

    /** @return the owner of the building */
    public Player getOwner() {
        return owner;
    }

    /** @return tile X position */
    public int getX() {
        return x;
    }

    /** @return tile Y position */
    public int getY() {
        return y;
    }

    /** @return current health */
    public int getHealth() {
        return health;
    }

    /**
     * Modifies the building's health by the given amount.
     * Ensures health stays within valid bounds.
     *
     * @param health amount to add (negative = damage)
     */
    public void setHealth(int health) {
        this.health += health;

        if (this.health >= buildingType.getMaxHealth()) {
            this.health = buildingType.getMaxHealth();
        }
        if (this.health <= 0) {
            this.health = 0;
            isBuilt = false;
        }
    }

    /** @return true if the building is still active */
    public boolean isBuilt() {
        return isBuilt;
    }

    /** @return the building type */
    public BuildingType getBuildingType() {
        return buildingType;
    }
}
