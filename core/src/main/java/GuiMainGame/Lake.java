package GuiMainGame;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Lake extends StructureRenderer {

    private TextureRegion topLeft, topMid, topRight;
    private TextureRegion midLeft, midMid, midRight;
    private TextureRegion botLeft, botMid, botRight;

    public Lake(SpriteSheetLoader sheet, int size) {
        topLeft  = sheet.getTile(0, 2);
        topMid   = sheet.getTile(0, 3);
        topRight = sheet.getTile(0, 4);
        midLeft  = sheet.getTile(1, 2);
        midMid   = sheet.getTile(1, 3);
        midRight = sheet.getTile(1, 4);
        botLeft  = sheet.getTile(2, 2);
        botMid   = sheet.getTile(2, 3);
        botRight = sheet.getTile(2, 4);

        layout = buildLayout(size);
    }

    private int[][] buildLayout(int size) {
        int[][] grid = new int[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (row == 0 && col == 0)             grid[row][col] = 100; // TL
                else if (row == 0 && col == size - 1) grid[row][col] = 102; // TR
                else if (row == size - 1 && col == 0) grid[row][col] = 120; // BL
                else if (row == size - 1 && col == size - 1) grid[row][col] = 122; // BR
                else if (row == 0)                    grid[row][col] = 101; // TM
                else if (row == size - 1)             grid[row][col] = 121; // BM
                else if (col == 0)                    grid[row][col] = 110; // ML
                else if (col == size - 1)             grid[row][col] = 112; // MR
                else                                  grid[row][col] = 111; // MM
            }
        }
        return grid;
    }

    @Override
    public TextureRegion getTile(int id) {
        return switch (id) {
            case 120 -> topLeft;
            case 121 -> topMid;
            case 122 -> topRight;
            case 110 -> midLeft;
            case 111 -> midMid;
            case 112 -> midRight;
            case 100 -> botLeft;
            case 101 -> botMid;
            case 102 -> botRight;
            default  -> null;
        };
    }
}





