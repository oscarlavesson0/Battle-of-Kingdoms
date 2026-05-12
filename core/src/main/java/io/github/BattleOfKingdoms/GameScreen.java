package io.github.BattleOfKingdoms;

import GuiMainGame.*;
import base.BaseController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import popup.StatsChosenListener;
import terrain.TileController;
import popup.BasePopup;
import unit.Unit;
import unit.Weapon;
import unit.UnitController;
import com.badlogic.gdx.audio.Music;
import base.BaseStats;
import base.Player;
import popup.TrainUnitListener;
import unit.CustomUnit;
import popup.UnitStatsPopup;

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

    private CharacterRenderer character;
    private UnitView unitView;
    private Unit unit;

    private UnitController unitController;
    private HighlightSystem highlightSystem;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        //Musik
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("lwjgl3/assets/Audio/Medieval Fantasy Tavern D&D Fantasy Music and Ambience - Daydreaming of Persephone (128k).mp3"));
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.05f);
        bgMusic.play();

        batch = new SpriteBatch();
        sheet = new SpriteSheetLoader();

        tileController = new TileController();
        world = new WorldMap(tileController);

        grass = sheet.getTile(0, 5);

        // LOGIK-BASER
        BaseStats base1 = new BaseStats(Player.PLAYER_ONE, tileController.getTileGrid()[2][2]);
        BaseStats base2 = new BaseStats(Player.PLAYER_TWO, tileController.getTileGrid()[34][46]);

        baseGraphic = new Base(sheet, base1);
        lakeGraphic = new Lake(sheet);


        // GRAFIK + LOGIK
        world.placeBaseStructure(baseGraphic, base1, 2, 2);
        world.placeBaseStructure(baseGraphic, base2, 34, 46);

        character = new CharacterRenderer();
        unit = new Unit(20, 5, 4, 2, Weapon.SWORD, 10, 10, Player.PLAYER_ONE);
        unitView = new UnitView(unit, character);
        unitView.applyPlayerColor(Player.PLAYER_ONE);



        unitController = new UnitController(tileController.getTileGrid());
        unitController.setUnitView(unitView);
        highlightSystem = new HighlightSystem(unitController);

        Random random = new Random();

        int lake1Row = random.nextInt(10, world.getRows() - 10);
        int lake1Col = random.nextInt(10, world.getCols() - 10);
        world.placeLakeStructure(lakeGraphic, lake1Row, lake1Col);

        int lake2Row = random.nextInt(10, world.getRows() - 10);
        int lake2Col = random.nextInt(10, world.getCols() - 10);
        world.placeLakeStructure(lakeGraphic, lake2Row, lake2Col);

        //Popup Bas
        basePopup = new BasePopup(null, 200, 150, 300, 200);

        //Popup för units-stats
        statsPopup = new UnitStatsPopup(300, 200, 300, 250);

        BaseController baseController = new BaseController(unitController);

        basePopup.setTrainUnitListener(new TrainUnitListener() {
            @Override
            public void onTrainUnit(BaseStats base) {
                statsPopup.open(base); // Öppna stats popup
            }
        });

        // Koppla stat-popupens "Train units"
        statsPopup.setListener(new StatsChosenListener() {
            @Override
            public void onStatsChosen(BaseStats base, int hp, int attack, int speed, int defence) {

                // Basecontroller skapar och placerar unit
                baseController.createUnitFromChoice(base);

                System.out.println("Unit created with stats: HP - " + hp +" ATK - " + attack + " SPD - " + speed + " DEF - " + defence);
            }
        });

        //Input
        Gdx.input.setInputProcessor(
            new GameInput(tileController, basePopup, statsPopup,unitController, unit, highlightSystem)
        );
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.begin();
        for (int row = 0; row < world.getRows(); row++) {
            for (int col = 0; col < world.getCols(); col++) {

                int id = world.getTile(row, col);
                int x = col * WorldMap.TILE_SIZE;
                int y = row * WorldMap.TILE_SIZE;

                batch.draw(grass, x, y);

                TextureRegion lakeTile = lakeGraphic.getTile(id);
                if (lakeTile != null) {
                    batch.draw(lakeTile, x, y);
                    continue;
                }

                TextureRegion baseTile = baseGraphic.getTile(id);
                if (baseTile != null) {
                    batch.draw(baseTile, x, y, 16, 16);
                    continue;
                }
            }
        }
        batch.end();

        // RITA POPUPEN
        basePopup.render(batch);

        // RITA UNIT-STATS POPUP
        if (statsPopup.isVisible()){
            statsPopup.render();
        }

        // RITA HIGHLIGHT
        highlightSystem.render();

        // RITA UNIT
        batch.begin();
        unitView.update(delta);
        unitView.render(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        bgMusic.dispose();
        batch.dispose();
    }
}
