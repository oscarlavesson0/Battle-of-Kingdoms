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
                terrainGrid[i][j] = new Land("Land", 1);
            }
        }
    }

    public void generateMountain(){
        double startRange = 0.04;
        double endRange = 0.06;
        double percentage = random.nextDouble(startRange, endRange);
        int num = (terrainGrid.length * terrainGrid[0].length) * (int)percentage;
        int i = 0;
        while(i < num){
            int x = random.nextInt(0, tileGrid.length);
            int y = random.nextInt(0, tileGrid[0].length);
            if ((x == 0 && y == 0) || (x == terrainGrid.length - 1 && y == terrainGrid[0].length - 1)){
                continue;
            }
            if (!(terrainGrid[x][y] instanceof Mountain)){
                terrainGrid[x][y] = new Mountain("Mountain", 2);
                i++;
            }
        }
    }

    public void generateWater(){

    }

    public Terrain[][] getTerrainGrid() {
        return terrainGrid;
    }
}
