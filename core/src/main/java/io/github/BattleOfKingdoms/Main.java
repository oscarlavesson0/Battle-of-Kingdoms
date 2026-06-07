package io.github.BattleOfKingdoms;

import base.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

/**
 * Main entry point for Battle of Kingdoms.
 * Handles switching between all game screens such as
 * Start Menu, Instructions, Gameplay, and Win Screen.
 *
 * Authors:
 * Stefan Rajkovic
 * Enid Becarevic
 * Oscar Lavesson
 * JoelAxel Olsson
 */
public class Main extends Game {

    /** The currently active screen. */
    private Screen currentScreen;

    /**
     * Called when the game starts.
     * Loads the start menu as the first screen.
     */
    @Override
    public void create() {
        showStartMenu();
    }

    /**
     * Switches to the Start Menu screen.
     */
    public void showStartMenu() {
        currentScreen = new StartScreen(this);
        setScreen(currentScreen);
    }

    /**
     * Switches to the Instructions screen.
     */
    public void showInstructions() {
        currentScreen = new InstructionScreen(this);
        setScreen(currentScreen);
    }

    /**
     * Starts a new game by switching to the GameScreen.
     */
    public void startGame() {
        currentScreen = new GameScreen(this);
        setScreen(currentScreen);
    }

    /**
     * Switches to the Win Screen after a player wins.
     *
     * @param winner the player who won the game
     */
    public void showWinScreen(Player winner) {
        currentScreen = new WinScreen(this, winner);
        setScreen(currentScreen);
    }
}
