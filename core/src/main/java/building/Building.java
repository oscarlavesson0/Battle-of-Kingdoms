package building;

import base.Player;

public abstract class Building {

    private int id;
    private String name;
    private int maxHealth;
    private int constructionTime;
    private int cost;
    private int turnCounter;
    private boolean isBuilt;
    private int health;
    private int x;
    private int y;

    private Player owner;

    public Building(int x, int y, Player owner, int id, int constructionTime){
        this.x = x;
        this.y = y;
        this.owner = owner;
        this.id = id;
        this.constructionTime = constructionTime;
        this.isBuilt = false;
        turnCounter = 0;
    }

    public void updateBuilding(){
        if (turnCounter >= constructionTime){
            isBuilt = true;
        }
        if (isBuilt){
            ActivateBuildingAbility();
        }
        turnCounter++;
    }

    public abstract void ActivateBuildingAbility();

    public Player getOwner(){
        return owner;
    }
    public String getName(){
        return name;
    }
    public int getId(){
        return id;
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
        this.health = health;
    }
    public int getCost(){
        return cost;
    }
    public void setBuilt(boolean built){
        isBuilt = built;
    }
    public boolean isBuilt(){
        return isBuilt;
    }
}
