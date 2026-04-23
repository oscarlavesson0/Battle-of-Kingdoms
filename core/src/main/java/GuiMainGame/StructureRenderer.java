package GuiMainGame;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class StructureRenderer {

    protected int[][] layout;

    public int[][] getLayout() {
        return layout;
    }

    public int getWidth() {
        return layout[0].length;
    }

    public int getHeight() {
        return layout.length;
    }

    // Varje struktur måste kunna returnera rätt tile
    public abstract TextureRegion getTile(int id);
}

