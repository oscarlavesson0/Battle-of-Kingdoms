package base;

import terrain.Tile;

public class BaseStats {
    private Player owner;
    private Tile position;
    private int maxHp = 5;
    private int currentHp = 5;
    private int defense = 10;
    private int goldPerTurn = 50;

    public BaseStats(Player owner, Tile position){
        this.owner = owner;
        this.position = position;
        System.out.println(position.getX() + " bbbb " + position.getY());
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

    public void setPosition(Tile position) {
        this.position = position;
        System.out.println(position.getX() + " setposition " + position.getY());
    }
}
