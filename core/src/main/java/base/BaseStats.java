package base;

import terrain.Tile;

public class BaseStats {
    private Player owner;
    private Tile position;
    private int maxHp = 100;
    private int currentHp = 100;
    private int defense = 10;
    private int goldPerTurn = 50;

    public BaseStats(Player owner, Tile position){
        this.owner = owner;
        this.position = position;
    }

    public Player getOwner() { return owner; }
    public Tile getPosition() { return position; }
    public int getDefense() { return defense; }
    public int getMaxHp() { return maxHp; }
    public int getCurrentHp() { return currentHp; }
    public int getGoldPerTurn(){ return goldPerTurn; }


    public void takeDamage(int rawDamage) {
        int actual = Math.max(1, rawDamage - defense);
        currentHp  = Math.max(0, currentHp - actual);
    }

    public boolean isDestroyed() {
        return currentHp <= 0;
    }
}
