package base;

/**
 * Listener for turn events, notified when the active player changes and when
 * a full turn (both players) completes.
 *
 * @author Emil Hadzic
 */
public interface TurnChangeListener {

    /**
     * Called when the active player switches.
     *
     * @param newPlayer the player whose turn it now is
     */
    public void onPlayerSwitch(Player newPlayer);

    /**
     * Called when a full turn completes.
     *
     * @param newTurnNumber the number of the new turn
     */
    public void onTurnComplete(int newTurnNumber);
}
