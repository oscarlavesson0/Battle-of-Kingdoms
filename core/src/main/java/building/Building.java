package building;

import base.Player;

public abstract class Building {

    private String name;
    private int maxHealth;
    private int health;
    private int x;
    private int y;

    private Player owner;

    public Building(int x, int y, Player owner){
        this.x = x;
        this.y = y;
        this.owner = owner;
    }

    public abstract void ActivateBuildingAbility();

    public Player getOwner(){
        return owner;
    }


}
