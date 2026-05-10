package io.github.BattleOfKingdoms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class InstructionScreen implements Screen {

    private final Main game;
    private SpriteBatch batch;
    private BitmapFont font;

    public InstructionScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.setColor(Color.BLACK);
        font.getData().setScale(1.5f);

        font.draw(batch, "Instructions:", 100, 500);
        font.draw(batch, "- Klicka på en bas för info", 100, 450);
        font.draw(batch, "- Flytta units genom att klicka", 100, 400);
        font.draw(batch, "- Vinn genom att förstöra fiendens bas", 100, 350);
        font.draw(batch, "[ Back ]", 100, 200);

        batch.end();

        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (x >= 100 && x <= 300 && y >= 200 - 60 && y <= 200) {
                game.showStartMenu();
            }
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
