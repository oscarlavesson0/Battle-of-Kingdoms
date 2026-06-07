package GuiMainGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * SpriteSheetLoader loads a spritesheet containing uniformly sized tiles with a fixed
 * margin between them. It slices the sheet into a 2D array of {@link TextureRegion}
 * objects that can be retrieved by row and column.
 *
 * <p>This loader is designed for spritesheets where each tile is 16×16 pixels and
 * separated by a 1‑pixel margin. The loader automatically calculates how many rows
 * and columns exist based on the sheet size.</p>
 *
 * <p>Typical usage:</p>
 * <pre>
 *     SpriteSheetLoader sheet = new SpriteSheetLoader();
 *     TextureRegion tile = sheet.getTile(2, 3);
 * </pre>
 */
public class SpriteSheetLoader {

    private Texture texture;
    private TextureRegion[][] tiles;

    private final int TILE_SIZE = 16;
    private final int MARGIN = 1;

    /**
     * Loads the spritesheet and slices it into individual tiles.
     *
     * <p>The spritesheet must follow this format:</p>
     * <ul>
     *     <li>Each tile is 16×16 pixels</li>
     *     <li>Each tile is separated by a 1‑pixel margin</li>
     *     <li>The sheet contains only full tiles (no partial tiles)</li>
     * </ul>
     *
     * <p>The resulting tiles are stored in a 2D array where:</p>
     * <ul>
     *     <li>First index = row</li>
     *     <li>Second index = column</li>
     * @author Oscar Lavesson
     * </ul>
     */
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

    /**
     * Retrieves a specific tile from the spritesheet.
     *
     * @param row the tile row index
     * @param col the tile column index
     * @return the corresponding {@link TextureRegion}
     * @author Oscar Lavesson
     */
    public TextureRegion getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * @return the full 2D array of all tiles in the spritesheet
     * @author Oscar Lavesson
     */
    public TextureRegion[][] getAllTiles() {
        return tiles;
    }
}

