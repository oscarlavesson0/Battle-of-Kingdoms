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

import static com.badlogic.gdx.math.MathUtils.random;

public class GameScreen implements Screen {

    private final Main game;
    private Music bgMusic;
    private SpriteBatch batch;
    private SpriteSheetLoader sheet;
    private TextureRegion grass;
    private TileController tileController;
    private WorldMap world;
    private Base baseGraphic;
    private List<Lake> lakes = new ArrayList<>();
    private BasePopup basePopup;
    private UnitStatsPopup statsPopup;
    private BuildingMenuPopup buildingMenuPopup;
    private BuildingInfoPopup buildingInfoPopup;
    private List<UnitView> unitViews = new ArrayList<>();
    private BaseController baseController;
    private UnitController unitController;
    private BuildingController buildingController;
    private HighlightSystem highlightSystem;
    private TurnManager turnManager;
    private IncomeHelper incomeHelper;
    private BitmapFont hudFont;
    private BitmapFont titleFont;
    private ShapeRenderer hudShape;
    private Texture coinIcon;
    private Texture hpIcon;
    private Texture gearIcon;
    private SettingsPopup settingsPopup;
    private ConfirmPopup confirmPopup;

    public static final float gearBtnSize = 40f;
    private float gearBtnX, gearBtnY;

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
        List<int[]> placedLakePositions = new ArrayList<>();
        int[] lakeSizes = {5, 7, 8};
        int lakeCount = random.nextInt(2, 5);

        for (int i = 0; i < lakeCount; i++) {
            int size = lakeSizes[random.nextInt(lakeSizes.length)];
            Lake lake = new Lake(sheet, size);
            lakes.add(lake);
            placeLakeSafely(random, lake, placedLakePositions);
        }

        basePopup = new BasePopup(null, 200, 150, 300, 200, camera);
        statsPopup = new UnitStatsPopup(300, 200, 300, 250, camera);
        buildingMenuPopup = new BuildingMenuPopup(buildingController, 300, 200, 300, 250);
        buildingInfoPopup = new BuildingInfoPopup(300, 200, 300, 250);

