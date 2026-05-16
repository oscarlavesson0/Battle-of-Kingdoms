package popup;

import base.BaseStats;
import building.BuildingController;
import building.BuildingType;
import com.badlogic.gdx.Gdx;
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

    private List<BuildingType> buildings;

    String errorMessage;
    boolean showError = false;

    private BitmapFont font;
    private SpriteBatch batch;

    private ShapeRenderer shapeRenderer;
    private BuildingChosenListener listener;

    private Texture woodBackground;
    private Texture frameTexture;
    private Texture hospitalIcon;
    private Texture barracksIcon;

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

        this.woodBackground = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/woodtexture.png"));
        this.frameTexture = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/frame.png"));
        this.hospitalIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/heart.png"));
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
        batch.draw(frameTexture, x, y, width, height);
        batch.end();

        if (showError){
            if (batch.isDrawing()){
                batch.end();
            }

        }
    }

    public void handleClick(float screenX, float screenY){
        if (!isVisible()){
            return;
        }

        float realY = Gdx.graphics.getHeight() - screenY;



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
        }
    }
}
