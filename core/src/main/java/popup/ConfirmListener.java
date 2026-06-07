package popup;

/**
 * Listener for the result of a confirmation popup.
 *
 * @author Emil Hadzic
 */
public interface ConfirmListener {
    /**
     * Called when the user resolves the confirmation.
     *
     * @param confirmed true if the user confirmed, false otherwise
     */
    void onResult(boolean confirmed);
}