        basePopup.setTrainUnitListener(new TrainUnitListener() {
            @Override public void onTrainUnit(BaseStats base) { statsPopup.open(base);}

            @Override
            public void onCreateBuilding(BaseStats base) {
                buildingMenuPopup.open(base);
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

        buildingMenuPopup.setListener(new BuildingChosenListener() {
            @Override
            public void OnBuildingChosen(BuildingType buildingType, BaseStats baseStats) {
                Building building = buildingController.createBuilding(buildingType, baseStats);
                if (building == null) {
                    buildingMenuPopup.ShowError("Not enough gold!");
                }
            }
        });

        turnManager  = new TurnManager();
        incomeHelper = new IncomeHelper();
        turnManager.setListener(new TurnChangeListener() {
            @Override public void onPlayerSwitch(Player newPlayer) {
                basePopup.hide();
                statsPopup.hide();
                buildingMenuPopup.hide();
                buildingInfoPopup.hide();
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
        parameter.size = 30;
        hudFont = generator.generateFont(parameter);
        generator.dispose();

        hudShape = new ShapeRenderer();

        coinIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/coin.png"));
        hpIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/heart.png"));
        gearIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/gear.png"));

        settingsPopup = new SettingsPopup(330, 350, 300, 250, camera);
        confirmPopup  = new ConfirmPopup(330, 380, 300, 160, camera);

        settingsPopup.open(new popup.SettingsActionListener() {
            @Override public void onResume() { }
            @Override public void onMainMenu() {
                confirmPopup.open("Return to main menu?", confirmed -> {
                    if (confirmed) {
                        bgMusic.stop();
                        game.showStartMenu();
                    }
                });
            }
            @Override public void onQuit() {
                confirmPopup.open("Quit the game?", confirmed -> {
                    if (confirmed) Gdx.app.exit();
                });
            }
        });
        settingsPopup.hide();

        endTurnBtnX = Gdx.graphics.getWidth() - endTurnBtnW - 20;
        endTurnBtnY = 20;
        gearBtnX = VIRTUAL_WIDTH - gearBtnSize - 20;
        gearBtnY = VIRTUAL_HEIGHT - gearBtnSize - 20;

        List<BaseStats> allBases = Arrays.asList(base1, base2);
        gameInput = new GameInput(
            tileController, basePopup, statsPopup, buildingMenuPopup, buildingInfoPopup,
            unitController, unitController.getUnits(), unitViews,
            highlightSystem, turnManager,
            endTurnBtnX, endTurnBtnY, endTurnBtnW, endTurnBtnH,
            allBases, viewport,
            settingsPopup, confirmPopup,
            gearBtnX, gearBtnY, gearBtnSize);

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

                boolean drawnLake = false;
                for (Lake lake : lakes) {
                    TextureRegion lakeTile = lake.getTile(id);
                    if (lakeTile != null) {
                        batch.draw(lakeTile, x, y);
                        drawnLake = true;
                        break;
                    }
                }
                if (drawnLake) continue;
                TextureRegion baseTile = baseGraphic.getTile(id);
                if (baseTile != null) { batch.draw(baseTile, x, y, 16, 16); continue; }
            }
        }
        batch.end();

        // POPUPS
        basePopup.render(batch);
        if (statsPopup.isVisible()) statsPopup.render();
        if (buildingMenuPopup.isVisible()) buildingMenuPopup.render(hudShape, batch, camera);
        if (buildingInfoPopup.isVisible()) buildingInfoPopup.render(hudShape, batch, camera);

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

        // Kugghjul-knapp
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(gearIcon, gearBtnX, gearBtnY, gearBtnSize, gearBtnSize);
        batch.end();

        // Settings + Confirm popups
        settingsPopup.render();
        confirmPopup.render();

        // BYGGNADER
        batch.begin();
        buildingController.getBuildingRenderer().render(batch);
        batch.end();

    }

    private void renderBaseHpBars() {
        for (BaseStats b : new BaseStats[]{ base1, base2 }) {
            if (b.isDestroyed()) continue;
            // Basens tile-position (övre vänstra hörnet)
            float baseTileX = b.getPosition().getX();
            float baseTileY = b.getPosition().getY();

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
        hudFont.draw(batch, topLine, screenW/2f - 180, screenH - 20);

        hudFont.setColor(Color.BLACK);
        batch.draw(coinIcon, 124, screenH - 44, 25, 25);
        hudFont.draw(batch, "P1: " + Player.PLAYER_ONE.getGold(), 20, screenH - 20);
        batch.draw(coinIcon, 124, screenH - 78, 25, 25);
        hudFont.draw(batch, "P2: " + Player.PLAYER_TWO.getGold(), 20, screenH - 50);

        hudFont.setColor(Color.BLACK);
        batch.draw(hpIcon, 240, screenH - 115, 25, 25);
        hudFont.draw(batch, "Bas P1: " + base1.getCurrentHp() + "/" + base1.getMaxHp(),
            20, screenH - 85);
        batch.draw(hpIcon, 240, screenH - 150, 25, 25);
        hudFont.draw(batch, "Bas P2: " + base2.getCurrentHp() + "/" + base2.getMaxHp(),
            20, screenH - 120);

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
        gearIcon.dispose();
        settingsPopup.dispose();
        confirmPopup.dispose();
    }
    private void placeLakeSafely(Random random, Lake lake, List<int[]> placedLakePositions) {
        int minDistanceFromBase = 10;
        int minDistanceFromLake = 8; // minsta avstånd mellan sjöar
        int maxAttempts = 100;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            int row = random.nextInt(10, world.getRows() - 10);
            int col = random.nextInt(10, world.getCols() - 10);

            // Kolla avstånd till bas1
            int base1Row = base1.getPosition().getX();
            int base1Col = base1.getPosition().getY();
            int dist1 = Math.abs(row - base1Row) + Math.abs(col - base1Col);

            // Kolla avstånd till bas2
            int base2Row = base2.getPosition().getX();
            int base2Col = base2.getPosition().getY();
            int dist2 = Math.abs(row - base2Row) + Math.abs(col - base2Col);

            if (dist1 < minDistanceFromBase || dist2 < minDistanceFromBase) continue;

            // Kolla avstånd till redan placerade sjöar
            boolean tooCloseToLake = false;
            for (int[] pos : placedLakePositions) {
                int distToLake = Math.abs(row - pos[0]) + Math.abs(col - pos[1]);
                if (distToLake < minDistanceFromLake) {
                    tooCloseToLake = true;
                    break;
                }
            }
            if (tooCloseToLake) continue;

            // Säker plats hittad
            world.placeLakeStructure(lake, row, col);
            placedLakePositions.add(new int[]{row, col});
            return;
        }
        System.out.println("Could not place lake safely after " + maxAttempts + " attempts");
    }
}
