package popup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Abstract base class for all popup windows in the game.
 * Handles visibility, background rendering, close button,
 * and click detection. Subclasses implement their own content.
 *
 * Author:
 * JoelAxel Olsson
 */
public abstract class Popup {

    /** Whether the popup is currently visible. */
    private boolean visible = false;

    /** Popup position and size. */
    private float x;
    private float y;
    private float width;
    private float height;

    /** Background textures and close icon. */
    private Texture woodBackground;
    private Texture frameTexture;
    private Texture letterXIcon;

    /** Font used for popup text. */
    private BitmapFont font;

    /** Font generator used to load the font. */
    private FreeTypeFontGenerator generator;

    /** Font parameter settings. */
    private FreeTypeFontParameter parameter;

    /**
     * Creates a popup with the given position and size.
     *
     * @param x popup X position
     * @param y popup Y position
     * @param width popup width
     * @param height popup height
     */
    public Popup(float x, float y, float width, float height) {
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;

        woodBackground = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/woodtexture.png"));
        frameTexture   = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/frame.png"));
        letterXIcon    = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/letter-x.png"));

        generateFont();
    }

    /**
     * Generates the font used for popup text.
     */
    private void generateFont() {
        this.generator = new FreeTypeFontGenerator(Gdx.files.internal("lwjgl3/assets/ui/font/PixelWarden.ttf"));
        this.parameter = new FreeTypeFontParameter();
        this.parameter.size = 16;
        this.font = generator.generateFont(parameter);
        this.generator.dispose();
    }

    /** Makes the popup visible. */
    public void open() { visible = true; }

    /** Hides the popup. */
    public void hide() { visible = false; }

    /** @return true if the popup is visible */
    public boolean isVisible() { return visible; }

    /**
     * Renders the popup background, frame, close button,
     * and then calls the subclass-specific content renderer.
     *
     * @param shapeRenderer renderer for shapes
     * @param batch sprite batch for drawing textures and text
     * @param camera camera used for projection
     */
    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch, OrthographicCamera camera) {
        if (!visible) return;

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        if (batch.isDrawing()) batch.end();

        renderWoodBackground(batch);
        renderXIcon(batch);
        renderContent(shapeRenderer, batch, camera, font);
    }

    /**
     * Draws the wooden background and frame.
     */
    private void renderWoodBackground(SpriteBatch batch) {
        batch.begin();
        batch.draw(woodBackground, x, y, width, height);
        batch.draw(frameTexture, x - 13, y - 15, width + 30, height + 30);
        batch.end();
    }

    /**
     * Draws the close (X) icon in the top-right corner.
     */
    private void renderXIcon(SpriteBatch batch) {
        batch.begin();
        batch.draw(letterXIcon, x + width - 50, y + height - 50, 25, 25);
        batch.end();
    }

    /**
     * Subclasses implement this to draw their specific content.
     *
     * @param shapeRenderer shape renderer
     * @param batch sprite batch
     * @param camera camera used for projection
     * @param font font used for text
     */
    public abstract void renderContent(ShapeRenderer shapeRenderer, SpriteBatch batch,
                                       OrthographicCamera camera, BitmapFont font);

    /**
     * Handles click detection for closing the popup or clicking inside it.
     *
     * @param screenX click X coordinate
     * @param screenY click Y coordinate
     */
    public void handleClick(float screenX, float screenY) {
        if (!isVisible()) return;

        boolean insidePopup =
            screenX >= getX() && screenX <= getX() + width &&
                screenY >= getY() && screenY <= getY() + height;

        if (!insidePopup) {
            hide();
            return;
        }

        float closeX1 = getX() + width - 90;
        float closeX2 = getX() + width - 20;
        float closeY1 = getY() + height - 35;
        float closeY2 = getY() + height - 5;

        boolean closeClicked =
            screenX >= closeX1 && screenX <= closeX2 &&
                screenY >= closeY1 && screenY <= closeY2;

        if (closeClicked) hide();
    }

    /** @return popup X position */
    public float getX() { return x; }

    /** @return popup Y position */
    public float getY() { return y; }

    /** @return popup height */
    public float getHeight() { return height; }

    /** @return popup width */
    public float getWidth() { return width; }
}
