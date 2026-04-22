package GuiMainGame;

import GuiMainGame.SpriteSheetLoader;
import GuiMainGame.WorldMap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BaseRenderer {

    private TextureRegion cornerL;
    private TextureRegion cornerR;
    private TextureRegion outerwall;
    private TextureRegion floor;
    private TextureRegion gate;
    private TextureRegion innerwall;


    public BaseRenderer(SpriteSheetLoader sheet) {

        // Basens byggdelar
        cornerL = sheet.getTile(16, 14);
        cornerR = sheet.getTile(21, 15);
        outerwall = sheet.getTile(23, 14);
        innerwall = sheet.getTile(23, 14);
        floor = sheet.getTile(23, 19);
        gate = sheet.getTile(9, 29);
    }

    public void renderBase(SpriteBatch batch, int startRow, int startCol) {

        int[][] layout = {
            {1,1,1,1,1},
            {1,1,1,1,1},
            {1,1,1,1,1},
            {1,1,1,1,1},
            {1,1,1,1,1}
        };

        for (int r = 0; r < layout.length; r++) {
            for (int c = 0; c < layout[0].length; c++) {

                TextureRegion tile = switch (layout[r][c]) {
                    case 1 -> cornerL;
                    case 2 -> outerwall;
                    case 3 -> floor;
                    case 4 -> gate;
                    case 5 -> cornerR;
                    default -> floor;
                };

                batch.draw(tile,
                    (startCol + c) * WorldMap.TILE_SIZE,
                    (startRow + r) * WorldMap.TILE_SIZE);
            }
        }
    }
}

