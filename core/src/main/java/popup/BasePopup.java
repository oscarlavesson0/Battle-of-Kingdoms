package popup;

import base.Base;
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

    private Base base;

    // Popup position and size
    private float x, y, width, height;

    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    private boolean visible = false;

    // Button area
    private float buttonX, buttonY, buttonWidth, buttonHeight;

    /**
     * Creates a popup window for a specific Base.
     * @param base The base whose information will be displayed.
     * @param x Popup X position
     * @param y Popup Y position
     * @param width Popup width
     * @param height Popup height
     */
    public BasePopup(Base base, float x, float y, float width, float height) {
        this.base = base;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();

        // Button size and position
        buttonWidth = width - 40;
        buttonHeight = 40;
        buttonX = x + 20;
        buttonY = y + 20;
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

        // Draw background rectangle
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0, 0, 0.8f));
        shapeRenderer.rect(x, y, width, height);

        // Draw HP bar background
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x + 20, y + height - 90, width - 40, 20);

        // Draw HP bar fill
        float hpPercent = (float) base.getCurrentHp() / base.getMaxHp();
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(x + 20, y + height - 90, (width - 40) * hpPercent, 20);

        shapeRenderer.end();
        batch.begin();

        // Draw owner name
        font.setColor(Color.WHITE);
        font.draw(batch, "Owner: " + base.getOwner().getId(), x + 20, y + height - 20);

        // Draw HP text
        font.setColor(Color.GREEN);
        font.draw(batch, "HP: " + base.getCurrentHp() + " / " + base.getMaxHp(),
            x + 20, y + height - 60);

        // Draw defense text
        font.setColor(Color.CYAN);
        font.draw(batch, "Defense: " + base.getDefense(), x + 20, y + height - 120);

        // Draw button
        font.setColor(Color.LIGHT_GRAY);
        font.draw(batch, "[ Train Units ]", buttonX + 10, buttonY + 30);
    }

    /**
     * Handles click input for closing the popup or pressing the button.
     * @param screenX Mouse X position
     * @param screenY Mouse Y position (inverted Y-axis)
     */
    public void handleClick(float screenX, float screenY) {
        if (!visible) return;

        // Convert Y coordinate (LibGDX uses bottom-left origin)
        float realY = Gdx.graphics.getHeight() - screenY;

        // Check if click is inside popup
        boolean insidePopup = screenX >= x && screenX <= x + width &&
            realY >= y && realY <= y + height;

        if (!insidePopup) {
            hide();
            return;
        }

        // Check if button is clicked
        boolean buttonClicked = screenX >= buttonX && screenX <= buttonX + buttonWidth &&
            realY >= buttonY && realY <= buttonY + buttonHeight;

        if (buttonClicked) {
            System.out.println("Train Units clicked (not implemented)");
        }
    }

    public void show(Base base) {
        this.base = base;
        this.visible = true;
    }
}

