package building;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class BuildingRenderer {

    SpriteBatch batch;

    List<Building> buildings;

    public BuildingRenderer(BuildingController buildingController) {
        buildings = buildingController.getBuildings();

    }

    public void render(SpriteBatch batch) {
        for (Building building : buildings) {
            Texture texture = new Texture(building.getName());
            int x = building.getX();
            int y = building.getY();
            batch.draw(texture, x, y, 30, 30);
        }
    }
}
