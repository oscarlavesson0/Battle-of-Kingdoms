package base;


/**
 * Listener notified when a base is destroyed, identifying the winner.
 *
 * @author Enid Becarevic
 */
public interface BaseDestroyedListener {
    /**
     * Called when a base is destroyed.
     *
     * @param destroyedBase the base that was destroyed
     * @param winner        the player who won as a result
     */
    void onBaseDestroyed(BaseStats destroyedBase, Player winner);
}
