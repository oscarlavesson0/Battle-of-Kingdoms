package GuiMainGame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Base extends StructureRenderer {

    private TextureRegion cornerL, cornerR, wall, floor, gate;

    public Base(SpriteSheetLoader sheet) {

        layout = new int[][] {
            {501,501,501,501,501},
            {501,502,502,502,501},
            {501,502,503,502,501},
            {501,502,502,502,501},
            {501,501,504,501,501}
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
            case 501 -> cornerL;
            case 502 -> wall;
            case 503 -> floor;
            case 504 -> gate;
            case 505 -> cornerR;
            default -> floor;
        };
    }
}

