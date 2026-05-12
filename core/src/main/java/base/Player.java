package base;

public enum Player{
    PLAYER_ONE("Player 1"),
    PLAYER_TWO("Player 2");

    private final String displayName;
    private int gold;

    Player(String displayName){
        this.displayName = displayName;
        this.gold = 0;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int amount){
        this.gold += amount;
    }
}
