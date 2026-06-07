package GuiMainGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * ActionMenu represents the contextual action menu that appears when a unit is selected.
 * It supports multiple modes depending on the unit's state (before moving, after moving,
 * waiting, or when inspecting an enemy). The menu displays different buttons and colors
 * depending on the mode and whether the unit can attack.
 *
 * <p>The menu is rendered using LibGDX SpriteBatch and ShapeRenderer, and supports
 * click detection to determine which action the player selected.</p>
 *
 * <p>Supported actions include MOVE, WAIT, ATTACK, CANCEL, CLOSE, and INFO.</p>
 */

public class ActionMenu {
    /**
     * Represents the possible actions the player can select from the menu.
     */
    public enum Action { MOVE, WAIT, ATTACK, CANCEL, CLOSE, INFO }

    private boolean visible = false;
    private float menuX, menuY;
    private boolean canAttack;
    /**
     * Internal menu mode determining which buttons are shown.
     */
    private enum Mode { PRE_MOVE, PRE_WAIT, AFTER_WAIT, ENEMY_INFO }
    private Mode mode = Mode.PRE_MOVE;

    private static final float BTN_W = 110f;
    private static final float BTN_H = 30f;
    private static final float GAP   = 6f;
    private static final float PAD   = 10f;

    private final BitmapFont font;
    private Texture woodBackground;
    private Texture frameTexture;

    private static final Color BLUE   = new Color(0.20f, 0.45f, 0.75f, 1f);
    private static final Color PURPLE = new Color(0.40f, 0.40f, 0.80f, 1f);
    private static final Color RED    = new Color(0.75f, 0.18f, 0.18f, 1f);
    private static final Color GRAY   = new Color(0.50f, 0.50f, 0.50f, 1f);
    private static final Color DARK   = new Color(0.30f, 0.30f, 0.30f, 1f);
    private static final Color YELLOW = new Color(0.55f, 0.55f, 0.18f, 1f);

    /**
     * Creates a new ActionMenu and loads required fonts and textures.
     * @Author Enid Becarevic
     */
    public ActionMenu() {
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
            Gdx.files.internal("lwjgl3/assets/ui/font/PixelWarden.ttf"));
        FreeTypeFontParameter param = new FreeTypeFontParameter();
        param.size = 16;
        font = gen.generateFont(param);
        gen.dispose();

