package base;

import terrain.Tile;

/**
 * Holds the state of a player's base: owner, position, HP and defence.
 * Provides damage handling and destruction checks.
 *
 * @author Enid Becarevic
 * @author Emil Hadzic
 * @author Oscar Lavesson
 * @author JoelAxel Olsson
 */
public class BaseStats {
    private Player owner;
    private Tile position;
    private int maxHp = 5;
    private int currentHp = 5;
    private int defense = 10;
    private int goldPerTurn = 50;

    /**
     * Creates a base owned by the given player at the given tile.
     *
     * @param owner    the player owning the base
     * @param position the tile where the base is located
     */
    public BaseStats(Player owner, Tile position){
        this.owner = owner;
        this.position = position;
        System.out.println(position.getX() + " bbbb " + position.getY());
    }

    /** @return the player owning this base */
    public Player getOwner() { return owner; }

    /** @return the tile where this base is located */
    public Tile getPosition() { return position; }

    /** @return the base's defence value */
    public int getDefense() { return defense; }

    /** @return the base's maximum HP */
    public int getMaxHp() { return maxHp; }

    /** @return the base's current HP */
    public int getCurrentHp() { return currentHp; }

    /** @return the gold this base generates per turn */
    public int getGoldPerTurn(){ return goldPerTurn; }

    /**
     * Applies damage to the base after subtracting defence.
     * At least 1 damage is always taken, and HP never drops below 0.
     *
     * @param rawDamage the incoming damage before defence is applied
     */
    public void takeDamage(int rawDamage) {
        int actual = Math.max(1, rawDamage - defense);
        currentHp  = Math.max(0, currentHp - actual);
    }

    /** @return true if the base has been destroyed (HP at or below 0) */
    public boolean isDestroyed() {
        return currentHp <= 0;
    }

    /**
     * Updates the base's tile position.
     *
     * @param position the new tile
     */
    public void setPosition(Tile position) {
        this.position = position;
        System.out.println(position.getX() + " setposition " + position.getY());
    }
}
