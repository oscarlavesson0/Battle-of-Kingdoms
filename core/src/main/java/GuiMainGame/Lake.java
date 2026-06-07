package GuiMainGame;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Represents a lake structure on the world map. A Lake is a multi‑tile structure
 * composed of 3×3 water tiles (or larger, depending on the chosen size).
 *
 * <p>The lake uses a tile layout inherited from {@link StructureRenderer}, where
 * each tile ID corresponds to a specific water texture (corner, edge, or center).
 * The lake can be generated in any square size, and the layout is automatically
 * constructed based on the provided size.</p>
 *
 * <p>Tile ID mapping:</p>
 * <ul>
 *     <li>100–102 → bottom row (left, mid, right)</li>
 *     <li>110–112 → middle row (left, mid, right)</li>
 *     <li>120–122 → top row (left, mid, right)</li>
 * </ul>
 */

public class Lake extends StructureRenderer {

    private TextureRegion topLeft, topMid, topRight;
    private TextureRegion midLeft, midMid, midRight;
    private TextureRegion botLeft, botMid, botRight;

    /**
     * Creates a new Lake structure using tiles from the provided spritesheet.
     *
     * @param sheet the spritesheet loader used to retrieve water tile textures
     * @param size  the size of the lake (width and height in tiles)
     * @author Oscar Lavesson
     */

    public Lake(SpriteSheetLoader sheet, int size) {
        topLeft  = sheet.getTile(0, 2);
        topMid   = sheet.getTile(0, 3);
        topRight = sheet.getTile(0, 4);
        midLeft  = sheet.getTile(1, 2);
        midMid   = sheet.getTile(1, 3);
        midRight = sheet.getTile(1, 4);
        botLeft  = sheet.getTile(2, 2);
        botMid   = sheet.getTile(2, 3);
        botRight = sheet.getTile(2, 4);

        layout = buildLayout(size);
    }

    /**
     * Builds the tile ID layout for the lake. The layout determines which tile
     * texture is used at each position (corner, edge, or center).
     *
     * <p>The IDs are arranged as follows:</p>
     * <ul>
     *     <li>Top row:    120 (TL), 121 (TM), 122 (TR)</li>
     *     <li>Middle row: 110 (ML), 111 (MM), 112 (MR)</li>
     *     <li>Bottom row: 100 (BL), 101 (BM), 102 (BR)</li>
     * </ul>
     *
     * @param size the width/height of the lake in tiles
     * @return a 2D array representing the tile ID layout
     * @author Oscar Lavesson
     */
    private int[][] buildLayout(int size) {
        int[][] grid = new int[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (row == 0 && col == 0)             grid[row][col] = 100; // TL
                else if (row == 0 && col == size - 1) grid[row][col] = 102; // TR
                else if (row == size - 1 && col == 0) grid[row][col] = 120; // BL
                else if (row == size - 1 && col == size - 1) grid[row][col] = 122; // BR
                else if (row == 0)                    grid[row][col] = 101; // TM
                else if (row == size - 1)             grid[row][col] = 121; // BM
                else if (col == 0)                    grid[row][col] = 110; // ML
                else if (col == size - 1)             grid[row][col] = 112; // MR
                else                                  grid[row][col] = 111; // MM
            }
        }
        return grid;
    }

    /**
     * Returns the correct water tile texture for the given tile ID.
     *
     * @param id the tile ID from the lake layout
     * @return the corresponding TextureRegion, or null if the ID is invalid
     * @author Oscar Lavesson
     */
    @Override
    public TextureRegion getTile(int id) {
        return switch (id) {
            case 120 -> topLeft;
            case 121 -> topMid;
            case 122 -> topRight;
            case 110 -> midLeft;
            case 111 -> midMid;
            case 112 -> midRight;
            case 100 -> botLeft;
            case 101 -> botMid;
            case 102 -> botRight;
            default  -> null;
        };
    }
}





