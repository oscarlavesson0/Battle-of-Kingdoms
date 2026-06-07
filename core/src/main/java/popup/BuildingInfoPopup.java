package popup;

import building.Building;
import building.BuildingType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Popup used to display detailed information about a selected building.
 * Shows the building's name, owner, description and current health.
 *
 * Author:
 * JoelAxel Olsson
 */
public class BuildingInfoPopup extends Popup {

    /** The building whose information is displayed. */
    Building building;

    /** The type of the building being shown. */
    BuildingType buildingType;

    /**
     * Creates a popup for displaying building information.
     *
     * @param x popup X position
     * @param y popup Y position
     * @param width popup width
     * @param height popup height
     */
    public BuildingInfoPopup(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    /**
     * Opens the popup and loads the building to display.
     *
     * @param building the building whose information will be shown
     */
    public void open(Building building) {
        super.open();
        this.building = building;
        this.buildingType = building.getBuildingType();
    }

    /**
     * Renders the content inside the popup, including name, owner,
     * description and health of the building.
     *
     * @param shapeRenderer renderer for shapes
     * @param batch sprite batch for drawing text and textures
     * @param camera camera used for projection
     * @param font font used for text rendering
     */
    @Override
    public void renderContent(ShapeRenderer shapeRenderer, SpriteBatch batch,
                              OrthographicCamera camera, BitmapFont font) {

        batch.begin();
        font.setColor(Color.GOLD);

        font.draw(batch, buildingType.getName(), getX() + 20, getY() + 200);
        font.draw(batch, building.getOwner().getDisplayName(), getX() + 20, getY() + 180);
        font.draw(batch, buildingType.getDescription(), getX() + 20, getY() + 160);
        font.draw(batch, "Health: " + building.getHealth(), getX() + 20, getY() + 140);

        batch.end();
    }
}
