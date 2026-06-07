package GuiMainGame;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * StructureRenderer is an abstract base class for all multi‑tile structures
 * placed on the world map (e.g., bases, lakes, buildings).
 *
 * <p>A structure is represented by a 2D layout grid where each cell contains
 * an integer tile ID. Subclasses must implement {@link #getTile(int)} to map
 * these IDs to actual {@link TextureRegion} graphics.</p>
 */

public abstract class StructureRenderer {

    /** 2D grid describing the structure's tile layout.
     * @author Oscar Lavesson
     */
    protected int[][] layout;

    /**
     * Returns the full tile layout for this structure.
     *
     * @return a 2D array of tile IDs
     * @author Oscar Lavesson
     */
    public int[][] getLayout() {
        return layout;
    }

    /**
     * @return the width of the structure in tiles
     * @author Oscar Lavesson
     */
    public int getWidth() {
        return layout[0].length;
    }

    /**
     * @return the height of the structure in tiles
     * @author Oscar Lavesson
     */
    public int getHeight() {
        return layout.length;
    }

    /**
     * Returns the texture associated with a specific tile ID.
     * Subclasses must implement this to map IDs to actual graphics.
     *
     * @param id the tile ID from the layout grid
     * @return the corresponding {@link TextureRegion}, or null if invalid
     * @author Oscar Lavesson
     */    public abstract TextureRegion getTile(int id);
}

