package popup;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BuildingInfoPopup extends Popup {
    public BuildingInfoPopup(float x, float y, float width, float height, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, OrthographicCamera camera) {
        super(x, y, width, height, shapeRenderer, spriteBatch, camera);
    }
}
