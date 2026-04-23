package io.github.BattleOfKingdoms;

import GuiMainGame.Base;
import GuiMainGame.Lake;
import GuiMainGame.SpriteSheetLoader;
import GuiMainGame.WorldMap;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.*;
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
    private Base base;
    private BasePopup basePopup;
    private GameInput input;
    private Lake lake1;
    private Lake lake2;
    private Base base1;
    private Base base2;


    @Override
    public void create() {

            batch = new SpriteBatch();
            sheet = new SpriteSheetLoader();

            tileController = new TileController();
            world = new WorldMap(tileController);

            grass = sheet.getTile(0, 5);

            // Skapa strukturer
            lake1 = new Lake(sheet);
            lake2 = new Lake(sheet);

            base1 = new Base(sheet);
            base2 = new Base(sheet);

            // Placera baser i hörnen
            int baseSize = base1.getWidth();

        world.placeStructure(base1, 2, 2);   // nedre vänster
        world.placeStructure(base2, 35, 47);   // övre höger



        // Placera två sjöar på slumpmässiga platser
            Random random = new Random();

            int lake1Row = random.nextInt(10, world.getRows() - 10);
            int lake1Col = random.nextInt(10, world.getCols() - 10);
            world.placeStructure(lake1, lake1Row, lake1Col);

            int lake2Row = random.nextInt(10, world.getRows() - 10);
            int lake2Col = random.nextInt(10, world.getCols() - 10);
            world.placeStructure(lake2, lake2Row, lake2Col);

            // Popup + input
            basePopup = new BasePopup(null, 200, 150, 300, 200);
            input = new GameInput(tileController, basePopup);
            Gdx.input.setInputProcessor(input);

    }

    @Override
    public void render() {

            ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
            batch.begin();

            for (int r = 0; r < world.getRows(); r++) {
                for (int c = 0; c < world.getCols(); c++) {

                    int id = world.getTile(r, c);
                    int x = c * WorldMap.TILE_SIZE;
                    int y = r * WorldMap.TILE_SIZE;

                    // 1. Rita gräs först (bakgrund)
                    if (id == 1) {
                        batch.draw(grass, x, y);
                        continue;
                    }

                    // 2. Försök rita sjö
                    TextureRegion lakeTile = lake1.getTile(id);
                    if (lakeTile != null) {
                        batch.draw(lakeTile, x, y);
                        continue;
                    }

                    // 3. Försök rita bas
                    TextureRegion baseTile = base1.getTile(id);
                    if (baseTile != null) {
                        batch.draw(baseTile, x, y);
                        continue;
                    }

                    // 4. Om inget matchar → fallback till gräs
                    batch.draw(grass, x, y);
                }
            }

            batch.end();

            if (basePopup != null && basePopup.isVisible()) {
                basePopup.render(batch);
            }
    }

        @Override
        public void dispose () {
            batch.dispose();
        }
    }

