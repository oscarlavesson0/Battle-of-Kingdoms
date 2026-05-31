package building;

public enum BuildingType {
    Hospital("Hospital", 120, 100, 3, 2, 3, "Heals units 2 HP"),
    Barracks("Barracks", 400, 100, 0, 2, 3, "Gives +2 speed to all units"),
    Blacksmith("Blacksmith", 300, 100, 0, 2, 3, "Gives +2 attributes to all units"),
    Tower("Tower", 150, 100, 3, -3, 3, "Damages units 3 HP"),;

    private String name;
    private int cost;
    private int maxHealth;
    private int range;
    private int affectPoints;
    private int constructionTime;
    private String description;

    BuildingType(String name, int cost, int maxHealth, int range, int affectPoints, int constructionTime, String description){
        this.name = name;
        this.cost = cost;
        this.maxHealth = maxHealth;
        this.range = range;
        this.affectPoints = affectPoints;
        this.constructionTime = constructionTime;
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
    public int getConstructionTime() {
        return constructionTime;
    }
    public String getDescription() {
        return description;
    }
}
