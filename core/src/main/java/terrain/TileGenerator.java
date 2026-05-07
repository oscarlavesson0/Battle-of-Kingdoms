package terrain;

import base.BaseStats;
import base.Player;

public class TileGenerator {

    TerrainGenerator terrainGenerator;
    Tile[][] tileGrid;
    Terrain[][] terrainGrid;

    public TileGenerator(Tile[][] tileGrid) {
        this.tileGrid = tileGrid;

        terrainGenerator = new TerrainGenerator(tileGrid);
        terrainGenerator.generateLand();
        terrainGenerator.generateMountain();
        terrainGenerator.generateWater();

        terrainGrid = terrainGenerator.getTerrainGrid();
    }

    public Tile[][] generateTiles() {

        // Create tiles with terrain
        for (int row = 0; row < tileGrid.length; row++) {
            for (int col = 0; col < tileGrid[row].length; col++) {
                tileGrid[row][col] = new Tile(row, col, terrainGrid[row][col]);
            }
        }

        // Base 1 logic position
        int base1Row = 2;
        int base1Col = 2;

        BaseStats base1 = new BaseStats(new Player("1"), tileGrid[base1Row][base1Col]);

        // Mark all 5×5 tiles as belonging to this base
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                tileGrid[base1Row + r][base1Col + c].setBase(base1);
            }
        }

        // Base 2 logic position
        int base2Row = 32;
        int base2Col = 44;

        BaseStats base2 = new BaseStats(new Player("2"), tileGrid[base2Row][base2Col]);

        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                tileGrid[base2Row + r][base2Col + c].setBase(base2);
            }
        }

        return tileGrid;
    }
}
