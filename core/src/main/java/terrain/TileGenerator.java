package terrain;

public class TileGenerator {

    TerrainGenerator terrainGenerator;
    Tile[][] tileGrid;
    Terrain[][] terrainGrid;

    public TileGenerator(Tile[][] tileGrid) {
        this.tileGrid = tileGrid;

        terrainGenerator = new TerrainGenerator(tileGrid);
        terrainGenerator.generateLand();

        terrainGrid = terrainGenerator.getTerrainGrid();
    }

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
