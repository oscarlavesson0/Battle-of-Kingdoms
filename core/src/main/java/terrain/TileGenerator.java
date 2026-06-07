package terrain;

/**
 * Generates the games tile grid by assigning terrain to each tile.
 *
 * @author Stefan Rajkovic
 * @author JoelAxel Olsson
 */
public class TileGenerator {

    private final TerrainGenerator terrainGenerator;
    private final Tile[][] tileGrid;
    private final Terrain[][] terrainGrid;

    /**
     * Creates a tile generator for the given tile grid.
     *
     * @param tileGrid the grid to populate with tiles
     */
    public TileGenerator(Tile[][] tileGrid) {
        this.tileGrid = tileGrid;

        terrainGenerator = new TerrainGenerator(tileGrid);
        terrainGenerator.generateLand();

        terrainGrid = terrainGenerator.getTerrainGrid();
    }

    /**
     * Generates tiles for the entire grid using the terrain layout.
     *
     * @return the populated tile grid
     */
    public Tile[][] generateTiles() {

        // Create tiles with terrain
        for (int row = 0; row < tileGrid.length; row++) {
            for (int col = 0; col < tileGrid[row].length; col++) {
                tileGrid[row][col] = new Tile(row, col, terrainGrid[row][col]);
            }
        }

        return tileGrid;
    }
}
