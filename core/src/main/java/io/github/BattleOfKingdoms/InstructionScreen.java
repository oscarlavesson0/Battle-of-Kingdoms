package io.github.BattleOfKingdoms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class InstructionScreen implements Screen {

    private final Main game;
    private Stage stage;
    private OrthographicCamera camera;

    private Texture background;

    private int page = 0;

    private final String[] pages = {
        "Player 1 controls the base on the left.\n" +
            "Player 2 controls the base on the right.\n\n" +
            "The goal is to destroy the enemy base.",

        "You attack using your units.\n\n" +
            "To move a unit:\n" +
            "1. Click your unit\n" +
            "2. Press 'Move'\n" +
            "3. Select a tile to move to\n\n" +
            "When standing next to an enemy unit or enemy base,\n" +
            "you can press 'Attack'.",

        "Each unit can move once per turn.\n\n" +
            "When you are done with your actions,\n" +
            "press 'End Turn'.\n\n" +
            "There is also a timer.\n" +
            "If it reaches 0, your turn ends automatically.",

        "You gain 50 gold each round.\n\n" +
            "To create a new unit:\n" +
            "1. Click your base\n" +
            "2. Press 'Create Unit'\n\n" +
            "You receive 7 skill points to upgrade your unit.",

        "Good luck, commander.\n" +
            "Crush the enemy and win the battle."
    };

    private Label textLabel;
    private TextButton backButton;
    private TextButton nextButton;
    private TextButton closeButton;

    private Table rootTable;

    public InstructionScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        background = new Texture("lwjgl3/assets/ui/backgrounds/Startmeny.png");

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            Gdx.files.internal("lwjgl3/assets/ui/font/PixelWarden.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        parameter.color = Color.BLACK;

        BitmapFont sharpFont = generator.generateFont(parameter);
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = sharpFont;

        textLabel = new Label(pages[page], labelStyle);
        textLabel.setAlignment(Align.center);
        textLabel.setWrap(true);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        backButton = new TextButton("Back", skin);
        nextButton = new TextButton("Next", skin);
        closeButton = new TextButton("Close", skin);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (page > 0) {
                    page--;
                    updatePage();
                }
            }
        });

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (page < pages.length - 1) {
                    page++;
                    updatePage();
                }
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.showStartMenu();
            }
        });

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.top().padTop(80);

        float textWidth = Gdx.graphics.getWidth() * 0.8f;

        rootTable.add(textLabel).width(textWidth).padBottom(80).center();
        rootTable.row();

        Table buttonRow = new Table();
        buttonRow.center();

        buttonRow.add(backButton).padRight(40);
        buttonRow.add(nextButton).padRight(40);
        buttonRow.add(closeButton);

        rootTable.add(buttonRow).center();

        stage.addActor(rootTable);

        updatePage();
    }

    private void updatePage() {
        textLabel.setText(pages[page]);

        backButton.setVisible(page > 0);
        nextButton.setVisible(page < pages.length - 1);
        closeButton.setVisible(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        stage.getBatch().setProjectionMatrix(camera.combined);

        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        float textWidth = width * 0.8f;
        textLabel.setWidth(textWidth);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
    }
}
