package popup;

/**
 * Listener for actions chosen in the settings popup.
 *
 * @author Emil Hadzic
 */
public interface SettingsActionListener {
    /** Called when the user chooses to resume the game. */
    void onResume();

    /** Called when the user chooses to return to the main menu. */
    void onMainMenu();

    /** Called when the user chooses to quit the game. */
    void onQuit();
}
