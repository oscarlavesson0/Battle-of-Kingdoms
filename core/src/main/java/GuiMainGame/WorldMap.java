package GuiMainGame;

import terrain.Tile;
import terrain.TileController;
import terrain.Water;
import base.BaseStats;

public class WorldMap {

    public static final int TILE_SIZE = 16;
    public static final int WORLD_SIZE = 60;

    private int[][] map = new int[60][60];
    Tile[][] tileGrid;

    public WorldMap(TileController tileController) {
        tileGrid = tileController.getTileGrid();

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                map[row][col] = 1;
            }
        }
    }

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

    public void placeBaseStructure(StructureRenderer structure, BaseStats baseStats, int startRow, int startCol) {
        int[][] layout = structure.getLayout();

        baseStats.setPosition(tileGrid[startRow][startCol]);
        System.out.println("olace X: " + startCol + " Y: " + startRow);

        for (int r = 0; r < layout.length; r++) {
            for (int c = 0; c < layout[0].length; c++) {

                int mapRow = startRow + r;
                int mapCol = startCol + c;

                if (mapRow >= 0 && mapRow < map.length &&
                    mapCol >= 0 && mapCol < map[0].length) {

                    map[mapRow][mapCol] = layout[r][c];
                    tileGrid[mapRow][mapCol].setBase(baseStats);
                    System.out.println("map olace X: " + mapCol + " Y: " + mapRow);
                }
            }
        }
    }

    public int getTile(int row, int col) { return map[row][col]; }
    public int getRows() { return map.length; }
    public int getCols() { return map[0].length; }
}
