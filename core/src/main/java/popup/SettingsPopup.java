package popup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Settings-popup med tre knappar: Resume, Main Menu, Quit.
 */
public class SettingsPopup {

    private OrthographicCamera camera;
    private float x, y, width, height;
    private boolean visible = false;
    private SettingsActionListener listener;

    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    private Texture woodBackground;
    private Texture frameTexture;
    private Texture letterXIcon;

    private final Color GOLD = new Color(1f, 0.84f, 0.0f, 1f);

    private float resumeX, resumeY;
    private float menuX, menuY;
    private float quitX, quitY;
    private float btnW, btnH;

    public SettingsPopup(float x, float y, float width, float height, OrthographicCamera camera) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.camera = camera;

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
            Gdx.files.internal("lwjgl3/assets/ui/font/PixelWarden.ttf"));
        FreeTypeFontParameter param = new FreeTypeFontParameter();
        param.size = 18;
        font = gen.generateFont(param);
        gen.dispose();

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        woodBackground = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/woodtexture.png"));
        frameTexture   = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/frame.png"));
        letterXIcon    = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/letter-x.png"));

        // three buttos vertically
        btnW = 160;
        btnH = 35;
        float centerX = x + (width - btnW) / 2f;
        resumeX = centerX; resumeY = y + height - 90;
        menuX   = centerX; menuY   = y + height - 140;
        quitX   = centerX; quitY   = y + height - 190;
    }

    public void open(SettingsActionListener listener) {
        this.listener = listener;
        this.visible = true;
    }

    public void hide() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void show() {
        this.visible = true;
    }

    public void render() {
        if (!visible) return;

        if (batch.isDrawing()) batch.end();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        // woodbackground and fram
        batch.begin();
        batch.draw(woodBackground, x, y, width, height);
        batch.draw(frameTexture, x - 13, y - 15, width + 30, height + 30);
        batch.end();

        // button rectangles
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.2f, 0.1f, 0.05f, 1f);
        shapeRenderer.rect(resumeX, resumeY, btnW, btnH);
        shapeRenderer.rect(menuX,   menuY,   btnW, btnH);
        shapeRenderer.rect(quitX,   quitY,   btnW, btnH);
        shapeRenderer.end();

        // text and x icon
        batch.begin();
        font.setColor(GOLD);
        font.draw(batch, "Settings", x + 20, y + height - 20);

        font.draw(batch, "Resume",     resumeX + 50, resumeY + 25);
        font.draw(batch, "Main Menu",  menuX + 40,   menuY + 25);
        font.draw(batch, "Quit",       quitX + 60,   quitY + 25);

        batch.draw(letterXIcon, x + width - 35, y + height - 35, 25, 25);
        batch.end();
    }

    public void handleClick(float screenX, float realY) {
        if (!visible) return;

        // Close x
        float closeX1 = x + width - 35;
        float closeY1 = y + height - 35;
        if (screenX >= closeX1 && screenX <= closeX1 + 25 &&
            realY >= closeY1 && realY <= closeY1 + 25) {
            if (listener != null) listener.onResume();
            hide();
            return;
        }

        // Resume
        if (screenX >= resumeX && screenX <= resumeX + btnW &&
            realY >= resumeY && realY <= resumeY + btnH) {
            if (listener != null) listener.onResume();
            hide();
            return;
        }

        // Main Menu
        if (screenX >= menuX && screenX <= menuX + btnW &&
            realY >= menuY && realY <= menuY + btnH) {
            if (listener != null) listener.onMainMenu();
            return;
        }

        // Quit
        if (screenX >= quitX && screenX <= quitX + btnW &&
            realY >= quitY && realY <= quitY + btnH) {
            if (listener != null) listener.onQuit();
        }
    }

    public void dispose() {
        font.dispose();
        batch.dispose();
        woodBackground.dispose();
        frameTexture.dispose();
        letterXIcon.dispose();
        shapeRenderer.dispose();
    }
}
