package building;

public enum BuildingType {
    Hospital("Hospital", 120, 10, 3, 2, "Heals units 2 HP"),
    Tower("Tower", 200, 10, 3, -2, "Damages units 3 HP"),;

    private String name;
    private int cost;
    private int maxHealth;
    private int range;
    private int affectPoints;
    private String description;

    BuildingType(String name, int cost, int maxHealth, int range, int affectPoints, String description){
        this.name = name;
        this.cost = cost;
        this.maxHealth = maxHealth;
        this.range = range;
        this.affectPoints = affectPoints;
        this.description = description;
    }

    public String getName() {
        return name;
    }
    public int getCost() {
        return cost;
    }
    public int getMaxHealth() { return maxHealth; }
    public int getRange() { return range; }
    public int getAffectPoints() { return affectPoints; }
    public String getDescription() {
        return description;
    }
}
