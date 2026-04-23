package GuiMainGame;

import terrain.Tile;
import terrain.TileController;

public class WorldMap {

    public static final int TILE_SIZE = 16;

    private int[][] map = new int[60][60];
    Tile[][] tileGrid;

    public WorldMap(TileController tileController) {

        tileGrid = tileController.getTileGrid();

        // Fyll kartan baserat på terräng-ID
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                map[row][col] = 1; // gräs
            }
        }
    }

    public void placeStructure(StructureRenderer structure, int centerRow, int centerCol) {

        int[][] layout = structure.getLayout();

        for (int r = 0; r < layout.length; r++) {
            for (int c = 0; c < layout[0].length; c++) {

                int mapRow = centerRow - layout.length/2 + r;
                int mapCol = centerCol - layout[0].length/2 + c;

                if (mapRow >= 0 && mapRow < map.length &&
                    mapCol >= 0 && mapCol < map[0].length) {

                    map[mapRow][mapCol] = layout[r][c];
                }
            }
        }
    }


    public int getTile(int row, int col) { return map[row][col]; }
    public int getRows() { return map.length; }
    public int getCols() { return map[0].length; }
}




