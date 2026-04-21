package io.github.BattleOfKingdoms;

import GuiMainGame.BaseRenderer;
import GuiMainGame.LakeRenderer;
import GuiMainGame.SpriteSheetLoader;
import GuiMainGame.WorldMap;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private SpriteSheetLoader sheet;
    private TextureRegion water;
    private TextureRegion grass;
    private WorldMap world;
    private LakeRenderer lakeRenderer;
    private BaseRenderer baseRenderer;



    @Override
    public void create() {
        batch = new SpriteBatch();
        sheet = new SpriteSheetLoader();
        world = new WorldMap();

        grass = sheet.getTile(16, 0); // gräs
        lakeRenderer = new LakeRenderer(sheet);
        baseRenderer = new BaseRenderer(sheet);


    }


    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.begin();

        // Rita gräs
        for (int r = 0; r < world.getRows(); r++) {
            for (int c = 0; c < world.getCols(); c++) {
                if (world.getTile(r, c) == 1) {
                    batch.draw(grass, c * WorldMap.TILE_SIZE, r * WorldMap.TILE_SIZE);
                }
            }
        }

        // Rita sjön
        lakeRenderer.renderLake(batch, world);
        baseRenderer.renderBase(batch, 15, 15);


        batch.end();
    }

    @Override
   public void dispose() {
        batch.dispose();
    }
}
