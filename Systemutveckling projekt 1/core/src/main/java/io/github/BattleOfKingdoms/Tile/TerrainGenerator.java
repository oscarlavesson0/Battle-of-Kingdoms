package Tile;

import java.util.Random;

public class TerrainGenerator {

    Terrain[][] terrainGrid;
    Random rand;

    public TerrainGenerator(Tile[][] tileGrid) {
        terrainGrid = new Terrain[tileGrid.length][tileGrid[0].length];
        rand = new Random();
    }

   public void generateLand() {
       for (int i = 0; i < terrainGrid.length; i++) {
           for (int j = 0; j < terrainGrid[i].length; j++) {
               terrainGrid[i][j] = new Land("L");
           }
       }
   }

   public void generateWater(){
        int i = 0;
        while (i < terrainGrid.length * terrainGrid[0].length / 10) {
            int x = rand.nextInt(0, terrainGrid.length - 1);
            int y = rand.nextInt(0, terrainGrid[0].length - 1);
            if (!(terrainGrid[x][y] instanceof Water)) {
                terrainGrid[x][y] = new Water("W");
                i++;
            }
        }
   }

   public Terrain[][] getTerrainGrid() {
        return terrainGrid;
   }
}
