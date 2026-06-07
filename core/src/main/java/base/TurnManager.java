package base;

/**
 * Manages turns and the per-turn timer. Tracks the current player and turn
 * number, switches players when a turn ends, and notifies a listener of
 * player switches and completed turns.
 *
 * @author Enid Becarevic
 * @author Emil Hadzic
 */
public class TurnManager {

    public static final float turnDuration = 210;
    private float timeRemaining = turnDuration;
    private int turnNumber = 1;
    private Player currentPlayer = Player.PLAYER_ONE;
    private boolean player1HasPlayedThisTurn = false;

    private TurnChangeListener listener;

    //public Player getCurrentPlayer() {
    //return currentPlayer;
    //}

    /**
     * Sets the listener notified on player switches and turn completion.
     *
     * @param listener the turn change listener
     */
    public void setListener(TurnChangeListener listener){
        this.listener = listener;
    }

    /**
     * Ends the current player's turn. Switches to the other player, resets the
     * timer, and increments the turn number once both players have played.
     */
    public void endTurn() {
        if (currentPlayer == Player.PLAYER_ONE) {
            player1HasPlayedThisTurn = true;
            currentPlayer = Player.PLAYER_TWO;
            timeRemaining = turnDuration;
            if(listener != null) listener.onPlayerSwitch(currentPlayer);
        } else {
            turnNumber++;
            currentPlayer = Player.PLAYER_ONE;
            player1HasPlayedThisTurn = false;
            timeRemaining = turnDuration;
            if(listener != null){
                listener.onTurnComplete(turnNumber);
                listener.onPlayerSwitch(currentPlayer);
            }
        }
    }

    /**
     * Advances the timer and ends the turn automatically when time runs out.
     *
     * @param delta the time elapsed since the last update, in seconds
     */
    public void update(float delta){
        timeRemaining -= delta;
        if(timeRemaining <= 0){
            endTurn();
        }
    }

    /** @return the current turn number */
    public int getTurnNumber(){
        return turnNumber;
    }

    /** @return the player whose turn it currently is */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    /** @return the time remaining in the current turn, never below 0 */
    public float getTimeRemaining(){
        return Math.max(0, timeRemaining);
    }

    //public boolean isPlayersTurn(Player player) {
    //return currentPlayer == player;
    //}
}
