package building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.BattleOfKingdoms.GameScreen;

import java.util.ArrayList;
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
        textures.put("Barracks", new Texture(Gdx.files.internal("lwjgl3/assets/ui/BuildingIcons/Barracks.png")));
        textures.put("Blacksmith", new Texture(Gdx.files.internal("lwjgl3/assets/ui/BuildingIcons/Blacksmith.png")));
        textures.put("Tower", new Texture(Gdx.files.internal("lwjgl3/assets/ui/BuildingIcons/Blacksmith.png")));
    }

    public void render(SpriteBatch batch) {
        buildings = buildingController.getBuildings();
        if (buildings != null) {
            for (Building building : buildings) {
                Texture texture = textures.get(building.getBuildingType().getName());
                float y = building.getX() * (940f / 60f);
                float x = (building.getY() * (940f / 60f));
                batch.draw(texture, x, y, 25, 25);
            }
        }
    }
}
