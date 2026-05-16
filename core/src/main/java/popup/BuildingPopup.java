package popup;

import base.BaseStats;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BuildingPopup {
    private boolean visible = false;

    private int x;
    private int y;
    private int width;
    private int height;

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

        this.woodBackground = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/woodtexture.png"));
        this.frameTexture = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/frame.png"));
        this.hospitalIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/heart.png"));
    }

    public void setListener(BuildingChosenListener listener) {
        this.listener = listener;
    }

    public void open(BaseStats base){
        visible = true;
    }
    public void hide(){
        visible = false;
    }
    public boolean isVisible(){
        return visible;
    }

    public void render(){
        if (!visible){
            return;
        }

        if (batch.isDrawing()){
            batch.end();
        }
    }
}
