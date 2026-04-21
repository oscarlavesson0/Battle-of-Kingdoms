package GuiMainGame;



public class WorldMap {

    public static final int TILE_SIZE = 16;

    private int[][] map = new int[60][60];

    public WorldMap() {
        // Fyll hela kartan med gräs (1)
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                map[row][col] = 1;
            }
        }

        //Lägger till vatten i mitten av kartan
        int centerRow = map.length / 2;
        int centerCol = map[0].length / 2;

        // Övre rad
        map[centerRow - 1][centerCol - 1] = 120; // w00
        map[centerRow - 1][centerCol]     = 121; // w01
        map[centerRow - 1][centerCol + 1] = 122; // w02

        // Mittrad
        map[centerRow][centerCol - 1] = 110; // w10
        map[centerRow][centerCol]     = 111; // w11
        map[centerRow][centerCol + 1] = 112; // w12

        // Nedre rad
        map[centerRow + 1][centerCol - 1] = 100; // w20
        map[centerRow + 1][centerCol]     = 101; // w21
        map[centerRow + 1][centerCol + 1] = 102; // w22
    }

    public int getTile(int row, int col) {
        return map[row][col];
    }

    public int getRows() {
        return map.length;
    }

    public int getCols() {
        return map[0].length;
    }
}


