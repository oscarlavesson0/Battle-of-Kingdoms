package base;

/**
 * A gold mine that can be occupied by a player to generate extra gold each turn.
 * <p>
 * Note: planned feature. The class is complete but was not fully integrated
 * into the income system due to time constraints.
 *
 * @author Emil Hadzic
 */
public class GoldMine {

    private Player owner;
    private final int goldPerTurn = 100;

    /**
     * Creates an unoccupied gold mine.
     */
    public GoldMine(){
        this.owner = null;
    }

    /** @return true if the mine is currently occupied by a player */
    public boolean isOccupied(){
        return owner != null;
    }

    /** @return the player occupying the mine, or null if unoccupied */
    public Player getOwner(){
        return owner;
    }

    /**
     * Occupies the mine for the given player.
     *
     * @param player the player taking the mine
     */
    public void occupy(Player player){
        this.owner = player;
    }

    /** Clears ownership, leaving the mine unoccupied. */
    public void clear(){
        this.owner = null;
    }

    /** @return the gold generated per turn while occupied */
    public int getGoldPerTurn(){
        return goldPerTurn;
    }
}
