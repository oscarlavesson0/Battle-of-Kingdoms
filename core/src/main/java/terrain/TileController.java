package terrain;

/**
 * Controls the creation and access of the games tile grid.
 * Initializes a 60x60 grid and generates tiles.
 *
 * @author JoelAxel Olsson
 */
public class TileController {

    private Tile[][] tileGrid;
    private final TileGenerator tileGenerator;

    /**
     * Creates a tile controller and generates a 60x60 tile grid.
     */
    public TileController() {
        tileGrid = new Tile[60][60];
        tileGenerator = new TileGenerator(tileGrid);
        tileGrid = tileGenerator.generateTiles();
    }

    /**
     * Returns the full tile grid used by the game.
     *
     * @return 2D array of tiles
     */
    public Tile[][] getTileGrid() {
        return tileGrid;
    }
}
