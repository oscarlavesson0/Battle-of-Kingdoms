package GuiMainGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Base extends StructureRenderer {

    private TextureRegion[][] parts;
    private Animation<TextureRegion> explosionAnim;
    private float explosionTime = 0f;
    private boolean exploding = false;

    public Base(SpriteSheetLoader sheet) {

        Texture castleTexture = new Texture("lwjgl3/assets/ui/Castle Walls/Castle_door.png");

        parts = TextureRegion.split(castleTexture, 16, 16);

        layout = new int[][]{
            {1012, 1013, 1014, 1015},
            {1004, 1009, 1010, 1007},
            {1008, 1005, 1006, 1011},
            {1000, 1001, 1002, 1003}
        };
    }
    @Override
    public TextureRegion getTile(int id) {
        return switch (id) {
            case 1000 -> parts[0][0];
            case 1001 -> parts[0][1];
            case 1002 -> parts[0][2];
            case 1003 -> parts[0][3];

            case 1004 -> parts[1][0];
            case 1005 -> parts[1][1];
            case 1006 -> parts[1][2];
            case 1007 -> parts[1][3];

            case 1008 -> parts[2][0];
            case 1009 -> parts[2][1];
            case 1010 -> parts[2][2];
            case 1011 -> parts[2][3];

            case 1012 -> parts[3][0];
            case 1013 -> parts[3][1];
            case 1014 -> parts[3][2];
            case 1015 -> parts[3][3];

            default -> null;
        };
    }
    private void loadExplosion() {

        Texture explosionSheet = new Texture("lwjgl3/assets/ui/exp3_0.png");

        int frameWidth = explosionSheet.getWidth() / 4;  // 4 kolumner
        int frameHeight = explosionSheet.getHeight() / 3; // 3 rader

        TextureRegion[][] tmp = TextureRegion.split(explosionSheet, frameWidth, frameHeight);

        TextureRegion[] frames = new TextureRegion[12];
        int index = 0;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                frames[index++] = tmp[row][col];
            }
        }

        explosionAnim = new Animation<>(0.08f, frames); // 0.08f = lagom hastighet
        explosionAnim.setPlayMode(Animation.PlayMode.NORMAL);

    }
    public void explode() {
        exploding = true;
        explosionTime = 0f;
    }
}

