package terrain;

public class TileGenerator {

    TerrainGenerator terrainGenerator;
    Tile[][] tileGrid;

    public TileGenerator(Tile[][] tileGrid) {
        terrainGenerator = new TerrainGenerator(this.tileGrid);
        this.tileGrid = tileGrid;

    }

    public Tile[][] generateTiles() {
        Tile[][] newTileGrid = tileGrid;

        return  newTileGrid;
    }
}
