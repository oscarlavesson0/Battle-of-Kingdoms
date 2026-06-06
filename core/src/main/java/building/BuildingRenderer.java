package building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildingRenderer {

    BuildingController buildingController;

    List<Building> buildings;
    Map<String, Texture> textures;

    public BuildingRenderer(BuildingController buildingController) {
        this.buildingController = buildingController;
        textures = new HashMap<>();
        textures.put("Hospital", new Texture(Gdx.files.internal("lwjgl3/assets/ui/BuildingIcons/Hospital.png")));
        textures.put("Tower", new Texture(Gdx.files.internal("lwjgl3/assets/ui/BuildingIcons/Tower.png")));
    }

    public void render(SpriteBatch batch) {
        buildings = buildingController.getBuildings();
        if (buildings != null) {
            for (Building building : buildings) {
                Texture texture = textures.get(building.getBuildingType().getName());
                float x = building.getX() * (960f / 60f);
                float y = (building.getY() * (960f / 60f));
                batch.draw(texture, x, y, 25, 35);
            }
        }
    }
}
