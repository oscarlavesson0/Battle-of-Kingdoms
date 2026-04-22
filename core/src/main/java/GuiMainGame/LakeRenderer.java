package GuiMainGame;

import GuiMainGame.SpriteSheetLoader;
import GuiMainGame.WorldMap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import terrain.Tile;
import terrain.TileController;

import java.util.ArrayList;
import java.util.List;

public class LakeRenderer {

    private TextureRegion w00, w01, w02;
    private TextureRegion w10, w11, w12;
    private TextureRegion w20, w21, w22;
    Tile[][] tileGrid;
    TextureRegion[][] textureGrid;

    public LakeRenderer(SpriteSheetLoader sheet, TileController tileController) {
        tileGrid = tileController.getTileGrid();
        textureGrid = new TextureRegion[tileGrid.length][tileGrid[0].length];
        for (int i = 0; i < tileGrid.length; i++){
            for (int j = 0; j < tileGrid[0].length; j++){
                if (tileGrid[i][j].getTerrain().getName().equals("Water")){
                    textureGrid[i][j] = sheet.getTile(1, 3);
                }
                if (tileGrid[i][j].getTerrain().getName().equals("Mountain")){
                    textureGrid[i][j] = sheet.getTile(13, 22);
                }
            }
        }
        /*
         // Ladda sjö-tiles
        w00 = sheet.getTile(0, 2);
        w01 = sheet.getTile(0, 3);
        w02 = sheet.getTile(0, 4);

        w10 = sheet.getTile(1, 2);
        w11 = sheet.getTile(1, 3);
        w12 = sheet.getTile(1, 4);

        w20 = sheet.getTile(2, 2);
        w21 = sheet.getTile(2, 3);
        w22 = sheet.getTile(2, 4);
         */

    }

    public TextureRegion getLakeTile(int id) {
        return switch (id) {

            case 100 -> w00;
            case 101 -> w01;
            case 102 -> w02;

            case 110 -> w10;
            case 111 -> w11;
            case 112 -> w12;

            case 120 -> w20;
            case 121 -> w21;
            case 122 -> w22;

            default -> null;
        };
    }

    public void renderLake(SpriteBatch batch, WorldMap world) {

        for (int row = 0; row < world.getRows(); row++) {
            for (int col = 0; col < world.getCols(); col++) {

                // int id = world.getTile(row, col);
                // TextureRegion tile = getLakeTile(id);
                TextureRegion tile = textureGrid[row][col];

                if (tile != null) {
                    batch.draw(tile, col * WorldMap.TILE_SIZE, row * WorldMap.TILE_SIZE);
                }
            }
        }
    }
}

