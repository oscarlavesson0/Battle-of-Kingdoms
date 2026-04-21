package GuiMainGame;

import GuiMainGame.SpriteSheetLoader;
import GuiMainGame.WorldMap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BaseRenderer {

    private TextureRegion cornerL;
    private TextureRegion cornerR;

    private TextureRegion wall;
    private TextureRegion floor;
    private TextureRegion gate;

    public BaseRenderer(SpriteSheetLoader sheet) {

        // Basens byggdelar
        cornerL = sheet.getTile(22, 14);
        cornerR = sheet.getTile(22, 15);
        wall   = sheet.getTile(23, 14);
        floor  = sheet.getTile(23, 19);
        gate   = sheet.getTile(9, 29);
    }

    public void renderBase(SpriteBatch batch, int startRow, int startCol) {

        int[][] layout = {
            {1,2,2,2,5},
            {2,3,3,3,2},
            {2,3,4,3,2},
            {2,3,3,3,2},
            {2,2,2,2,2}
        };

        for (int r = 0; r < layout.length; r++) {
            for (int c = 0; c < layout[0].length; c++) {

                TextureRegion tile = switch (layout[r][c]) {
                    case 1 -> cornerL;
                    case 2 -> wall;
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

