package GuiMainGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CharacterRenderer {

    private Texture sheet;
    private TextureRegion knight;

    public CharacterRenderer() {
        sheet = new Texture("lwjgl3/assets/ui/knight Hero Platfomer/Walk.png");

        // Klipp ut gubben längst till vänster
        knight = new TextureRegion(sheet, 0, 0, 64, 64);
    }

    public void render(SpriteBatch batch, int x, int y) {
        batch.draw(knight, x, y);
    }
}


