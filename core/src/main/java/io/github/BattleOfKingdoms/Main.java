package io.github.BattleOfKingdoms;

import base.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class Main extends Game {

    private Screen currentScreen;

    @Override
    public void create() {
        showStartMenu();
    }

    public void showStartMenu() {
        currentScreen = new StartScreen(this);
        setScreen(currentScreen);
    }

    public void showInstructions() {
        currentScreen = new InstructionScreen(this);
        setScreen(currentScreen);
    }

    public void startGame() {
        currentScreen = new GameScreen(this);
        setScreen(currentScreen);
    }

    public void showWinScreen(Player winner) {
        currentScreen = new WinScreen(this, winner);
        setScreen(currentScreen);
    }
}
