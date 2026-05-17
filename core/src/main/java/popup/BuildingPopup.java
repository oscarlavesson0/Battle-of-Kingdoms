package popup;

import base.BaseStats;
import building.BuildingController;
import building.BuildingType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class BuildingPopup {
    private boolean visible = false;

    private int x;
    private int y;
    private int width;
    private int height;

    private int buildingIconX;
    private int buildingIconY;

    private List<BuildingType> buildings;

    private String errorMessage;
    private boolean showError = false;

    private BitmapFont font;
    private SpriteBatch batch;

    private ShapeRenderer shapeRenderer;
    private BuildingChosenListener listener;

    private Texture woodBackground;
    private Texture frameTexture;
    private Texture hospitalIcon;
    private Texture barracksIcon;
    private Texture toolSmithIcon;
    private Texture letterXIcon;

    public BuildingPopup(int x, int y,  int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("lwjgl3/assets/ui/font/PixelWarden.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 16;
        this.font = generator.generateFont(parameter);
        generator.dispose();
        this.shapeRenderer = new ShapeRenderer();
        this.batch = new SpriteBatch();

        buildings = new ArrayList<BuildingType>();

        buildingIconX = 30;
        buildingIconY = 30;

        this.woodBackground = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/woodtexture.png"));
        this.frameTexture = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/frame.png"));
        this.hospitalIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/heart.png"));
        this.toolSmithIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/BuildingIcons/anvil.png"));
        this.barracksIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/BuildingIcons/barracks.png"));
        this.letterXIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/letter-x.png"));
    }

    public void setListener(BuildingChosenListener listener) {
        this.listener = listener;
    }

    public void open(BaseStats base, BuildingController buildingController) {
        visible = true;
        buildings = buildingController.getBuildingTypes();
    }
    public void hide(){
        visible = false;
    }
    public boolean isVisible(){
        return visible;
    }

    public void ShowError(String errorMessage){
        this.errorMessage = errorMessage;
        this.showError = true;
    }

    public void render(){
        if (!visible){
            return;
        }

        if (batch.isDrawing()){
            batch.end();
        }

        batch.begin();
        batch.draw(woodBackground, x, y, width, height);
        batch.draw(frameTexture, x - 13, y - 15, width + 30, height + 30);
        batch.end();

        batch.begin();
        font.setColor(Color.GOLD);

        buildingIconY = 230;
        // Loopa genom alla buildingtyper för att visa alternativ i fönstret.
        for (BuildingType building : buildings) {
            Texture buildingIcon = hospitalIcon;
            if (building == BuildingType.Hospital){
                buildingIcon = hospitalIcon;
            }
            if (building == BuildingType.Barracks){
                buildingIcon = barracksIcon;
            }
            if (building == BuildingType.Blacksmith){
                buildingIcon = toolSmithIcon;
            }
            font.setColor(Color.GOLD);
            batch.draw(buildingIcon, x + buildingIconX, y + buildingIconY - 30, 20, 20);
            font.draw(batch, building.getName(), x + buildingIconX + 40, y + buildingIconY - 10);
            font.draw(batch, Integer.toString(building.getCost()) + "g", x + buildingIconX + 150, y + buildingIconY - 10);
            font.setColor(Color.WHITE);
            font.draw(batch, Integer.toString(building.getConstructionTime()) + "h", x + buildingIconX + 190, y + buildingIconY - 10);

            buildingIconY -= 30;
        }

        batch.draw(letterXIcon, x + width - 50, y + height - 50, 25, 25);

        if (showError){
            if (batch.isDrawing()){
                batch.end();
            }

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(1f, 1f, 1f, 0.95f));
            shapeRenderer.rect(x + 10, y + height - 330, 260, 90);
            shapeRenderer.end();

            batch.begin();
            font.setColor(Color.RED);
            font.draw(batch, errorMessage, x + 20, y + height - 260);

            font.setColor(Color.BLACK);
            font.draw(batch, "[ OK ]", x + 20, y + height - 300);
        }
        batch.end();
    }

    public void handleClick(float screenX, float screenY){
        if (!isVisible()){
            return;
        }

        float realY = Gdx.graphics.getHeight() - screenY;

        //error
        if (showError){
            if (screenX >= x + 20 && screenX <= x + width + 120 &&
            realY >= y + height - 320 && realY <= y + height - 280){
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

        boolean insidePopup = screenX >= x && screenX <= x + width &&
            realY >= y && realY <= y + height;


        if(!insidePopup){
            hide();
            return;
        }

        float closeX1 = x + width - 90;
        float closeX2 = x + width - 20;
        float closeY1 = y + height - 35;
        float closeY2 = y + height - 5;

        if (screenX >= closeX1 && screenX <= closeX2
            && realY >= closeY1 && realY <= closeY2){
            hide();
            return;
        }

        for (BuildingType building : buildings) {

        }
    }
}
