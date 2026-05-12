package base;

public enum Player{
    PLAYER_ONE("Player 1"),
    PLAYER_TWO("Player 2");

    private final String displayName;

    Player(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
