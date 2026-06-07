package base;

/**
 * Represents a player in the game, holding a display name and a gold balance.
 *
 * @author Emil Hadzic
 * @author Stefan Rajkovic
 */
public enum Player{
    PLAYER_ONE("Player 1"),
    PLAYER_TWO("Player 2");

    private final String displayName;
    private int gold;

    /**
     * Creates a player with the given display name and a starting gold of 100.
     *
     * @param displayName the player's display name
     */
    Player(String displayName){
        this.displayName = displayName;
        this.gold = 100;
    }

    /** @return the player's display name */
    public String getDisplayName() {
        return displayName;
    }

    /** @return the player's current gold balance */
    public int getGold() {
        return gold;
    }

    /**
     * Adds gold to the player's balance.
     *
     * @param amount the amount to add
     */
    public void addGold(int amount){
        this.gold += amount;
    }

    /**
     * Subtracts gold from the player's balance.
     *
     * @param amount the amount to subtract
     */
    public void subtractGold(int amount){this.gold -= amount;}
}
