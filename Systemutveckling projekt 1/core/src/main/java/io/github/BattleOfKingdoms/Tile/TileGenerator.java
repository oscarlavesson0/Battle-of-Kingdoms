import Tile.Tile;

public class TileGenerator {

    TerrainGenerator terrainGenerator;
    ResourceGenerator resourceGenerator;
    Tile[][] tileGrid;
    Terrain[][] terrainGrid;

    public TileGenerator(Tile[][] tileGrid) {
        terrainGenerator = new TerrainGenerator(tileGrid);
        resourceGenerator = new ResourceGenerator();
        this.tileGrid = tileGrid;

        terrainGenerator.generateLand();
        terrainGenerator.generateWater();
        terrainGrid = terrainGenerator.getTerrainGrid();
    }

    public Tile[][] generateTiles() {
        Tile[][] newtileGrid = tileGrid;
        for (int i = 0; i < tileGrid.length; i++) {
            for(int j = 0; j < tileGrid[i].length; j++) {
                tileGrid[i][j] = new Tile(i, j, terrainGrid[i][j]);
            }
        }
        return newtileGrid;
    }
}
