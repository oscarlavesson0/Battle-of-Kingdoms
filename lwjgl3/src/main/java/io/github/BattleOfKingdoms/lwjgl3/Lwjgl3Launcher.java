package io.github.BattleOfKingdoms.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.BattleOfKingdoms.Test;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("LibGDX Test");
        config.setWindowedMode(800, 600);
        new Lwjgl3Application(new Test(), config);
        System.out.println("hej");
    }
}
