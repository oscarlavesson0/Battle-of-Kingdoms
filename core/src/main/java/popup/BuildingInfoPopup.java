package popup;

import building.Building;
import building.BuildingType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BuildingInfoPopup extends Popup {
    Building building;
    BuildingType buildingType;

    public BuildingInfoPopup(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public void open(Building building) {
        super.open();


    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch, OrthographicCamera camera) {

    }

    @Override
    public void handleClick(float screenX, float screenY) {

    }
}
