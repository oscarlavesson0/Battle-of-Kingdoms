package GuiMainGame;

import terrain.Tile;
import terrain.TileController;
import terrain.Water;
import base.BaseStats;


/**
 * WorldMap represents the logical tile map of the game world.
 * It stores:
 * <ul>
 *     <li>A 2D integer array representing tile graphics (IDs)</li>
 *     <li>A reference to the {@link TileController}'s tile grid for terrain and structure data</li>
 * </ul>
 */
public class WorldMap {

    public static final int TILE_SIZE = 16;
    public static final int WORLD_SIZE = 60;

    private int[][] map = new int[60][60];
    Tile[][] tileGrid;

    /**
     * Creates a new WorldMap and initializes all tiles to a default value (grass = 1).
     *
     * @param tileController the controller providing access to the tile grid
     * @author JoelAxel Olsson
     * @author Oscar Lavesson
     * @author Enid Becarevic
     */
    public WorldMap(TileController tileController) {
        tileGrid = tileController.getTileGrid();

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                map[row][col] = 1;
            }
        }
    }

    /**
     * Places a lake or other structure on the map, centered around the given tile.
     * Updates both:
     * <ul>
     *     <li>The visual map array (tile IDs)</li>
     *     <li>The tileGrid terrain (sets {@link Water} so units cannot walk on it)</li>
     * </ul>
     *
     * @param structure the structure renderer providing tile layout
     * @param centerRow the center row of the structure
     * @param centerCol the center column of the structure
     * @author Oscar Lavesson
     * @author Enid Becarevic
     */
    public void placeLakeStructure(StructureRenderer structure, int centerRow, int centerCol) {
        int[][] layout = structure.getLayout();

        for (int r = 0; r < layout.length; r++) {
            for (int c = 0; c < layout[0].length; c++) {

                int mapRow = centerRow - layout.length / 2 + r;
                int mapCol = centerCol - layout[0].length / 2 + c;

                if (mapRow >= 0 && mapRow < map.length &&
                    mapCol >= 0 && mapCol < map[0].length) {

                    // Sätt grafik
                    map[mapRow][mapCol] = layout[r][c];

                    // Sätt Water-terrain i tileGrid så units inte kan gå här
                    tileGrid[mapRow][mapCol].setTerrain(new Water("Water", 2));
                }
            }
        }
    }

    /**
     * Places a base structure on the map at a fixed starting tile.
     * Updates:
     * <ul>
     *     <li>The visual map array (tile IDs)</li>
     *     <li>The tileGrid to assign the base to each tile it occupies</li>
     *     <li>The BaseStats position reference</li>
     * </ul>
     *
     * @param structure the structure renderer providing tile layout
     * @param baseStats the base stats object representing the base
     * @param startRow  the top-left row where the base begins
     * @param startCol  the top-left column where the base begins
     * @author Enid Becarevic
     */    public void placeBaseStructure(StructureRenderer structure, BaseStats baseStats, int startRow, int startCol) {
        int[][] layout = structure.getLayout();

        baseStats.setPosition(tileGrid[startCol][startRow]);
        System.out.println("olace X: " + startCol + " Y: " + startRow);

        for (int r = 0; r < layout.length; r++) {
            for (int c = 0; c < layout[0].length; c++) {

                int mapRow = startRow + r;
                int mapCol = startCol + c;

                if (mapRow >= 0 && mapRow < map.length &&
                    mapCol >= 0 && mapCol < map[0].length) {

                    map[mapRow][mapCol] = layout[r][c];
                    tileGrid[mapRow][mapCol].setBase(baseStats);
                }
            }
        }
    }

    /**
     * Returns the tile ID at the given map position.
     *
     * @param row the row index
     * @param col the column index
     * @return the tile ID
     * @author Oscar Lavesson
     */
    public int getTile(int row, int col) { return map[row][col]; }
    /** @return number of rows in the map
     * @author Oscar Lavesson*/
    public int getRows() { return map.length; }
    /** @return number of columns in the map
     * @author Oscar Lavesson*/
    public int getCols() { return map[0].length; }
}
