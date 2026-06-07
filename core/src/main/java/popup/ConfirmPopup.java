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
 * Reusable yes/no confirmation popup. Shows a message with Yes and No buttons
 * and reports the choice to a {@link ConfirmListener}.
 *
 * @author Emil Hadzic
 */
public class ConfirmPopup {

    private OrthographicCamera camera;
    private float x, y, width, height;
    private boolean visible = false;
    private String message = "";
    private ConfirmListener listener;

    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    private Texture woodBackground;
    private Texture frameTexture;
    private Texture letterXIcon;

    private final Color GOLD = new Color(1f, 0.84f, 0.0f, 1f);

    private float yesX, yesY, noX, noY, btnW, btnH;

    /**
     * Creates a confirmation popup at the given position and size.
     *
     * @param x      the popup's x position
     * @param y      the popup's y position
     * @param width  the popup's width
     * @param height the popup's height
     * @param camera the camera whose projection is used for rendering
     */
    public ConfirmPopup(float x, float y, float width, float height, OrthographicCamera camera){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.camera = camera;

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("lwjgl3/assets/ui/font/PixelWarden.ttf"));
        FreeTypeFontParameter param = new FreeTypeFontParameter();
        param.size = 18;
        font = gen.generateFont(param);
        gen.dispose();

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        woodBackground = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/woodtexture.png"));
        frameTexture = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/frame.png"));
        letterXIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/letter-x.png"));

        btnW = 80;
        btnH = 30;
        yesX = x + 30;
        yesY = y + 30;
        noX = x + width - btnW - 30;
        noY = y + 30;
    }

    /**
     * Shows the popup with the given message and result listener.
     *
     * @param message  the question shown to the user
     * @param listener the listener notified of the result
     */
    public void open(String message, ConfirmListener listener){
        this.message = message;
        this.listener = listener;
        this.visible = true;

    }

    /** Hides the popup. */
    public void hide(){
        visible = false;
    }

    /** @return true if the popup is currently visible */
    public boolean isVisible(){
        return visible;
    }

    /** Renders the popup background, buttons, message and close icon. */
    public void render(){
        if (!visible) return;

        if (batch.isDrawing()) batch.end();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        //woodbackground and frame
        batch.begin();
        batch.draw(woodBackground, x, y, width, height);
        batch.draw(frameTexture, x - 13, y - 15, width + 30, height + 30);
        batch.end();

        //rectangle buttons
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.2f, 0.1f, 0.05f, 1f);
        shapeRenderer.rect(yesX, yesY, btnW, btnH);
        shapeRenderer.rect(noX, noY, btnW, btnH);
        shapeRenderer.end();

        //text and x button
        batch.begin();
        font.setColor(GOLD);
        font.draw(batch, message, x + 20, y + height - 40);
        font.draw(batch, "Yes", yesX + 25, yesY + 22);
        font.draw(batch, "No", noX + 30, noY + 22);
        batch.draw(letterXIcon, x + width - 35, y + height - 35, 25, 25);
        batch.end();
    }

    /**
     * Handles a click at the given world coordinates. Yes confirms, No and the
     * close icon both cancel.
     *
     * @param screenX the click's x coordinate in world space
     * @param realY   the click's y coordinate in world space
     */
    public void handleClick(float screenX, float realY) {
        if (!visible) return;

        float closeX1 = x + width - 35;
        float closeY1 = y + height - 35;
        if (screenX >= closeX1 && screenX <= closeX1 + 25 && realY >= closeY1 && realY <= closeY1 + 25) {
            if (listener != null) listener.onResult(false);
            hide();
            return;
        }
        //yes button
        if (screenX >= yesX && screenX <= yesX + btnW && realY >= yesY && realY <= yesY + btnH) {
            if (listener != null) listener.onResult(true);
            hide();
            return;
        }
        //no button
        if (screenX >= noX && screenX <= noX + btnW && realY >= noY && realY <= noY + btnH) {
            if (listener != null) listener.onResult(false);
            hide();
        }
    }

    /** Disposes all graphics resources held by the popup. */
    public void dispose() {
        font.dispose();
        batch.dispose();
        woodBackground.dispose();
        frameTexture.dispose();
        letterXIcon.dispose();
        shapeRenderer.dispose();
    }
}
