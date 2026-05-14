package io.github.BattleOfKingdoms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import base.Player;

public class WinScreen implements Screen {

    private final Main game;
    private final Player winner;
    private SpriteBatch batch;
    private BitmapFont titleFont;
    private BitmapFont buttonFont;
    private ShapeRenderer shape;

    // Knapp-koordinater
    private float btnX, btnY, btnW, btnH;

    public WinScreen(Main game, Player winner) {
        this.game = game;
        this.winner = winner;
    }

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

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float cx = Gdx.graphics.getWidth()  / 2f;
        float cy = Gdx.graphics.getHeight() / 2f;

        // Rita knapp-bakgrund
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.15f, 0.4f, 0.15f, 1f);
        shape.rect(btnX, btnY, btnW, btnH);
        shape.end();

        batch.begin();

        // Vinnare-text
        titleFont.setColor(Color.GOLD);
        String winLine = winner.getDisplayName() + " vann!";
        titleFont.draw(batch, winLine, cx - 200, cy + 120);

        titleFont.setColor(Color.WHITE);
        titleFont.getData().setScale(1.6f);
        titleFont.draw(batch, "Fiendens bas är förstörd.", cx - 200, cy + 60);

        // Knapp-text
        buttonFont.setColor(Color.WHITE);
        buttonFont.draw(batch, "[ Tillbaka till menyn ]", btnX + 20, btnY + 32);

        batch.end();

        handleInput();
    }

    private void handleInput() {
        if (!Gdx.input.justTouched()) return;
        float tx = Gdx.input.getX();
        float ty = Gdx.graphics.getHeight() - Gdx.input.getY();
        if (tx >= btnX && tx <= btnX + btnW && ty >= btnY && ty <= btnY + btnH) {
            // Återställ guld innan ny match
            game.showStartMenu();
        }
    }

    @Override public void resize(int w, int h) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, w, h);
        shape.getProjectionMatrix().setToOrtho2D(0, 0, w, h);
    }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() {
        batch.dispose();
        shape.dispose();
        titleFont.dispose();
        buttonFont.dispose();
    }
}
