package building;

/**
 * Enum representing all available building types in the game.
 * Each type defines its name, cost, max health, range,
 * effect strength and a description of its ability.
 *
 * Author:
 * JoelAxel Olsson
 */
public enum BuildingType {

    /** Heals nearby units. */
    Hospital("Hospital", 120, 10, 3, 2, "Heals units 2 HP"),

    /** Damages nearby enemy units. */
    Tower("Tower", 200, 10, 3, -2, "Damages units 3 HP");

    /** Display name of the building. */
    private String name;

    /** Gold cost required to construct the building. */
    private int cost;

    /** Maximum health of the building. */
    private int maxHealth;

    /** Range in tiles for the building's effect. */
    private int range;

    /** Effect strength (positive = heal, negative = damage). */
    private int affectPoints;

    /** Description of the building's ability. */
    private String description;

    /**
     * Creates a building type with defined stats and behavior.
     *
     * @param name display name
     * @param cost gold cost to build
     * @param maxHealth maximum health
     * @param range effect range in tiles
     * @param affectPoints healing or damage amount
     * @param description description of the ability
     */
    BuildingType(String name, int cost, int maxHealth, int range, int affectPoints, String description) {
        this.name = name;
        this.cost = cost;
        this.maxHealth = maxHealth;
        this.range = range;
        this.affectPoints = affectPoints;
        this.description = description;
    }

    /** @return the display name of the building */
    public String getName() {
        return name;
    }

    /** @return the gold cost of the building */
    public int getCost() {
        return cost;
    }

    /** @return the maximum health of the building */
    public int getMaxHealth() {
        return maxHealth;
    }

    /** @return the effect range in tiles */
    public int getRange() {
        return range;
    }

    /** @return the effect strength (heal or damage) */
    public int getAffectPoints() {
        return affectPoints;
    }

    /** @return description of the building's ability */
    public String getDescription() {
        return description;
    }
}
