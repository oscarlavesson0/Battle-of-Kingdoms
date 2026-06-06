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

public class BuildingMenuPopup extends Popup {
    private int x;
    private int y;
    private int width;
    private int height;

    private int buildingIconX;
    private int buildingIconY;

    private List<BuildingType> buildings;
    private List<BuildingButton> buildingButtons;

    private BuildingController buildingController;
    private BaseStats baseStats;

    private String errorMessage;
    private boolean showError = false;

    private BuildingChosenListener listener;

    private Texture hospitalIcon;
    private Texture towerIcon;

    public BuildingMenuPopup(BuildingController buildingController, int x, int y,  int width, int height) {
        super(x, y, width, height);

        this.buildingController = buildingController;

        buildings = new ArrayList<BuildingType>();
        buildingButtons = new ArrayList<BuildingButton>();

        buildingIconX = 330;
        buildingIconY = 400;

        this.hospitalIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/heart.png"));
        this.towerIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/BuildingIcons/tower.png"));

        buildings = buildingController.getBuildingTypes();
        int i = 0;
        for (BuildingType building : buildings) {
            buildingButtons.add(new BuildingButton(buildingIconX, buildingIconY - 30, 200, 200, building));
            System.out.println(buildingButtons.get(i).getX() + ", " +  buildingButtons.get(i).getY());
            buildingIconY -= 30;
            i++;
        }
    }

    public void setListener(BuildingChosenListener listener) {
        this.listener = listener;
    }

    public void open(BaseStats base) {
        super.open();
        baseStats = base;
    }

    @Override
    public void renderContent(ShapeRenderer shapeRenderer, SpriteBatch batch, OrthographicCamera camera, BitmapFont font) {
        batch.begin();
        font.setColor(Color.GOLD);

        buildingIconY = 400;
        // Loopa genom alla buildingtyper för att visa alternativ i fönstret.
        for (BuildingType building : buildings) {
            Texture buildingIcon = hospitalIcon;
            if (building == BuildingType.Hospital){
                buildingIcon = hospitalIcon;
            }
            if (building == BuildingType.Tower){
                buildingIcon = towerIcon;
            }
            font.setColor(Color.GOLD);
            batch.draw(buildingIcon, x + buildingIconX, y + buildingIconY - 30, 20, 20);
            font.draw(batch, building.getName(), x + buildingIconX + 40, y + buildingIconY - 10);
            font.draw(batch, Integer.toString(building.getCost()) + "g", x + buildingIconX + 150, y + buildingIconY - 10);

            buildingIconY -= 30;
        }
        if (showError){
            if (batch.isDrawing()){
                batch.end();
            }

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

    public void ShowError(String errorMessage){
        this.errorMessage = errorMessage;
        this.showError = true;
    }

    public void handleClick(float screenX, float screenY){
        super.handleClick(screenX, screenY);
        if (!isVisible()){
            return;
        }
        //error
        if (showError){
            if (screenX >= x + 20 && screenX <= x + width + 120 &&
            screenY >= y + height - 320 && screenY <= y + height - 280){
                showError = false;
                return;
            }
            boolean insidePopup = screenX >= x && screenX <= x + width &&
                screenY >= y && screenY <= y + height;

            if (!insidePopup){
                showError = false;
            }
            return;
        }

        for (BuildingButton button : buildingButtons) {
            if (screenX <= button.getWidth() + button.getX() && screenX >= button.getX()
            && screenY <= button.getHeight() + button.getY() && screenY >= button.getY()){
                if (listener != null){
                    listener.OnBuildingChosen(button.getBuildingType(), baseStats);
                }
                if (!showError){
                    hide();
                }
                break;
            }
        }
    }
}
