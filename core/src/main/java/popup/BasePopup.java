package popup;

import base.BaseStats;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


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

        font = new BitmapFont();
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
        shapeRenderer.setProjectionMatrix(camera.combined);
        // Draw background and HP bar with ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // White background
        shapeRenderer.setColor(new Color(1f, 1f, 1f, 0.95f));
        shapeRenderer.rect(x, y, width, height);

        // HP bar background
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x + 20, y + height - 120, width - 40, 20);

        // HP bar fill
        float hpPercent = (float) base.getCurrentHp() / base.getMaxHp();
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(x + 20, y + height - 120, (width - 40) * hpPercent, 20);

        shapeRenderer.end();

        // Draw text with SpriteBatch
        batch.begin();

        // Owner text
        font.setColor(Color.BLACK);
        font.draw(batch, "Owner: " + base.getOwner().getDisplayName(), x + 20, y + height - 20);

        // HP text
        font.setColor(Color.GREEN);
        font.draw(batch, "HP: " + base.getCurrentHp() + " / " + base.getMaxHp(),
            x + 20, y + height - 70);

        // Defense text
        font.setColor(Color.BLUE);
        font.draw(batch, "Defense: " + base.getDefense(), x + 20, y + height - 140);

        // Close button
        font.setColor(Color.RED);
        font.draw(batch, "[ Close ]", x + width - 80, y + height - 20);

        // Train Units button
        font.setColor(Color.DARK_GRAY);
        font.draw(batch, "[ Train Units ]", buttonX + 0, buttonY + 10);

        // Create buildings button
        font.draw(batch, "[ Create Buildings ]", buttonX + 100, buttonY + 10);

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

