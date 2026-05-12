package io.github.BattleOfKingdoms;

import GuiMainGame.*;
import base.BaseController;
import base.TurnManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import terrain.TileController;
import popup.BasePopup;
import unit.CustomUnit;
import unit.Weapon;
import unit.UnitController;
import com.badlogic.gdx.audio.Music;
import base.BaseStats;
import base.Player;
import popup.TrainUnitListener;

import java.util.Random;

public class GameScreen implements Screen {

    private final Main game;

    private Music         bgMusic;
    private SpriteBatch   batch;
    private ShapeRenderer shapeRenderer;
    private SpriteSheetLoader sheet;
    private TextureRegion grass;

    private TileController  tileController;
    private WorldMap        world;
    private Base            baseGraphic;
    private Lake            lakeGraphic;
    private BasePopup       basePopup;
    private BitmapFont      font;

    // Player 1
    private CharacterRenderer character;
    private UnitView          unitView;
    private CustomUnit        unit;

    // Player 2
    private CharacterRenderer character2;
    private UnitView          unitView2;
    private CustomUnit        unit2;

    private UnitController  unitController;
    private HighlightSystem highlightSystem;
    private TurnManager     turnManager;

    /** Stored so GameScreen can call getActionMenu() for rendering. */
    private GameInput gameInput;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal(
            "lwjgl3/assets/Audio/Medieval Fantasy Tavern D&D Fantasy Music and Ambience - Daydreaming of Persephone (128k).mp3"));
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.05f);
        bgMusic.play();

        batch         = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        sheet         = new SpriteSheetLoader();
        font          = new BitmapFont();

        tileController = new TileController();
        world          = new WorldMap(tileController);
        grass          = sheet.getTile(0, 5);

        // Bases
        BaseStats base1 = new BaseStats(Player.PLAYER_ONE,
            tileController.getTileGrid()[4][4]);
        BaseStats base2 = new BaseStats(Player.PLAYER_TWO,
            tileController.getTileGrid()[43][35]);

        baseGraphic = new Base(sheet, base1);
        lakeGraphic = new Lake(sheet);

        world.placeBaseStructure(baseGraphic, base1,  2,  2);
        world.placeBaseStructure(baseGraphic, base2, 34, 46);

        unitController  = new UnitController(tileController.getTileGrid());
        highlightSystem = new HighlightSystem(unitController);
        turnManager     = new TurnManager();

        // Player 1 unit (red)
        character = new CharacterRenderer();
        unit      = new CustomUnit(20, 20, 5, 0, Weapon.SWORD, 0, 0, Player.PLAYER_ONE);
        unitController.spawnUnitNearBase(base1, unit);
        unitView  = new UnitView(unit, character);
        unitView.syncPosition();
        unitView.applyPlayerColor(Player.PLAYER_ONE);
        unitController.registerUnit(unit, unitView);

        // Player 2 unit (blue)
        character2 = new CharacterRenderer();
        unit2      = new CustomUnit(20, 1, 5, 0, Weapon.SWORD, 0, 0, Player.PLAYER_TWO);
        unitController.spawnUnitNearBase(base2, unit2);
        unitView2  = new UnitView(unit2, character2);
        unitView2.syncPosition();
        unitView2.applyPlayerColor(Player.PLAYER_TWO);
        unitController.registerUnit(unit2, unitView2);

        unitController.setActiveUnitView(unitView);

        // Random lakes
        Random random = new Random();
        world.placeLakeStructure(lakeGraphic,
            random.nextInt(10, world.getRows() - 10),
            random.nextInt(10, world.getCols() - 10));
        world.placeLakeStructure(lakeGraphic,
            random.nextInt(10, world.getRows() - 10),
            random.nextInt(10, world.getCols() - 10));

        // Base popup + train-unit listener
        basePopup = new BasePopup(null, 200, 150, 300, 200);
        BaseController baseController = new BaseController(unitController);
        basePopup.setTrainUnitListener(new TrainUnitListener() {
            @Override
            public void onTrainUnit(BaseStats base) {
                baseController.createUnit(base);
                System.out.println("Unit created for: " + base.getOwner().getDisplayName());
            }
        });

        // Input – keep reference so we can reach getActionMenu()
        gameInput = new GameInput(tileController, basePopup,
            unitController, highlightSystem, turnManager);
        Gdx.input.setInputProcessor(gameInput);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // tile layer
        batch.begin();
        for (int row = 0; row < world.getRows(); row++) {
            for (int col = 0; col < world.getCols(); col++) {
                int id = world.getTile(row, col);
                int x  = col * WorldMap.TILE_SIZE;
                int y  = row * WorldMap.TILE_SIZE;

                batch.draw(grass, x, y);

                TextureRegion lakeTile = lakeGraphic.getTile(id);
                if (lakeTile != null) { batch.draw(lakeTile, x, y); continue; }

                TextureRegion baseTile = baseGraphic.getTile(id);
                if (baseTile != null) { batch.draw(baseTile, x, y, 16, 16); }
            }
        }
        batch.end();

        // overlay
        basePopup.render(batch);
        highlightSystem.render();

        //unit sprites + HUD text
        batch.begin();
        unitView.update(delta);
        unitView2.update(delta);

        if (unit.isAlive())  unitView.render(batch);
        if (unit2.isAlive()) unitView2.render(batch);

        font.setColor(Color.WHITE);
        font.draw(batch, "Tur: " + turnManager.getCurrentPlayer().getDisplayName(), 10, 55);
        font.draw(batch, "[ End Turn ]", 10, 35);
        batch.end();

        //HP bars
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (unit.isAlive())  unitView.renderHpBar(shapeRenderer);
        if (unit2.isAlive()) unitView2.renderHpBar(shapeRenderer);
        shapeRenderer.end();

        // HP
        batch.begin();
        if (unit.isAlive())  unitView.renderHpText(batch);
        if (unit2.isAlive()) unitView2.renderHpText(batch);
        batch.end();

        // Action meny
        gameInput.getActionMenu().render(batch, shapeRenderer);
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        bgMusic.dispose();
        batch.dispose();
        shapeRenderer.dispose();
    }
}
