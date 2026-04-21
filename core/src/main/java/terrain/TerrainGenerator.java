package terrain;

import java.util.Random;

public class TerrainGenerator {

    Terrain[][] terrainGrid;
    Tile[][] tileGrid;
    Random random;

    public TerrainGenerator(Tile[][] tileGrid) {
        terrainGrid = new Terrain[tileGrid.length][tileGrid[0].length];
        this.tileGrid = tileGrid;
        random = new Random();
    }

}
