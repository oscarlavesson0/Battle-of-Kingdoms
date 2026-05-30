package popup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Popup {
    private boolean visible = false;

    private float x;
    private float y;
    private float width;
    private float height;

    private Texture woodBackground;
    private Texture letterXIcon;

    private BitmapFont font;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontParameter parameter;


    public Popup(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        woodBackground = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/woodtexture.png"));

        generateFont(font, generator, parameter);
    }

    private void generateFont(BitmapFont font, FreeTypeFontGenerator generator, FreeTypeFontParameter parameter) {
        this.generator = new FreeTypeFontGenerator(Gdx.files.internal("lwjgl3/assets/ui/font/PixelWarden.ttf"));
        this.parameter = new FreeTypeFontParameter();
        this.parameter.size = 16;
        this.font = generator.generateFont(parameter);
        this.generator.dispose();
    }

    public void open(){visible = true;}
    public void hide(){ visible = false; }
    public boolean isVisible(){ return visible; }

    public abstract void render(ShapeRenderer shapeRenderer, SpriteBatch batch, OrthographicCamera camera);
    public abstract void handleClick(float x, float y);

}
