package GuiMainGame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Base extends StructureRenderer {

    private TextureRegion cornerL, cornerR, wall, floor, gate;

    public Base(SpriteSheetLoader sheet) {

        layout = new int[][] {
            {1,1,1,1,1},
            {1,2,2,2,1},
            {1,2,3,2,1},
            {1,2,2,2,1},
            {1,1,4,1,1}
        };

        cornerL = sheet.getTile(16, 14);
        cornerR = sheet.getTile(21, 15);
        wall    = sheet.getTile(23, 14);
        floor   = sheet.getTile(23, 19);
        gate    = sheet.getTile(9, 29);
    }

    @Override
    public TextureRegion getTile(int id) {
        return switch (id) {
            case 1 -> cornerL;
            case 2 -> wall;
            case 3 -> floor;
            case 4 -> gate;
            case 5 -> cornerR;
            default -> floor;
        };
    }
}

