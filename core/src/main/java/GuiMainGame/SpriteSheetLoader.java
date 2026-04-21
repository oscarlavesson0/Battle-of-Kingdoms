package GuiMainGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteSheetLoader {

    private Texture texture;
    private TextureRegion[][] tiles;

    private final int TILE_SIZE = 16;
    private final int MARGIN = 1;

    public SpriteSheetLoader() {
        texture = new Texture("lwjgl3/assets/ui/roguelikeSheet_transparent.png");

        int sheetWidth = texture.getWidth();
        int sheetHeight = texture.getHeight();

        int cols = sheetWidth / (TILE_SIZE + MARGIN);
        int rows = sheetHeight / (TILE_SIZE + MARGIN);

        tiles = new TextureRegion[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                int x = col * (TILE_SIZE + MARGIN);
                int y = row * (TILE_SIZE + MARGIN);

                tiles[row][col] = new TextureRegion(texture, x, y, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    public TextureRegion getTile(int row, int col) {
        return tiles[row][col];
    }

    public TextureRegion[][] getAllTiles() {
        return tiles;
    }
}

