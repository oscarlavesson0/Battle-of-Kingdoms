package popup;

import building.Building;
import building.BuildingType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
        this.building = building;
        this.buildingType = building.getBuildingType();
    }

    @Override
    public void renderContent(ShapeRenderer shapeRenderer, SpriteBatch batch, OrthographicCamera camera, BitmapFont font) {
        batch.begin();
        font.setColor(Color.GOLD);
        font.draw(batch, buildingType.getName(), getX() + 10, getY() - 30);

    }
}
