package popup;

import base.BaseStats;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Popup window that displays information about a Base.
 * Shows owner name, HP bar, HP text, defense value and buttons
 * for training units and creating buildings.
 *
 * Authors:
 * Stefan Rajkovic
 * Emil Hadzic
 * Enid Becarevic
 * Oscar Lavesson
 */
public class BasePopup {

    /** Camera used for projection when rendering. */
    private com.badlogic.gdx.graphics.OrthographicCamera camera;

    /** The base whose information is displayed. */
    private BaseStats base;

    /** Listener for unit training and building creation actions. */
    private TrainUnitListener listener;

    /** Popup position and size. */
    private float x, y, width, height;

    /** Font used for text rendering. */
    private BitmapFont font;

    /** ShapeRenderer used for drawing shapes. */
    private ShapeRenderer shapeRenderer;

    /** Whether the popup is currently visible. */
    private boolean visible = false;

    /** Button area for training units. */
    private float buttonX, buttonY, buttonWidth, buttonHeight;

    /** Button area for creating buildings. */
    private float buildingButtonX, buildingButtonY, buildingButtonWidth, buildingButtonHeight;

    /** Gold-colored text for UI labels. */
    private final Color GOLD = new Color(1f, 0.84f, 0.0f, 1f);

    /** UI textures. */
    private Texture woodBackground;
    private Texture frameTexture;
    private Texture heartIcon;
    private Texture shieldIcon;
    private Texture letterXIcon;
    private Texture hpBarSheet;

    /** Frames for the HP bar animation. */
    private TextureRegion[] hpBarFrames;

    /**
     * Creates a popup window for a specific Base.
     *
     * @param base the base whose information will be displayed
     * @param x popup X position
     * @param y popup Y position
     * @param width popup width
     * @param height popup height
     * @param camera camera used for rendering
     */
    public BasePopup(BaseStats base, float x, float y, float width, float height,
                     com.badlogic.gdx.graphics.OrthographicCamera camera) {

        this.base = base;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.camera = camera;

        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal("lwjgl3/assets/ui/font/PixelWarden.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 16;
        this.font = generator.generateFont(parameter);
        generator.dispose();

        this.shapeRenderer = new ShapeRenderer();

        buttonWidth = width - 210;
        buttonHeight = 40;
        buttonX = x + 20;
        buttonY = y + 20;

        buildingButtonWidth = width - 200;
        buildingButtonHeight = 40;
        buildingButtonX = x + 150;
        buildingButtonY = y + 20;

        this.woodBackground = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/woodtexture.png"));
        this.frameTexture = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/frame.png"));
        this.heartIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/heart.png"));
        this.shieldIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/shield.png"));
        this.letterXIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/letter-x.png"));
        this.hpBarSheet = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/hpbar.png"));

        this.hpBarFrames = new TextureRegion[12];
        for (int i = 0; i < 12; i++) {
            hpBarFrames[i] = new TextureRegion(hpBarSheet, i * 32 + 2, 4, 28, 24);
        }
    }

    /**
     * Makes the popup visible.
     */
    public void show() {
        visible = true;
    }

    /**
     * Hides the popup.
     */
    public void hide() {
        visible = false;
    }

    /**
     * @return true if the popup is currently visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Renders the popup window, including background, text,
     * HP bar, defense value, and action buttons.
     *
     * @param batch sprite batch used for rendering
     */
    public void render(SpriteBatch batch) {
        if (!visible) return;

        if (batch.isDrawing()) batch.end();

        batch.begin();
        batch.draw(woodBackground, x, y, width, height);
        batch.draw(frameTexture, x - 13, y - 15, width + 30, height + 30);
        batch.end();

        batch.begin();
        font.setColor(GOLD);

        font.draw(batch, "Owner: " + base.getOwner().getDisplayName(),
            x + 20, y + height - 20);

        float hpPercent = (float) base.getCurrentHp() / base.getMaxHp();
        int frameIndex = (int) ((1f - hpPercent) * 11);
        frameIndex = Math.max(0, Math.min(11, frameIndex));

        batch.draw(hpBarFrames[frameIndex], x + 60, y + height - 130, width - 80, 80);

        batch.draw(heartIcon, x + 20, y + height - 105, 30, 30);
        font.draw(batch, base.getCurrentHp() + " / " + base.getMaxHp(),
            x + 135, y + height - 80);

        batch.draw(shieldIcon, x + 20, y + height - 150, 30, 30);
        font.draw(batch, "" + base.getDefense(), x + 65, y + height - 125);

        font.draw(batch, "[ Train Units ]", buttonX, buttonY + 20);
        font.draw(batch, "[ Create Buildings ]", buttonX + 110, buttonY + 20);

        batch.draw(letterXIcon, x + width - 35, y + height - 35, 25, 25);

        batch.end();
    }

    /**
     * Handles click input for closing the popup or pressing action buttons.
     *
     * @param screenX mouse X position
     * @param screenY mouse Y position (inverted Y-axis)
     */
    public void handleClick(float screenX, float screenY) {
        if (!visible) return;

        boolean insidePopup =
            screenX >= x && screenX <= x + width &&
                screenY >= y && screenY <= y + height;

        if (!insidePopup) {
            hide();
            return;
        }

        float closeX1 = x + width - 90;
        float closeX2 = x + width - 20;
        float closeY1 = y + height - 35;
        float closeY2 = y + height - 5;

        if (screenX >= closeX1 && screenX <= closeX2 &&
            screenY >= closeY1 && screenY <= closeY2) {
            hide();
            return;
        }

        boolean buttonClicked =
            screenX >= buttonX && screenX <= buttonX + buttonWidth &&
                screenY >= buttonY && screenY <= buttonY + buttonHeight;

        if (buttonClicked && listener != null) {
            listener.onTrainUnit(base);
        }

        boolean buildingButtonClicked =
            screenX >= buildingButtonX &&
                screenX <= buildingButtonX + buildingButtonWidth &&
                screenY >= buildingButtonY &&
                screenY <= buildingButtonY + buildingButtonHeight;

        if (buildingButtonClicked && listener != null) {
            listener.onCreateBuilding(base);
        }
    }

    /**
     * Shows the popup for a specific base.
     *
     * @param base the base to display
     */
    public void show(BaseStats base) {
        this.base = base;
        this.visible = true;
    }

    /**
     * Sets the listener for unit training and building creation actions.
     *
     * @param listener callback for popup actions
     */
    public void setTrainUnitListener(TrainUnitListener listener) {
        this.listener = listener;
    }
}