        woodBackground = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/woodtexture.png"));
        frameTexture   = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/frame.png"));
    }

    /**
     * Shows the menu before the unit has moved.
     *
     * @param x menu X position
     * @param y menu Y position
     * @param canAttack whether the unit can attack this turn
     * @Author Enid Becarevic
     */
    public void showPreMove(float x, float y, boolean canAttack) {
        this.menuX = x; this.menuY = y;
        this.canAttack = canAttack;
        this.mode = Mode.PRE_MOVE;
        this.visible = true;
    }

    /**
     * Shows the menu before the unit waits.
     *
     * @param x menu X position
     * @param y menu Y position
     * @param canAttack whether the unit can attack this turn
     * @Author Enid Becarevic
     */
    public void showPreWait(float x, float y, boolean canAttack) {
        this.menuX = x; this.menuY = y;
        this.canAttack = canAttack;
        this.mode = Mode.PRE_WAIT;
        this.visible = true;
    }
    /**
     * Shows the menu after the unit has waited.
     *
     * @param x menu X position
     * @param y menu Y position
     * @Author Enid Becarevic
     */
    public void showAfterWait(float x, float y) {
        this.menuX = x; this.menuY = y;
        this.canAttack = false;
        this.mode = Mode.AFTER_WAIT;
        this.visible = true;
    }

    /**
     * Shows the enemy info menu.
     *
     * @param x menu X position
     * @param y menu Y position
     * @Author Enid Becarevic
     */
    public void showEnemyInfoMenu(float x, float y) {
        this.menuX = x; this.menuY = y;
        this.canAttack = false;
        this.mode = Mode.ENEMY_INFO;
        this.visible = true;
    }

    /** Hides the menu.
     * @Author Enid Becarevic
     */

    public void hide()          { visible = false; }

    /**
     * @return true if the menu is currently visible
     * @Author Enid Becarevic
     */
    public boolean isVisible()  { return visible; }

    /**
     * Calculates how many buttons should be displayed depending on the mode.
     *
     * @return number of buttons
     * @Author Enid Becarevic
     */
    private int buttonCount() {
        return switch (mode) {
            case PRE_MOVE   -> 3 + (canAttack ? 1 : 0) + 1; // Close,Wait,Move,[Attack],Info
            case PRE_WAIT   -> 2 + (canAttack ? 1 : 0) + 1; // Wait,Cancel,[Attack],Info
            case AFTER_WAIT -> 1;
            case ENEMY_INFO -> 1;
        };
    }

    /**
     * Checks if a click is inside a button.
     *
     * @param cx click X
     * @param cy click Y
     * @param by button Y position
     * @return true if the click hits the button
     * @Author Enid Becarevic
     */
    private boolean hit(float cx, float cy, float by) {
        return cx >= menuX && cx <= menuX + BTN_W &&
            cy >= by    && cy <= by + BTN_H;
    }

    /**
     * Handles a click on the menu and returns the selected action.
     *
     * @param cx click X coordinate
     * @param cy click Y coordinate
     * @return the selected Action, or null if no button was clicked
     * @Author Enid Becarevic
     */
    public Action handleClick(float cx, float cy) {
        if (!visible) return null;
        hide();
        float by = menuY;

        switch (mode) {
            case ENEMY_INFO -> {
                if (hit(cx, cy, by)) return Action.INFO;
            }
            case PRE_MOVE -> {
                if (hit(cx, cy, by)) return Action.CLOSE;
                by += BTN_H + GAP;
                if (hit(cx, cy, by)) return Action.WAIT;
                by += BTN_H + GAP;
                if (hit(cx, cy, by)) return Action.MOVE;
                by += BTN_H + GAP;
                if (canAttack) {
                    if (hit(cx, cy, by)) return Action.ATTACK;
                    by += BTN_H + GAP;
                }
                if (hit(cx, cy, by)) return Action.INFO;
            }
            case PRE_WAIT -> {
                if (hit(cx, cy, by)) return Action.WAIT;
                by += BTN_H + GAP;
                if (hit(cx, cy, by)) return Action.CANCEL;
                by += BTN_H + GAP;
                if (canAttack) {
                    if (hit(cx, cy, by)) return Action.ATTACK;
                    by += BTN_H + GAP;
                }
                if (hit(cx, cy, by)) return Action.INFO;
            }
            case AFTER_WAIT -> {
                if (hit(cx, cy, by)) return Action.CLOSE;
            }
        }
        return null;
    }

    /**
     * Renders the menu background, buttons, and text.
     *
     * @param batch SpriteBatch used for drawing textures and text
     * @param sr ShapeRenderer used for drawing button rectangles
     * @Author Enid Becarevic
     */
    public void render(SpriteBatch batch, ShapeRenderer sr) {
        if (!visible) return;

        int   count  = buttonCount();
        float totalH = count * BTN_H + (count - 1) * GAP;
        float bgW    = BTN_W + PAD * 2;
        float bgH    = totalH + PAD * 2;
        float bgX    = menuX - PAD;
        float bgY    = menuY - PAD;

        if (batch.isDrawing()) batch.end();
        batch.begin();
        batch.draw(woodBackground, bgX, bgY, bgW, bgH);
        batch.draw(frameTexture, bgX - 10, bgY - 12, bgW + 22, bgH + 24);
        batch.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        float by = menuY;

        switch (mode) {
            case ENEMY_INFO -> drawBtn(sr, by, YELLOW);
            case PRE_MOVE -> {
                drawBtn(sr, by, DARK);  by += BTN_H + GAP;
                drawBtn(sr, by, PURPLE);   by += BTN_H + GAP;
                drawBtn(sr, by, BLUE);   by += BTN_H + GAP;
                if (canAttack) { drawBtn(sr, by, RED); by += BTN_H + GAP; }
                drawBtn(sr, by, YELLOW);
            }
            case PRE_WAIT -> {
                drawBtn(sr, by, PURPLE);   by += BTN_H + GAP;
                drawBtn(sr, by, GRAY); by += BTN_H + GAP;
                if (canAttack) { drawBtn(sr, by, RED); by += BTN_H + GAP; }
                drawBtn(sr, by, YELLOW);
            }
            case AFTER_WAIT -> drawBtn(sr, by, DARK);
        }
        sr.end();

        // Text
        batch.begin();
        font.setColor(Color.WHITE);
        by = menuY;
        float textOffsetY = BTN_H - 9f;

        switch (mode) {
            case ENEMY_INFO -> font.draw(batch, "Info",   menuX + 8, by + textOffsetY);
            case PRE_MOVE -> {
                font.draw(batch, "Close",  menuX + 8, by + textOffsetY); by += BTN_H + GAP;
                font.draw(batch, "Wait",   menuX + 8, by + textOffsetY); by += BTN_H + GAP;
                font.draw(batch, "Move",   menuX + 8, by + textOffsetY); by += BTN_H + GAP;
                if (canAttack) { font.draw(batch, "Attack", menuX + 8, by + textOffsetY); by += BTN_H + GAP; }
                font.draw(batch, "Info",   menuX + 8, by + textOffsetY);
            }
            case PRE_WAIT -> {
                font.draw(batch, "Wait",   menuX + 8, by + textOffsetY); by += BTN_H + GAP;
                font.draw(batch, "Cancel", menuX + 8, by + textOffsetY); by += BTN_H + GAP;
                if (canAttack) { font.draw(batch, "Attack", menuX + 8, by + textOffsetY); by += BTN_H + GAP; }
                font.draw(batch, "Info",   menuX + 8, by + textOffsetY);
            }
            case AFTER_WAIT -> font.draw(batch, "Close", menuX + 8, by + textOffsetY);
        }
        batch.end();
    }

    /**
     * Draws a single button rectangle.
     *
     * @param sr ShapeRenderer instance
     * @param y button Y position
     * @param col button color
     * @Author Enid Becarevic
     */
    private void drawBtn(ShapeRenderer sr, float y, Color col) {
        sr.setColor(col);
        sr.rect(menuX, y, BTN_W, BTN_H);
    }
}
