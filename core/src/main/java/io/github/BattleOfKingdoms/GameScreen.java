package io.github.BattleOfKingdoms;

import base.BaseDestroyedListener;   // NY IMPORT
import base.BaseStats;
import base.IncomeHelper;
import base.Player;
import base.TurnChangeListener;
import base.TurnManager;
import building.Building;
import building.BuildingController;
import building.BuildingType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import GuiMainGame.*;
import base.BaseController;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import popup.*;
import terrain.TileController;
import unit.CustomUnit;
import unit.UnitController;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import java.util.ArrayList;
import java.util.Arrays;  // NY IMPORT
import java.util.List;
import java.util.Random;

public class GameScreen implements Screen {

    private final Main game;
    private Music bgMusic;
    private SpriteBatch batch;
    private SpriteSheetLoader sheet;
    private TextureRegion grass;
    private TileController tileController;
    private WorldMap world;
    private Base baseGraphic;
    private Lake lakeGraphic;
    private BasePopup basePopup;
    private UnitStatsPopup statsPopup;
    private BuildingPopup buildingPopup;
    private List<UnitView> unitViews = new ArrayList<>();
    private BaseController baseController;
    private UnitController unitController;
    private BuildingController buildingController;
    private HighlightSystem highlightSystem;
    private TurnManager turnManager;
    private IncomeHelper incomeHelper;
    private BitmapFont hudFont;
    private ShapeRenderer hudShape;
    private Texture coinIcon;
    private Texture hpIcon;

    public static final float endTurnBtnW = 120f;
    public static final float endTurnBtnH = 40f;
    private float endTurnBtnX, endTurnBtnY;

    private GameInput gameInput;

    private BaseStats base1, base2;

    private OrthographicCamera camera;
    private Viewport viewport;
    private static final float VIRTUAL_WIDTH = 960f;
    private static final float VIRTUAL_HEIGHT = 960f;

    public GameScreen(Main game) { this.game = game; }

