package terrain;

public class TileController {

    Tile[][] tileGrid;
    TileGenerator tileGenerator;

    TileController(int row, int col) {
        tileGrid = new Tile[16][16];
        tileGenerator = new TileGenerator(tileGrid);
        tileGrid = tileGenerator.generateTiles();


    }
}
