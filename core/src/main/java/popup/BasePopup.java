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
 * Shows owner name, HP bar, HP text, defense value and a button.
 */
public class BasePopup {
    private com.badlogic.gdx.graphics.OrthographicCamera camera;


    private BaseStats base;

    private TrainUnitListener listener;

    // Popup position and size
    private float x, y, width, height;

    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    private boolean visible = false;

    // Button area
    private float buttonX, buttonY, buttonWidth, buttonHeight;
    // Create Buildings button area
    private float buildingButtonX, buildingButtonY, buildingButtonWidth, buildingButtonHeight;

    private final Color GOLD = new Color(1f, 0.84f, 0.0f, 1f);

    private Texture woodBackground;
    private Texture frameTexture;
    private Texture heartIcon;
    private Texture shieldIcon;
    private Texture letterXIcon;
    private Texture hpBarSheet;
    private TextureRegion[] hpBarFrames;

    /**
     * Creates a popup window for a specific Base.
     * @param base The base whose information will be displayed.
     * @param x Popup X position
     * @param y Popup Y position
     * @param width Popup width
     * @param height Popup height
     */
    public BasePopup(BaseStats base, float x, float y, float width, float height, com.badlogic.gdx.graphics.OrthographicCamera camera) {
        this.base = base;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.camera = camera;

        //font = new BitmapFont();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("lwjgl3/assets/ui/font/PixelWarden.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 16;
        this.font = generator.generateFont(parameter);
        generator.dispose();
        shapeRenderer = new ShapeRenderer();

        // Button size and position
        buttonWidth = width - 210;
        buttonHeight = 40;
        buttonX = x + 20;
        buttonY = y + 20;
        // Building button size and position
        buildingButtonWidth = width - 200;
        buildingButtonHeight = 40;
        buildingButtonX = x + 150;
        buildingButtonY = y + 20;

        this.woodBackground = new Texture (Gdx.files.internal("lwjgl3/assets/ui/StatIcons/woodtexture.png"));
        this.frameTexture = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/frame.png"));
        this.heartIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/heart.png"));
        this.shieldIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/shield.png"));
        this.letterXIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/letter-x.png"));
        this.hpBarSheet = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/hpbar.png"));

// Klipp upp spritesheet:en i 12 frames (32x32 vardera)
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
     * Checks if the popup is currently visible.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Renders the popup window, including background, text, HP bar and button.
     */
    public void render(SpriteBatch batch) {
        if (!visible) return;

        // Ensure batch is not running before using ShapeRenderer
        if (batch.isDrawing()) {
            batch.end();
        }
        batch.begin();
        batch.draw(woodBackground, x, y, width, height);
        batch.draw(frameTexture, x - 13, y - 15, width + 30, height + 30);
        batch.end();
        /*shapeRenderer.setProjectionMatrix(camera.combined);
        // Draw background and HP bar with ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // White background
        //shapeRenderer.setColor(new Color(1f, 1f, 1f, 0.95f));
        //shapeRenderer.rect(x, y, width, height);

        // HP bar background
        shapeRenderer.setColor(0.2f, 0.1f, 0.05f, 1f);
        shapeRenderer.rect(x + 60, y + height - 120, width - 80, 20);

        // HP bar fill
        float hpPercent = (float) base.getCurrentHp() / base.getMaxHp();
        shapeRenderer.setColor(0.2f, 0.7f, 0.2f, 1f);
        shapeRenderer.rect(x + 60, y + height - 120, (width - 80) * hpPercent, 20);

        shapeRenderer.end();*/

        // Draw text with SpriteBatch
        batch.begin();
        font.setColor(GOLD);

        // Owner text
        //font.setColor(Color.BLACK);
        font.draw(batch, "Owner: " + base.getOwner().getDisplayName(), x + 20, y + height - 20);

        // HP-bar

        float hpPercent = (float) base.getCurrentHp() / base.getMaxHp();
        int frameIndex = (int) ((1f - hpPercent) * 11);
        frameIndex = Math.max(0, Math.min(11, frameIndex));
        batch.draw(hpBarFrames[frameIndex], x + 60, y + height - 130, width - 80, 80);

        // HP ikon
        //font.setColor(Color.GREEN);
        batch.draw(heartIcon, x + 20, y + height - 105, 30, 30);
        font.draw(batch, base.getCurrentHp() + " / " + base.getMaxHp(),
            x + 135, y + height - 80);

        // Defence ikon
        //font.setColor(Color.BLUE);
        batch.draw(shieldIcon, x + 20, y + height - 150, 30, 30);
        font.draw(batch, "" + base.getDefense(), x + 65, y + height - 125);

        // Train Units button
        font.draw(batch, "[ Train Units ]", buttonX, buttonY + 20);

        // Create Buildings button
        font.draw(batch, "[ Create Buildings ]", buttonX + 110, buttonY + 20);

        // Stäng-kryss
        batch.draw(letterXIcon, x + width - 35, y + height - 35, 25, 25);


        batch.end();
    }





    /**
     * Handles click input for closing the popup or pressing the button.
     * @param screenX Mouse X position
     * @param screenY Mouse Y position (inverted Y-axis)
     */
    public void handleClick(float screenX, float screenY) {
        if (!visible) return;
        // Check if click is inside popup
        boolean insidePopup = screenX >= x && screenX <= x + width &&
            screenY >= y && screenY <= y + height;

        if (!insidePopup) {
            hide(); // Close when clicking outside
            return;
        }

        // Area to close popup
        float closeX1 = x + width - 90;
        float closeX2 = x + width - 20;
        float closeY1 = y + height - 35;
        float closeY2 = y + height - 5;

        if (screenX >= closeX1 && screenX <= closeX2 &&
            screenY >= closeY1 && screenY <= closeY2) {
            hide();
            return;
        }

        // Train units button
        boolean buttonClicked = screenX >= buttonX && screenX <= buttonX + buttonWidth &&
            screenY >= buttonY && screenY <= buttonY + buttonHeight;

        if (buttonClicked) {
            if (listener != null) {
                listener.onTrainUnit(base);
            }
        }

        // Create Buildings button
        boolean buildingButtonClicked = screenX >= buildingButtonX
            && screenX <= buildingButtonX + buildingButtonWidth
            && screenY >= buildingButtonY
            && screenY <= buildingButtonY + buildingButtonHeight;

        if (buildingButtonClicked){
            if (listener != null){
                listener.onCreateBuilding(base);
            }
        }
    }



    public void show(BaseStats base) {
        this.base = base;
        this.visible = true;
    }

    public void setTrainUnitListener(TrainUnitListener listener){
        this.listener = listener;
    }
}