    @Override
    public void show() {
        System.out.println("show() started");
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal(
            "lwjgl3/assets/Audio/Medieval Fantasy Tavern D&D Fantasy Music and Ambience - Daydreaming of Persephone (128k).mp3"));
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.05f);
        bgMusic.play();

        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();

        batch         = new SpriteBatch();
        sheet         = new SpriteSheetLoader();
        tileController = new TileController();
        world         = new WorldMap(tileController);
        grass         = sheet.getTile(0, 5);
        baseGraphic = new Base(sheet, base1);
        lakeGraphic = new Lake(sheet);

        base1 = new BaseStats(Player.PLAYER_ONE, tileController.getTileGrid()[2][28]);
        base2 = new BaseStats(Player.PLAYER_TWO, tileController.getTileGrid()[55][28]);

        world.placeBaseStructure(baseGraphic, base1, 28,  2);
        world.placeBaseStructure(baseGraphic, base2, 28, 55);

        unitController  = new UnitController(tileController.getTileGrid());
        highlightSystem = new HighlightSystem(unitController);
        baseController  = new BaseController(unitController);
        buildingController = new BuildingController(tileController);

        unitController.setBaseDestroyedListener(new BaseDestroyedListener() {
            @Override
            public void onBaseDestroyed(BaseStats destroyedBase, Player winner) {
                // Byt till vinst-skärmen på nästa render-cykel är säkrast,
                // men ett direkt anrop fungerar också i libGDX.
                bgMusic.stop();
                game.showWinScreen(winner);
            }
        });

        baseController.setSpawnListener(spawnedUnit -> {
            UnitType type = spawnedUnit.getUnitType();
            CharacterRenderer r = new CharacterRenderer(type);
            UnitView view = new UnitView(spawnedUnit, r);
            view.applyPlayerColor(spawnedUnit.getPlayer());
            unitViews.add(view);
        });

        baseController.createUnitFromChoice(base1, 20, 5, 5, 1);
        baseController.createUnitFromChoice(base2, 20, 5, 5, 1);

        Random random = new Random();
        world.placeLakeStructure(lakeGraphic,
            random.nextInt(10, world.getRows()-10),
            random.nextInt(10, world.getCols()-10));
        world.placeLakeStructure(lakeGraphic,
            random.nextInt(10, world.getRows()-10),
            random.nextInt(10, world.getCols()-10));

        basePopup = new BasePopup(null, 200, 150, 300, 200, camera);
        statsPopup = new UnitStatsPopup(300, 200, 300, 250, camera);
        buildingPopup = new BuildingPopup(buildingController, 300, 200, 300, 250, camera);

        basePopup.setTrainUnitListener(new TrainUnitListener() {
            @Override public void onTrainUnit(BaseStats base) { statsPopup.open(base);}

            @Override
            public void onCreateBuilding(BaseStats base) {
                buildingPopup.open(base, buildingController);
            }

        });

        statsPopup.setListener(new StatsChosenListener() {
            @Override
            public void onStatsChosen(BaseStats base, int hp, int attack, int speed, int defence) {
                CustomUnit unit = baseController.createUnitFromChoice(base, hp, attack, speed, defence);
                if (unit == null) { statsPopup.ShowError("Not enough gold!"); return; }
                System.out.println("Unit created: HP=" + hp + " ATK=" + attack
                    + " SPD=" + speed + " DEF=" + defence);
            }
        });

        buildingPopup.setListener(new BuildingChosenListener() {
            @Override
            public void OnBuildingChosen(BuildingType buildingType, BaseStats baseStats) {
                Building building = buildingController.createBuilding(buildingType, baseStats);
                if (building == null) {
                    buildingPopup.ShowError("Not enough gold!");
                }
            }
        });

        turnManager  = new TurnManager();
        incomeHelper = new IncomeHelper();
        turnManager.setListener(new TurnChangeListener() {
            @Override public void onPlayerSwitch(Player newPlayer) {
                basePopup.hide();
                statsPopup.hide();
                buildingPopup.hide();
                unitController.resetMovement();
                System.out.println("Currently " + newPlayer.getDisplayName() + "'s turn!");
            }
            @Override public void onTurnComplete(int newTurnNumber) {
                incomeHelper.applyIncome(Player.PLAYER_ONE);
                incomeHelper.applyIncome(Player.PLAYER_TWO);
                buildingController.updateBuildings();
                System.out.println("--- Turn " + newTurnNumber + " ---");
                System.out.println("P1 gold: " + Player.PLAYER_ONE.getGold());
                System.out.println("P2 gold: " + Player.PLAYER_TWO.getGold());
            }
        });

        //hudFont  = new BitmapFont();
        //hudFont.getData().setScale(1.4f);
        //hudShape = new ShapeRenderer();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("lwjgl3/assets/ui/font/PixelWarden.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 20;
        hudFont = generator.generateFont(parameter);
        generator.dispose();

        hudShape = new ShapeRenderer();

        coinIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/coin.png"));
        hpIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/heart.png"));

        endTurnBtnX = Gdx.graphics.getWidth() - endTurnBtnW - 20;
        endTurnBtnY = 20;

        List<BaseStats> allBases = Arrays.asList(base1, base2);
        gameInput = new GameInput(
            tileController, basePopup, statsPopup, buildingPopup,
            unitController, unitController.getUnits(), unitViews,
            highlightSystem, turnManager,
            endTurnBtnX, endTurnBtnY, endTurnBtnW, endTurnBtnH,
            allBases, viewport);

        Gdx.input.setInputProcessor(gameInput);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);


        viewport.apply(true);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        hudShape.setProjectionMatrix(camera.combined);

        gameInput.update();
        turnManager.update(delta);

        // KARTA
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
                if (baseTile != null) { batch.draw(baseTile, x, y, 16, 16); continue; }
            }
        }
        batch.end();

        // POPUPS
        basePopup.render(batch);
        if (statsPopup.isVisible()) statsPopup.render();
        if (buildingPopup.isVisible()) buildingPopup.render();

        if (batch.isDrawing()) batch.end();
        if (hudShape.isDrawing()) hudShape.end();

        // HIGHLIGHT
        highlightSystem.render(camera);

        // UNITS
        batch.begin();
        for (UnitView uv : unitViews) { uv.update(delta); uv.render(batch); }
        batch.end();

        // HP-BARS
        hudShape.begin(ShapeRenderer.ShapeType.Filled);
        for (UnitView uv : unitViews) uv.renderHpBar(hudShape);
        renderBaseHpBars();
        hudShape.end();

        // HP-TEXT
        batch.begin();
        for (UnitView uv : unitViews) uv.renderHpText(batch);
        batch.end();

        // HUD
        renderHud();
        gameInput.getActionMenu().render(batch, hudShape);

        // BYGGNADER
        batch.begin();
        buildingController.getBuildingRenderer().render(batch);
        batch.end();

    }

    private void renderBaseHpBars() {
        for (BaseStats b : new BaseStats[]{ base1, base2 }) {
            if (b.isDestroyed()) continue;
            // Basens tile-position (övre vänstra hörnet)
            float baseTileX = b.getPosition().getY();
            float baseTileY = b.getPosition().getX();

            // Din bas är 4x4 tiles
            int baseWidthTiles = 4;
            int baseHeightTiles = 4;

            // Pixelposition för basens övre vänstra hörn
            float px = baseTileX * WorldMap.TILE_SIZE;
            float py = baseTileY * WorldMap.TILE_SIZE;

            // Healthbar bredd = hela basens bredd
            float barW = baseWidthTiles * WorldMap.TILE_SIZE;
            float barH = 4f;

            // Healthbar ska ligga ovanför basens högsta punkt
            float barX = px;
            float barY = py + (baseHeightTiles * WorldMap.TILE_SIZE) + 6;

            float pct = (float) b.getCurrentHp() / b.getMaxHp();

            // Bakgrund
            hudShape.setColor(Color.DARK_GRAY);
            hudShape.rect(barX, barY, barW, barH);

            // Fyllnad
            hudShape.setColor(pct > 0.5f ? Color.GREEN : Color.ORANGE);
            hudShape.rect(barX, barY, barW * pct, barH);
        }
    }



    private void renderHud() {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        float bx = screenW - endTurnBtnW - 20;
        float by = 20;

        hudShape.begin(ShapeRenderer.ShapeType.Filled);
        hudShape.setColor(0.15f, 0.4f, 0.15f, 0.9f);
        hudShape.rect(bx, by, endTurnBtnW, endTurnBtnH);
        hudShape.end();

        hudShape.setProjectionMatrix(new com.badlogic.gdx.math.Matrix4()
            .setToOrtho2D(0, 0, screenW, screenH));
        batch.setProjectionMatrix(new com.badlogic.gdx.math.Matrix4()
            .setToOrtho2D(0, 0, screenW, screenH));

        hudShape.begin(ShapeRenderer.ShapeType.Filled);
        hudShape.setColor(0.15f, 0.4f, 0.15f, 0.9f);
        hudShape.rect(bx, by, endTurnBtnW, endTurnBtnH);
        hudShape.end();

        batch.begin();
        hudFont.setColor(Color.BLACK);
        String topLine = "Turn " + turnManager.getTurnNumber()
            + "  " + Math.round(turnManager.getTimeRemaining()) + "s"
            + "  " + turnManager.getCurrentPlayer().getDisplayName();
        hudFont.draw(batch, topLine, screenW/2f - 150, screenH - 20);

        hudFont.setColor(Color.BLACK);
        batch.draw(coinIcon, 92, screenH - 38, 22, 22);
        hudFont.draw(batch, "P1: " + Player.PLAYER_ONE.getGold(), 20, screenH - 20);
        batch.draw(coinIcon, 92, screenH - 68, 22, 22);
        hudFont.draw(batch, "P2: " + Player.PLAYER_TWO.getGold(), 20, screenH - 50);

        hudFont.setColor(Color.BLACK);
        batch.draw(hpIcon, 170, screenH - 102, 22, 22);
        hudFont.draw(batch, "Bas P1: " + base1.getCurrentHp() + "/" + base1.getMaxHp(),
            20, screenH - 80);
        batch.draw(hpIcon, 170, screenH - 132, 22, 22);
        hudFont.draw(batch, "Bas P2: " + base2.getCurrentHp() + "/" + base2.getMaxHp(),
            20, screenH - 110);

        hudFont.setColor(Color.GOLD);
        hudFont.draw(batch, "End Turn", bx + 10, by + 20);
        batch.end();

        batch.setProjectionMatrix(camera.combined);
        hudShape.setProjectionMatrix(camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        bgMusic.dispose();
        batch.dispose();
        hudFont.dispose();
        hudShape.dispose();
        coinIcon.dispose();
        hpIcon.dispose();
    }
}
