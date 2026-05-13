package GuiMainGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ActionMenu {

    public enum Action { MOVE, WAIT, ATTACK, CANCEL, CLOSE }

    private boolean visible = false;
    private float menuX, menuY;
    private boolean canAttack;

    private enum Mode { PRE_MOVE, PRE_WAIT, AFTER_WAIT }
    private Mode mode = Mode.PRE_MOVE;

    private static final float BTN_W = 90f;
    private static final float BTN_H = 26f;
    private static final float GAP   = 4f;
    private static final float PAD   = 6f;

    private final BitmapFont font;

    public ActionMenu() {
        font = new BitmapFont();
        font.getData().setScale(0.85f);
    }

    public void showPreMove(float x, float y, boolean canAttack) {
        this.menuX = x;
        this.menuY = y;
        this.canAttack = canAttack;
        this.mode = Mode.PRE_MOVE;
        this.visible = true;
    }

    public void showPreWait(float x, float y, boolean canAttack) {
        this.menuX = x;
        this.menuY = y;
        this.canAttack = canAttack;
        this.mode = Mode.PRE_WAIT;
        this.visible = true;
    }

    public void showAfterWait(float x, float y) {
        this.menuX = x;
        this.menuY = y;
        this.canAttack = false;
        this.mode = Mode.AFTER_WAIT;
        this.visible = true;
    }

    public void hide() { visible = false; }
    public boolean isVisible() { return visible; }

    private boolean hit(float cx, float cy, float by) {
        return cx >= menuX && cx <= menuX + BTN_W &&
            cy >= by    && cy <= by + BTN_H;
    }

    public Action handleClick(float cx, float cy) {
        if (!visible) return null;
        hide();

        float by = menuY;

        switch (mode) {

            case PRE_MOVE -> {
                // Bottom to top: Close, Wait, Move, [Attack]
                if (hit(cx, cy, by)) return Action.CLOSE;
                by += BTN_H + GAP;

                if (hit(cx, cy, by)) return Action.WAIT;
                by += BTN_H + GAP;

                if (hit(cx, cy, by)) return Action.MOVE;
                by += BTN_H + GAP;

                if (canAttack && hit(cx, cy, by)) return Action.ATTACK;
            }

            case PRE_WAIT -> {
                // Bottom to top: Wait, Cancel, [Attack]
                if (hit(cx, cy, by)) return Action.WAIT;
                by += BTN_H + GAP;

                if (hit(cx, cy, by)) return Action.CANCEL;
                by += BTN_H + GAP;

                if (canAttack && hit(cx, cy, by)) return Action.ATTACK;
            }

            case AFTER_WAIT -> {
                // Only Close
                if (hit(cx, cy, by)) return Action.CLOSE;
            }
        }

        return null;
    }

    private void drawButton(ShapeRenderer sr, float y, float r, float g, float b) {
        sr.setColor(r, g, b, 1f);
        sr.rect(menuX, y, BTN_W, BTN_H);
    }

    public void render(SpriteBatch batch, ShapeRenderer sr) {
        if (!visible) return;

        int count = switch (mode) {
            case PRE_MOVE  -> 3 + (canAttack ? 1 : 0);
            case PRE_WAIT  -> 2 + (canAttack ? 1 : 0);
            case AFTER_WAIT -> 1;
        };

        float totalH = count * BTN_H + (count - 1) * GAP;

        if (batch.isDrawing()) batch.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);

        sr.setColor(0, 0, 0, 0.85f);
        sr.rect(menuX - PAD, menuY - PAD, BTN_W + PAD * 2, totalH + PAD * 2);

        float by = menuY;

        switch (mode) {

            case PRE_MOVE -> {
                drawButton(sr, by, 0.3f, 0.3f, 0.3f); // Close
                by += BTN_H + GAP;

                drawButton(sr, by, 0.4f, 0.4f, 0.8f); // Wait
                by += BTN_H + GAP;

                drawButton(sr, by, 0.2f, 0.4f, 0.7f); // Move
                by += BTN_H + GAP;

                if (canAttack)
                    drawButton(sr, by, 0.8f, 0.2f, 0.2f); // Attack
            }

            case PRE_WAIT -> {
                drawButton(sr, by, 0.4f, 0.4f, 0.8f); // Wait
                by += BTN_H + GAP;

                drawButton(sr, by, 0.5f, 0.5f, 0.5f); // Cancel
                by += BTN_H + GAP;

                if (canAttack)
                    drawButton(sr, by, 0.8f, 0.2f, 0.2f); // Attack
            }

            case AFTER_WAIT -> {
                drawButton(sr, by, 0.3f, 0.3f, 0.3f); // Close
            }
        }

        sr.end();

        batch.begin();
        font.setColor(Color.WHITE);
        by = menuY;

        switch (mode) {

            case PRE_MOVE -> {
                font.draw(batch, "Close",  menuX + 8, by + BTN_H - 8);
                by += BTN_H + GAP;

                font.draw(batch, "Wait",   menuX + 8, by + BTN_H - 8);
                by += BTN_H + GAP;

                font.draw(batch, "Move",   menuX + 8, by + BTN_H - 8);
                by += BTN_H + GAP;

                if (canAttack)
                    font.draw(batch, "Attack", menuX + 8, by + BTN_H - 8);
            }

            case PRE_WAIT -> {
                font.draw(batch, "Wait",   menuX + 8, by + BTN_H - 8);
                by += BTN_H + GAP;

                font.draw(batch, "Cancel", menuX + 8, by + BTN_H - 8);
                by += BTN_H + GAP;

                if (canAttack)
                    font.draw(batch, "Attack", menuX + 8, by + BTN_H - 8);
            }

            case AFTER_WAIT -> {
                font.draw(batch, "Close",  menuX + 8, by + BTN_H - 8);
            }
        }

        batch.end();
    }
}
