package popup;

import base.BaseStats;
import building.BuildingController;
import building.BuildingType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Popup window that displays all available buildings the player can construct.
 * Shows building icons, names, costs, and handles selection of a building type.
 *
 * Author:
 * JoelAxel Olsson
 */
public class BuildingMenuPopup extends Popup {

    /** Popup position and size. */
    private int x;
    private int y;
    private int width;
    private int height;

    /** Icon placement coordinates. */
    private int buildingIconX;
    private int buildingIconY;

    /** List of available building types. */
    private List<BuildingType> buildings;

    /** Buttons representing each building option. */
    private List<BuildingButton> buildingButtons;

    /** Controller used to retrieve building types and create buildings. */
    private BuildingController buildingController;

    /** The base that will construct the selected building. */
    private BaseStats baseStats;

    /** Error message handling. */
    private String errorMessage;
    private boolean showError = false;

    /** Listener for when a building is chosen. */
    private BuildingChosenListener listener;

    /** Icons for building types. */
    private Texture hospitalIcon;
    private Texture towerIcon;

    /**
     * Creates a popup for selecting a building to construct.
     *
     * @param buildingController controller used to access building types
     * @param x popup X position
     * @param y popup Y position
     * @param width popup width
     * @param height popup height
     */
    public BuildingMenuPopup(BuildingController buildingController, int x, int y, int width, int height) {
        super(x, y, width, height);

        this.buildingController = buildingController;

        buildings = new ArrayList<>();
        buildingButtons = new ArrayList<>();

        buildingIconX = 330;
        buildingIconY = 400;

        this.hospitalIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/heart.png"));
        this.towerIcon    = new Texture(Gdx.files.internal("lwjgl3/assets/ui/BuildingIcons/tower.png"));

        buildings = buildingController.getBuildingTypes();

        int i = 0;
        for (BuildingType building : buildings) {
            buildingButtons.add(new BuildingButton(buildingIconX, buildingIconY - 30, 200, 200, building));
            buildingIconY -= 30;
            i++;
        }
    }

    /**
     * Sets the listener that receives the selected building type.
     *
     * @param listener callback for building selection
     */
    public void setListener(BuildingChosenListener listener) {
        this.listener = listener;
    }

    /**
     * Opens the popup for the given base.
     *
     * @param base the base that will construct the building
     */
    public void open(BaseStats base) {
        super.open();
        baseStats = base;
    }

    /**
     * Renders the list of buildings, their icons, names, and costs.
     * Also renders error messages when needed.
     */
    @Override
    public void renderContent(ShapeRenderer shapeRenderer, SpriteBatch batch,
                              OrthographicCamera camera, BitmapFont font) {

        batch.begin();
        font.setColor(Color.GOLD);

        buildingIconY = 400;

        for (BuildingType building : buildings) {

            Texture buildingIcon = hospitalIcon;
            if (building == BuildingType.Hospital) buildingIcon = hospitalIcon;
            if (building == BuildingType.Tower)    buildingIcon = towerIcon;

            batch.draw(buildingIcon, x + buildingIconX, y + buildingIconY - 30, 20, 20);
            font.draw(batch, building.getName(), x + buildingIconX + 40, y + buildingIconY - 10);
            font.draw(batch, building.getCost() + "g", x + buildingIconX + 150, y + buildingIconY - 10);

            buildingIconY -= 30;
        }

        if (showError) {
            if (batch.isDrawing()) batch.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(1f, 1f, 1f, 0.95f));
            shapeRenderer.rect(x + 60, y + height + 330, 260, 90);
            shapeRenderer.end();

            batch.begin();
            font.setColor(Color.RED);
            font.draw(batch, errorMessage, x + 70, y + height + 390);

            font.setColor(Color.BLACK);
            font.draw(batch, "[ OK ]", x + 70, y + height + 360);
        }

        batch.end();
    }

    /**
     * Displays an error message inside the popup.
     *
     * @param errorMessage the message to show
     */
    public void ShowError(String errorMessage) {
        this.errorMessage = errorMessage;
        this.showError = true;
    }

    /**
     * Handles click interactions for selecting buildings or closing error messages.
     *
     * @param screenX click X coordinate
     * @param screenY click Y coordinate
     */
    public void handleClick(float screenX, float screenY) {
        super.handleClick(screenX, screenY);

        if (!isVisible()) return;

        if (showError) {
            if (screenX >= x + 20 && screenX <= x + width + 120 &&
                screenY >= y + height - 320 && screenY <= y + height - 280) {
                showError = false;
                return;
            }

            boolean insidePopup =
                screenX >= x && screenX <= x + width &&
                    screenY >= y && screenY <= y + height;

            if (!insidePopup) showError = false;

            return;
        }

        for (BuildingButton button : buildingButtons) {
            boolean clicked =
                screenX >= button.getX() &&
                    screenX <= button.getX() + button.getWidth() &&
                    screenY >= button.getY() &&
                    screenY <= button.getY() + button.getHeight();

            if (clicked) {
                if (listener != null) {
                    listener.OnBuildingChosen(button.getBuildingType(), baseStats);
                }
                if (!showError) hide();
                break;
            }
        }
    }
}
