package building;

import base.Player;
import terrain.Tile;
import terrain.TileController;

public abstract class Building {

    private boolean isBuilt;
    private int health;
    private int x;
    private int y;

    private Player owner;
    private BuildingType buildingType;

    public Building(int x, int y, Player owner, BuildingType buildingType){
        this.x = x;
        this.y = y;
        this.owner = owner;
        this.buildingType = buildingType;
        this.health = buildingType.getMaxHealth();
        this.isBuilt = true;
    }

    public void updateBuilding(){
        if (health <= 0){
            isBuilt = false;
        }
        if (isBuilt){
            activateBuildingAbility();
        }
    }

    public abstract void activateBuildingAbility();

    public Player getOwner(){
        return owner;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health += health;
        if (this.health >= buildingType.getMaxHealth()){
            this.health = buildingType.getMaxHealth();
        }
        if (this.health <= 0){
            this.health = 0;
            isBuilt = false;
        }

    }
    public boolean isBuilt(){
        return isBuilt;
    }
    public BuildingType getBuildingType() {
        return buildingType;
    }
}
