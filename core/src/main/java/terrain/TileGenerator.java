package terrain;

public class TileGenerator {

    TerrainGenerator terrainGenerator;
    Tile[][] tileGrid;
    Terrain[][] terrainGrid;

    public TileGenerator(Tile[][] tileGrid) {
        terrainGenerator = new TerrainGenerator(this.tileGrid);
        this.tileGrid = tileGrid;

        terrainGenerator.generateLand();
        terrainGenerator.generateMountain();
        terrainGenerator.generateWater();
        terrainGrid = terrainGenerator.getTerrainGrid();

    }

    public Tile[][] generateTiles() {
        Tile[][] newTileGrid = tileGrid;

        for (int i = 0; i < tileGrid.length; i++) {
            for (int j = 0; j < tileGrid[i].length; j++) {
                tileGrid[i][j] = new Tile(i, j, terrainGrid[i][j]);
            }
        }
        return  newTileGrid;
    }
}
