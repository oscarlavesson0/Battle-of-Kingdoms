package terrain;

import GuiMainGame.WorldMap;
import base.Base;
import base.Player;

public class TileGenerator {

    TerrainGenerator terrainGenerator;
    Tile[][] tileGrid;
    Terrain[][] terrainGrid;

    public TileGenerator(Tile[][] tileGrid) {
        terrainGenerator = new TerrainGenerator(tileGrid);
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
        //bas 1 nere i vänstra hörnet
        tileGrid[59][0].setBase(new Base(new Player("1"), tileGrid[59][0]));
        //bas 2 uppe i högre hörnet
        tileGrid[0][59].setBase(new Base(new Player("2"), tileGrid[0][59]));
        return newTileGrid;
    }
}
