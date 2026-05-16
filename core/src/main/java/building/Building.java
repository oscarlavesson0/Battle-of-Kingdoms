package building;

import base.Player;

public abstract class Building {

    private int id;
    private String name;
    private int maxHealth;
    private int constructionTime;
    private int cost;
    private boolean isBuilt;
    private int health;
    private int x;
    private int y;

    private Player owner;

    public Building(int x, int y, Player owner, int id){
        this.x = x;
        this.y = y;
        this.owner = owner;
        this.id = id;
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
