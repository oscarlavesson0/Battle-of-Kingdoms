package io.github.BattleOfKingdoms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import base.Player;

/**
 * Screen shown when a player wins the game.
 * Displays the winner, a message, and a button to return to the main menu.
 *
 * Author:
 * Enid Becarevic
 */
public class WinScreen implements Screen {

    /** Reference to the main game for switching screens. */
    private final Main game;

    /** The player who won the match. */
    private final Player winner;

    /** Rendering helpers. */
    private SpriteBatch batch;
    private BitmapFont titleFont;
    private BitmapFont buttonFont;
    private ShapeRenderer shape;

    /** Button position and size. */
    private float btnX, btnY, btnW, btnH;

    /**
     * Creates a WinScreen for the given winner.
     *
     * @param game the main game instance
     * @param winner the player who won the game
     */
    public WinScreen(Main game, Player winner) {
        this.game = game;
        this.winner = winner;
    }

    /**
     * Initializes fonts, shapes, and button layout.
     */
    @Override
    public void show() {
        batch   = new SpriteBatch();
        shape   = new ShapeRenderer();

        titleFont  = new BitmapFont();
        titleFont.getData().setScale(3f);

        buttonFont = new BitmapFont();
        buttonFont.getData().setScale(1.6f);

        btnW = 260f;
        btnH = 50f;
        btnX = (Gdx.graphics.getWidth()  - btnW) / 2f;
        btnY = (Gdx.graphics.getHeight() / 2f) - 80f;
    }

    /**
     * Renders the win message, button, and background color.
     *
     * @param delta time since last frame
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float cx = Gdx.graphics.getWidth()  / 2f;
        float cy = Gdx.graphics.getHeight() / 2f;

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.15f, 0.4f, 0.15f, 1f);
        shape.rect(btnX, btnY, btnW, btnH);
        shape.end();

        batch.begin();

        titleFont.setColor(Color.GOLD);
        String winLine = winner.getDisplayName() + " vann!";
        titleFont.draw(batch, winLine, cx - 200, cy + 120);

        titleFont.setColor(Color.WHITE);
        titleFont.getData().setScale(1.6f);
        titleFont.draw(batch, "Fiendens bas är förstörd.", cx - 200, cy + 60);

        buttonFont.setColor(Color.WHITE);
        buttonFont.draw(batch, "[ Tillbaka till menyn ]", btnX + 20, btnY + 32);

        batch.end();

        handleInput();
    }

    /**
     * Handles button click to return to the main menu.
     */
    private void handleInput() {
        if (!Gdx.input.justTouched()) return;

        float tx = Gdx.input.getX();
        float ty = Gdx.graphics.getHeight() - Gdx.input.getY();

        boolean inside =
            tx >= btnX && tx <= btnX + btnW &&
                ty >= btnY && ty <= btnY + btnH;

        if (inside) {
            game.showStartMenu();
        }
    }

    /**
     * Updates projection matrices when the window is resized.
     */
    @Override
    public void resize(int w, int h) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, w, h);
        shape.getProjectionMatrix().setToOrtho2D(0, 0, w, h);
    }

    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    /**
     * Disposes all allocated resources.
     */
    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
        titleFont.dispose();
        buttonFont.dispose();
    }
}
