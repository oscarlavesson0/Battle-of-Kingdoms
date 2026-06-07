package building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for rendering all buildings on the map.
 * Loads building textures and draws each building at its tile position.
 *
 * Author:
 * JoelAxel Olsson
 */
public class BuildingRenderer {

    /** Controller used to access buildings and their data. */
    BuildingController buildingController;

    /** Cached list of buildings to render. */
    List<Building> buildings;

    /** Map of textures for each building type by name. */
    Map<String, Texture> textures;

    /**
     * Creates a renderer and loads all building textures.
     *
     * @param buildingController controller providing building data
     */
    public BuildingRenderer(BuildingController buildingController) {
        this.buildingController = buildingController;

        textures = new HashMap<>();
        textures.put("Hospital", new Texture(Gdx.files.internal("lwjgl3/assets/ui/BuildingIcons/Hospital.png")));
        textures.put("Tower",    new Texture(Gdx.files.internal("lwjgl3/assets/ui/BuildingIcons/Tower.png")));
    }

    /**
     * Renders all buildings using their textures and tile positions.
     *
     * @param batch sprite batch used for drawing
     */
    public void render(SpriteBatch batch) {
        buildings = buildingController.getBuildings();

        if (buildings != null) {
            for (Building building : buildings) {

                Texture texture = textures.get(building.getBuildingType().getName());

                float x = building.getX() * (960f / 60f);
                float y = building.getY() * (960f / 60f);

                batch.draw(texture, x, y, 25, 35);
            }
        }
    }
}
