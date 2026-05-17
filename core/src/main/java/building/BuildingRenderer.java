package building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildingRenderer {

    List<Building> buildings;
    Map<String, Texture> textures;

    public BuildingRenderer(BuildingController buildingController) {
        buildings = buildingController.getBuildings();
        textures = new HashMap<>();
        textures.put("Hospital", new Texture(Gdx.files.internal("lwjgl3/assets/ui/BuildingIcons/Hospital.png")));
        textures.put("Barracks", new Texture(Gdx.files.internal("lwjgl3/assets/ui/BuildingIcons/Barracks.png")));
        textures.put("Blacksmith", new Texture(Gdx.files.internal("lwjgl3/assets/ui/BuildingIcons/Blacksmith.png")));
    }

    public void render(SpriteBatch batch) {
        for (Building building : buildings) {
            Texture texture = textures.get(building.getName());
            int x = building.getX();
            int y = building.getY();
            batch.draw(texture, x, y, 30, 30);
        }
    }
}
