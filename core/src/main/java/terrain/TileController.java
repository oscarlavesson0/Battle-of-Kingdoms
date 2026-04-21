package terrain;

import GuiMainGame.WorldMap;

public class TileController {

    Tile[][] tileGrid;
    TileGenerator tileGenerator;

    TileController(int row, int col) {
        tileGrid = new Tile[WorldMap.TILE_SIZE][WorldMap.TILE_SIZE];
        tileGenerator = new TileGenerator(tileGrid);
        tileGrid = tileGenerator.generateTiles();
    }

    /**
     * Här hämtas Tile griden för alla spelets tiles.
     * @return tileGrid
     */
    public Tile[][] getTileGrid() {
        return tileGrid;
    }
}
