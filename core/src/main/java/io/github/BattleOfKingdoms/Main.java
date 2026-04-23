package io.github.BattleOfKingdoms;

import GuiMainGame.Base;
import GuiMainGame.Lake;
import GuiMainGame.SpriteSheetLoader;
import GuiMainGame.WorldMap;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import terrain.TileController;
import GuiMainGame.GameInput;
import popup.BasePopup;

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

    @Override
    public void create() {

        batch = new SpriteBatch();
        sheet = new SpriteSheetLoader();

        tileController = new TileController();
        world = new WorldMap(tileController);

        grass = sheet.getTile(0, 5);

        baseGraphic = new Base(sheet);
        lakeGraphic = new Lake(sheet);

        // Place two random lakes
        Random random = new Random();

        int lake1Row = random.nextInt(10, world.getRows() - 10);
        int lake1Col = random.nextInt(10, world.getCols() - 10);
        world.placeStructure(lakeGraphic, lake1Row, lake1Col);

        int lake2Row = random.nextInt(10, world.getRows() - 10);
        int lake2Col = random.nextInt(10, world.getCols() - 10);
        world.placeStructure(lakeGraphic, lake2Row, lake2Col);

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

                // Draw grass
                batch.draw(grass, x, y);

                // Draw lake tile if exists
                TextureRegion lakeTile = lakeGraphic.getTile(id);
                if (lakeTile != null) {
                    batch.draw(lakeTile, x, y);
                    continue;
                }

                // Draw base graphics if tile belongs to a base
                if (tileController.getTileGrid()[row][col].getBase() != null) {
                    drawBaseGraphic(batch, row, col);
                }
            }
        }

        batch.end();

        if (basePopup.isVisible()) {
            basePopup.render(batch);
        }
    }

    private void drawBaseGraphic(SpriteBatch batch, int row, int col) {

        int[][] layout = baseGraphic.getLayout();

        for (int r = 0; r < layout.length; r++) {
            for (int c = 0; c < layout[0].length; c++) {

                TextureRegion tile = baseGraphic.getTile(layout[r][c]);

                int drawRow = row + r;
                int drawCol = col + c;

                batch.draw(tile,
                    drawCol * WorldMap.TILE_SIZE,
                    drawRow * WorldMap.TILE_SIZE);
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
