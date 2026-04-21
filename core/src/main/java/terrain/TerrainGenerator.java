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
        double startRange = 0.00;
        double endRange = 0.02;
        double percentage = random.nextDouble(startRange, endRange);
        int numberOfMountains = (int) ((terrainGrid.length * terrainGrid[0].length) * percentage);
        int i = 0;
        while(i < numberOfMountains){
            int x = random.nextInt(0, tileGrid.length);
            int y = random.nextInt(0, tileGrid[0].length);
            if ((x == 0 && y == 0) || (x == terrainGrid.length - 1 && y == terrainGrid[0].length - 1)){
                continue;
            }
            if (!(terrainGrid[x][y] instanceof Mountain)){
                terrainGrid[x][y] = new Mountain("Mountain", 3);
                i++;
            }
        }
    }

    public void generateWater(){
        double startRange = 0.06;
        double endRange = 0.1;
        double percentage = random.nextDouble(startRange, endRange);
        int numberOfWaterTiles = (int) ((terrainGrid.length * terrainGrid[0].length) * percentage);
        int i = 0;
        while(i < numberOfWaterTiles){
            int x = random.nextInt(0, tileGrid.length);
            int y = random.nextInt(0, tileGrid[0].length);
            if ((x == 0 && y == 0) || (x == terrainGrid.length - 1 && y == terrainGrid[0].length - 1)){
                continue;
            }
            if (!(terrainGrid[x][y] instanceof Water)){
                terrainGrid[x][y] = new Water("Water", 2);
                i++;
            }
        }
    }

    public Terrain[][] getTerrainGrid() {
        return terrainGrid;
    }
}
