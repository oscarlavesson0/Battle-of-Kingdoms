package terrain;

import java.util.Random;

/**
 * Generates terrain for the map and stores it in a terrain grid.
 * Currently supports generating a full land map.
 *
 * @author JoelAxel Olsson
 */
public class TerrainGenerator {

    private final Terrain[][] terrainGrid;
    private final Tile[][] tileGrid;
    private final Random random;

    /**
     * Creates a terrain generator for the given tile grid.
     *
     * @param tileGrid the grid of tiles to attach terrain to
     */
    public TerrainGenerator(Tile[][] tileGrid) {
        this.terrainGrid = new Terrain[tileGrid.length][tileGrid[0].length];
        this.tileGrid = tileGrid;
        this.random = new Random();
    }

    /**
     * Fills the entire grid with terrain.
     */
    public void generateLand() {
        for (int row = 0; row < tileGrid.length; row++) {
            for (int col = 0; col < tileGrid[row].length; col++) {
                terrainGrid[row][col] = new Land("Land", 1);
            }
        }
    }

    /**
     * Returns the generated terrain grid.
     *
     * @return 2D array of terrain
     */
    public Terrain[][] getTerrainGrid() {
        return terrainGrid;
    }
}
