package terrain;

import GuiMainGame.WorldMap;

public class TileController {

    Tile[][] tileGrid;
    TileGenerator tileGenerator;

    public TileController() {
        tileGrid = new Tile[60][60];
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
