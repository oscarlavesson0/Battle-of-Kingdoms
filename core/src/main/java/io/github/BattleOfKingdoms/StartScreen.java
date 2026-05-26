package io.github.BattleOfKingdoms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class StartScreen implements Screen {

    private final Main game;
    private SpriteBatch batch;
    private BitmapFont font;

    // Knapp‑koordinater
    private float startX, startY, startW, startH;
    private float instrX, instrY, instrW, instrH;
    private float exitX, exitY, exitW, exitH;

    public StartScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            Gdx.files.internal("lwjgl3/assets/ui/font/PixelWarden.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        parameter.color = Color.BLACK;

        font = generator.generateFont(parameter);
        generator.dispose();

        startW = instrW = exitW = 300;
        startH = instrH = exitH = 60;

        startX = instrX = exitX = (Gdx.graphics.getWidth() - startW) / 2f;

        startY = 400;
        instrY = 300;
        exitY = 200;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.setColor(Color.BLACK);
        font.getData().setScale(2);

        font.draw(batch, "Battle of Kingdoms", 200, 550);

        font.draw(batch, "Start Game", startX, startY);
        font.draw(batch, "Instructions", instrX, instrY);
        font.draw(batch, "Exit", exitX, exitY);

        batch.end();

        handleInput();
    }

    private void handleInput() {
        if (!Gdx.input.justTouched()) return;

        float x = Gdx.input.getX();
        float y = Gdx.graphics.getHeight() - Gdx.input.getY();

        // Start
        if (x >= startX && x <= startX + startW &&
            y >= startY - startH && y <= startY) {
            game.startGame();
        }

        // Instructions
        if (x >= instrX && x <= instrX + instrW &&
            y >= instrY - instrH && y <= instrY) {
            game.showInstructions();
        }

        // Exit
        if (x >= exitX && x <= exitX + exitW &&
            y >= exitY - exitH && y <= exitY) {
            Gdx.app.exit();
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
