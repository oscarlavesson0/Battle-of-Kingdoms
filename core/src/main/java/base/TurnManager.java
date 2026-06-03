package base;

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

    public void setListener(TurnChangeListener listener){
        this.listener = listener;
    }

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

    public void update(float delta){
        timeRemaining -= delta;
        if(timeRemaining <= 0){
            endTurn();
        }
    }

    public int getTurnNumber(){
        return turnNumber;
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public float getTimeRemaining(){
        return Math.max(0, timeRemaining);
    }

    //public boolean isPlayersTurn(Player player) {
        //return currentPlayer == player;
    //}
}
