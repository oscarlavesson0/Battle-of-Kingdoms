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
        int seaLength = 4;
        boolean foundWater = false;
        int riverLength = 5;
        double percentage = random.nextDouble(startRange, endRange);
        int numberOfWaterTiles = (int) ((terrainGrid.length * terrainGrid[0].length) * percentage);
        int i = 0;
        while(i < numberOfWaterTiles / 2){
            foundWater = false;
            int x = random.nextInt(6, tileGrid.length - 6);
            int y = random.nextInt(6, tileGrid[0].length - 6);
            if (!(terrainGrid[x][y] instanceof Water)){
                for (int j = -1; j < 6; j++){
                    for (int k = -1; k < 2; k++){
                        if (terrainGrid[x + j][y + k] instanceof Water){
                            foundWater = true;
                            break;
                        }
                    }
                    if (foundWater){
                        break;
                    }
                }
            }
            if (!foundWater){
                for (int j = 0; j < riverLength; j++){
                    terrainGrid[x][y + j] = new Water("Water", 2);
                    i++;
                }
            }
        }
        i = 0;
        while(i < numberOfWaterTiles / 2){ // Sea tiles
            foundWater = false;
            int x = random.nextInt(2, tileGrid.length - 2);
            int y = random.nextInt(2, tileGrid[0].length - 2);

            if (!(terrainGrid[x][y] instanceof Water)){
                for (int j = 0; j < 3; j++){
                    for (int k = 0; k < 3; k++){
                        if (terrainGrid[x + j][y + k] instanceof Water){
                            foundWater = true;
                            break;
                        }
                    }
                    if (foundWater){
                        break;
                    }
                }
                //terrainGrid[x][y] = new Water("Water", 2);
                //i++;
            }
            if (!foundWater){
                for (int j = 0; j < seaLength / 2; j++){
                    for (int k = 0; k < seaLength / 2; k++){
                        terrainGrid[x + j][y + k] = new Water("Water", 2);
                        i++;
                    }
                }
            }
        }
    }

    public Terrain[][] getTerrainGrid() {
        return terrainGrid;
    }
}
