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
    private Texture frameTexture;
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
        frameTexture = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/frame.png"));
        letterXIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/letter-x.png"));

        generateFont();
    }

    private void generateFont() {
        this.generator = new FreeTypeFontGenerator(Gdx.files.internal("lwjgl3/assets/ui/font/PixelWarden.ttf"));
        this.parameter = new FreeTypeFontParameter();
        this.parameter.size = 16;
        this.font = generator.generateFont(parameter);
        this.generator.dispose();
    }

    public void open(){visible = true;}
    public void hide(){ visible = false; }
    public boolean isVisible(){ return visible; }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch, OrthographicCamera camera){
        if (!visible){
            return;
        }
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        if (batch.isDrawing()){
            batch.end();
        }

        renderWoodBackground(batch);
        renderXIcon(batch);
        renderContent(shapeRenderer, batch, camera, font);
    }

    private void renderWoodBackground(SpriteBatch batch){
        batch.begin();
        batch.draw(woodBackground, x, y, width, height);
        batch.draw(frameTexture, x - 13, y - 15, width + 30, height + 30);
        batch.end();
    }

    private void renderXIcon(SpriteBatch batch){
        batch.begin();
        batch.draw(letterXIcon, x + width - 50, y + height - 50, 25, 25);
        batch.end();
    }

    public abstract void renderContent(ShapeRenderer shapeRenderer, SpriteBatch batch, OrthographicCamera camera, BitmapFont font);

    public void handleClick(float screenX, float screenY){
        if (!isVisible()) {
            return;
        }
        boolean insidePopup = screenX >= getX() && screenX <= getX() + width &&
            screenY >= getY() && screenY <= getY() + height;

        if(!insidePopup){
            hide();
            return;
        }
        float closeX1 = getX() + width - 90;
        float closeX2 = getX() + width - 20;
        float closeY1 = getY() + height - 35;
        float closeY2 = getY() + height - 5;

        if (screenX >= closeX1 && screenX <= closeX2
            && screenY >= closeY1 && screenY <= closeY2){
            hide();
        }
    }

    public float getX(){ return x; }
    public float getY(){ return y; }

    public float getHeight() {
        return height;
    }
    public float getWidth() {
        return width;
    }
}
