package base;

public class TurnManager {
    private Player currentPlayer = Player.PLAYER_ONE;

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void endTurn() {
        if (currentPlayer == Player.PLAYER_ONE) {
            currentPlayer = Player.PLAYER_TWO;
        } else {
            currentPlayer = Player.PLAYER_ONE;
        }
    }

    public boolean isPlayersTurn(Player player) {
        return currentPlayer == player;
    }
}
