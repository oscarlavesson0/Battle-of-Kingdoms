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

    public void generateLand(){
        for(int i = 0; i < tileGrid.length; i++){
            for(int j = 0; j < tileGrid[i].length; j++){
                terrainGrid[i][j] = new Land("Land", "1");
            }
        }
    }

    public void generateMountain(){

    }

    public void generateWater(){

    }

    public Terrain[][] getTerrainGrid() {
        return terrainGrid;
    }
}
