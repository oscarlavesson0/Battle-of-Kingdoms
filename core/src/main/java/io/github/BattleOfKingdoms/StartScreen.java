package io.github.BattleOfKingdoms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class StartScreen implements Screen {

    private final Main game;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture backgrund;
    private OrthographicCamera camera;

    // Knapp‑koordinater
    private float startX, startY, startW, startH;
    private float instrX, instrY, instrW, instrH;
    private float exitX, exitY, exitW, exitH;

    private GlyphLayout startLayout, instrLayout, exitLayout;

    private String title = "Battle of Kingdoms";
    private float titleX, titleY;
    private GlyphLayout titleLayout;

    private float inputCooldown = 0.2f;

    public StartScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            Gdx.files.internal("lwjgl3/assets/ui/font/PixelWarden.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        generator.dispose();

        font.getData().setScale(2);

        backgrund = new Texture("lwjgl3/assets/ui/backgrounds/Startmeny.png");

        startLayout = new GlyphLayout();
        instrLayout = new GlyphLayout();
        exitLayout = new GlyphLayout();

        startLayout.setText(font, "  Start Game");
        instrLayout.setText(font, "  Instructions");
        exitLayout.setText(font, "  Exit");

        startW = startLayout.width + 40;
        instrW = instrLayout.width + 40;
        exitW = exitLayout.width + 40;

        startH = instrH = exitH = font.getCapHeight() * 1.2f;

        startY = 400;
        instrY = 300;
        exitY = 200;

        titleLayout = new GlyphLayout();
        font.getData().setScale(2);
        titleLayout.setText(font, title);
        float titleWidth = titleLayout.width;

        titleX = (Gdx.graphics.getWidth() - titleWidth) / 2f;
        titleY = Gdx.graphics.getHeight() * 0.85f;
    }

    @Override
    public void render(float delta) {

        if (inputCooldown > 0) {
            inputCooldown -= delta;
            return;
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(backgrund, 0, 0, camera.viewportWidth, camera.viewportHeight);

        font.setColor(Color.BLACK);

        font.draw(batch, title, titleX, titleY);

        font.draw(batch, "  Start Game", startX, startY);
        font.draw(batch, "  Instructions", instrX, instrY);
        font.draw(batch, "  Exit", exitX, exitY);

        batch.end();

        handleInput();
    }

    @Override
    public void resize(int width, int height){
        camera.setToOrtho(false, width, height);

        startX = (width - startW) / 2f;
        instrX = (width - instrW) / 2f;
        exitX = (width - exitW) / 2f;

        startY = height * 0.45f;
        instrY = height * 0.30f;
        exitY = height * 0.15f;

        titleLayout.setText(font, title);
        float titleWidth = titleLayout.width;
        titleX = (width - titleWidth) / 2f;
        titleY = camera.viewportHeight * 0.80f;
    }

    private void handleInput() {
        if (!Gdx.input.justTouched()) return;

        Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touch);

        float x = touch.x;
        float y = touch.y;

        float startCenterY = startY - startH * 0.45f;
        float instrCenterY = instrY - instrH * 0.45f;
        float exitCenterY  = exitY  - exitH  * 0.45f;

        if (x >= startX && x <= startX + startW &&
            y >= startCenterY - startH/2 && y <= startCenterY + startH/2) {
            game.startGame();
        }

        if (x >= instrX && x <= instrX + instrW &&
            y >= instrCenterY - instrH/2 && y <= instrCenterY + instrH/2) {
            game.showInstructions();
        }

        if (x >= exitX && x <= exitX + exitW &&
            y >= exitCenterY - exitH/2 && y <= exitCenterY + exitH/2) {
            Gdx.app.exit();
        }
    }

    @Override public void show() {
        inputCooldown = 0.2f;
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        batch.dispose();
        font.dispose();
        backgrund.dispose();
    }
}
