package GuiMainGame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import unit.Unit;

public class UnitView {

    private Unit unit;
    private CharacterRenderer renderer;

    public UnitView(Unit unit, CharacterRenderer renderer) {
        this.unit = unit;
        this.renderer = renderer;
    }

    public void update(float delta) {
        renderer.update(delta);
    }

    public void render(SpriteBatch batch) {

        int tileX = unit.getX();
        int tileY = unit.getY();

        int px = tileX * WorldMap.TILE_SIZE;
        int py = tileY * WorldMap.TILE_SIZE;

        TextureRegion frame = renderer.getCurrentFrame();

        batch.draw(frame, px, py, 16, 16); // skala 32x32 → 16x16
    }

    public void setState(State state) {
        renderer.setState(state);
    }
}

