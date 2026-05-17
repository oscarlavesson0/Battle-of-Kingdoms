package io.github.BattleOfKingdoms;

import base.BaseDestroyedListener;   // NY IMPORT
import base.BaseStats;
import base.IncomeHelper;
import base.Player;
import base.TurnChangeListener;
import base.TurnManager;
import building.BuildingController;
import building.BuildingType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import GuiMainGame.*;
import base.BaseController;
import popup.*;
import terrain.TileController;
import unit.CustomUnit;
import unit.UnitController;

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

    public static final float endTurnBtnW = 120f;
    public static final float endTurnBtnH = 40f;
    private float endTurnBtnX, endTurnBtnY;

    private GameInput gameInput;

    private BaseStats base1, base2;

    public GameScreen(Main game) { this.game = game; }

    @Override
    public void show() {
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal(
            "lwjgl3/assets/Audio/Medieval Fantasy Tavern D&D Fantasy Music and Ambience - Daydreaming of Persephone (128k).mp3"));
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.05f);
        bgMusic.play();

        batch         = new SpriteBatch();
        sheet         = new SpriteSheetLoader();
        tileController = new TileController();
        world         = new WorldMap(tileController);
        grass         = sheet.getTile(0, 5);

        base1 = new BaseStats(Player.PLAYER_ONE, tileController.getTileGrid()[2][2]);
        base2 = new BaseStats(Player.PLAYER_TWO, tileController.getTileGrid()[20][30]);

        baseGraphic = new Base(sheet, base1);
        lakeGraphic = new Lake(sheet);

        world.placeBaseStructure(baseGraphic, base1, 2,  2);
        world.placeBaseStructure(baseGraphic, base2, 20, 30);

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

        basePopup = new BasePopup(null, 200, 150, 300, 200);
        statsPopup = new UnitStatsPopup(300, 200, 300, 250);
        buildingPopup = new BuildingPopup(buildingController,300, 200, 300, 250);

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
            public void OnBuildingChosen(BuildingType buildingType, TurnManager turnManager) {

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
                System.out.println("--- Turn " + newTurnNumber + " ---");
                System.out.println("P1 gold: " + Player.PLAYER_ONE.getGold());
                System.out.println("P2 gold: " + Player.PLAYER_TWO.getGold());
            }
        });

        hudFont  = new BitmapFont();
        hudFont.getData().setScale(1.4f);
        hudShape = new ShapeRenderer();

        endTurnBtnX = Gdx.graphics.getWidth() - endTurnBtnW - 20;
        endTurnBtnY = 20;

        List<BaseStats> allBases = Arrays.asList(base1, base2);
        gameInput = new GameInput(
            tileController, basePopup, statsPopup, buildingPopup,
            unitController, unitController.getUnits(), unitViews,
            highlightSystem, turnManager,
            endTurnBtnX, endTurnBtnY, endTurnBtnW, endTurnBtnH,
            allBases);

        Gdx.input.setInputProcessor(gameInput);
    }

    @Override
    public void render(float delta) {
        turnManager.update(delta);
        gameInput.update();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

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

        basePopup.render(batch);
        if (statsPopup.isVisible()) statsPopup.render();
        if (buildingPopup.isVisible()) buildingPopup.render();
        highlightSystem.render();

        batch.begin();
        for (UnitView uv : unitViews) { uv.update(delta); uv.render(batch); }
        batch.end();

        hudShape.begin(ShapeRenderer.ShapeType.Filled);
        for (UnitView uv : unitViews) uv.renderHpBar(hudShape);

        renderBaseHpBars();

        hudShape.end();

        batch.begin();
        for (UnitView uv : unitViews) uv.renderHpText(batch);
        batch.end();

        renderHud();
        gameInput.getActionMenu().render(batch, hudShape);
    }

    private void renderBaseHpBars() {
        for (BaseStats b : new BaseStats[]{ base1, base2 }) {
            if (b.isDestroyed()) continue;
            float bx = b.getPosition().getX() * WorldMap.TILE_SIZE;
            float by = b.getPosition().getY() * WorldMap.TILE_SIZE + 4 * WorldMap.TILE_SIZE + 4;
            float barW = 4 * WorldMap.TILE_SIZE;
            float barH = 4f;
            float pct  = (float) b.getCurrentHp() / b.getMaxHp();
            hudShape.setColor(com.badlogic.gdx.graphics.Color.DARK_GRAY);
            hudShape.rect(bx, by, barW, barH);
            hudShape.setColor(pct > 0.5f ? com.badlogic.gdx.graphics.Color.GREEN
                : com.badlogic.gdx.graphics.Color.ORANGE);
            hudShape.rect(bx, by, barW * pct, barH);
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

        batch.begin();
        hudFont.setColor(Color.WHITE);
        String topLine = "Turn " + turnManager.getTurnNumber()
            + "  " + Math.round(turnManager.getTimeRemaining()) + "s"
            + "  " + turnManager.getCurrentPlayer().getDisplayName();
        hudFont.draw(batch, topLine, screenW/2f - 150, screenH - 20);

        hudFont.setColor(Color.YELLOW);
        hudFont.draw(batch, "P1: " + Player.PLAYER_ONE.getGold() + "g", 20, screenH - 20);
        hudFont.draw(batch, "P2: " + Player.PLAYER_TWO.getGold() + "g", 20, screenH - 50);

        hudFont.setColor(Color.WHITE);
        hudFont.draw(batch, "Bas P1: " + base1.getCurrentHp() + "/" + base1.getMaxHp(),
            20, screenH - 80);
        hudFont.draw(batch, "Bas P2: " + base2.getCurrentHp() + "/" + base2.getMaxHp(),
            20, screenH - 110);

        hudFont.setColor(Color.WHITE);
        hudFont.draw(batch, "End Turn", bx + 10, by + 20);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        hudShape.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
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
    }
}
