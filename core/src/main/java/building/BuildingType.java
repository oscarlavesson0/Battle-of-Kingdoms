package building;

public enum BuildingType {
    Hospital("Hospital", 200, 3, "Heals units"),
    Barracks("Barracks", 400, 3, "Trains units"),
    Blacksmith("Blacksmith", 300, 3, "Powers units"),;

    private String name;
    private int cost;
    private int constructionTime;
    private String description;

    BuildingType(String name, int cost, int constructionTime, String description){
        this.name = name;
        this.cost = cost;
        this.constructionTime = constructionTime;
        this.description = description;
    }
}
