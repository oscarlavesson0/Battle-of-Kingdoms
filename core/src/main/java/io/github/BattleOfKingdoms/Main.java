package io.github.BattleOfKingdoms;

import GuiMainGame.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import terrain.TileController;
import popup.BasePopup;
import unit.Unit;
import unit.Weapon;

import java.util.Random;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private SpriteSheetLoader sheet;
    private TextureRegion grass;
    private TileController tileController;
    private WorldMap world;

    private Base baseGraphic;
    private Lake lakeGraphic;

    private BasePopup basePopup;

    private CharacterRenderer character;
    private int charX = 20, charY = 20;
    private UnitView unitView;
    private Unit unit;



    @Override
    public void create() {

        batch = new SpriteBatch();
        sheet = new SpriteSheetLoader();

        tileController = new TileController();
        world = new WorldMap(tileController);

        grass = sheet.getTile(0, 5);

        baseGraphic = new Base(sheet);
        lakeGraphic = new Lake(sheet);

        Unit unit = new Unit(20,5,4,2, Weapon.SWORD, 10,10);
        character = new CharacterRenderer();
        unitView = new UnitView(unit, character);

        // Place two random lakes
        Random random = new Random();

        int lake1Row = random.nextInt(10, world.getRows() - 10);
        int lake1Col = random.nextInt(10, world.getCols() - 10);
        world.placeLakeStructure(lakeGraphic, lake1Row, lake1Col);

        int lake2Row = random.nextInt(10, world.getRows() - 10);
        int lake2Col = random.nextInt(10, world.getCols() - 10);
        world.placeLakeStructure(lakeGraphic, lake2Row, lake2Col);

        world.placeBaseStructure(baseGraphic, 2,2);
        world.placeBaseStructure(baseGraphic, 34,46);

        basePopup = new BasePopup(null, 200, 150, 300, 200);

        Gdx.input.setInputProcessor(new GameInput(tileController, basePopup));
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        for (int row = 0; row < world.getRows(); row++) {
            for (int col = 0; col < world.getCols(); col++) {

                int id = world.getTile(row, col);
                int x = col * WorldMap.TILE_SIZE;
                int y = row * WorldMap.TILE_SIZE;

                // Rita gräs
                batch.draw(grass, x, y);

                // Rita sjö
                TextureRegion lakeTile = lakeGraphic.getTile(id);
                if (lakeTile != null) {
                    batch.draw(lakeTile, x, y);
                    continue;
                }

                // Rita bas
                TextureRegion baseTile = baseGraphic.getTile(id);
                if (baseTile != null) {
                    batch.draw(baseTile, x, y, 16, 16);
                    continue;
                }
                character.update(Gdx.graphics.getDeltaTime());

                float delta = Gdx.graphics.getDeltaTime();
                unitView.update(delta);
                unitView.render(batch);
            }
        }
        batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
