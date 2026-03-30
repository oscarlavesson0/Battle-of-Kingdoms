package Tile;

import java.util.Scanner;

public class TileController {

    Tile[][] tileGrid;
    TileGenerator tileGenerator;

    public TileController() {
        tileGrid = new Tile[8][8];
        tileGenerator = new TileGenerator(tileGrid);
        tileGrid = tileGenerator.generateTiles();
        showGrid();

        String input = "";
        Scanner scan = new Scanner(System.in);
        while (input.equals("")) {
            input = scan.nextLine();
            if (input.equals("")) {
                tileGenerator = new TileGenerator(tileGrid);
                tileGrid = tileGenerator.generateTiles();
                showGrid();
            }
        }
    }

    private void showGrid() {
        for (int i = 0; i < tileGrid.length; i++) {
            for (int j = 0; j < tileGrid[i].length; j++) {
                Terrain terrain = tileGrid[i][j].getTerrain();
                System.out.print(terrain.getName() + " ");
            }
            System.out.println();
        }
    }
}
