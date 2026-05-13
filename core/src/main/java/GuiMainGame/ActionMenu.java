package GuiMainGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ActionMenu {

    public enum Action { ATTACK, WAIT, CANCEL }

    private boolean visible = false;
    private float menuX, menuY;
    private boolean canAttack;

    private static final float BTN_W = 90f;
    private static final float BTN_H = 26f;
    private static final float GAP   = 4f;
    private static final float PAD   = 6f;

    private final BitmapFont font;

    public ActionMenu() {
        font = new BitmapFont();
        font.getData().setScale(0.85f);
    }

    public void showPostMove(float worldX, float worldY, boolean canAttack) {
        this.menuX = worldX + 4;
        this.menuY = worldY + 4;
        this.canAttack = canAttack;
        this.visible = true;
    }

    public void hide() { visible = false; }
    public boolean isVisible() { return visible; }

    public Action handleClick(float cx, float realY) {
        if (!visible) return null;
        hide();

        float by = menuY;

        // Attack (visas bara om det finns en fiende i närheten)
        if (canAttack) {
            if (cx >= menuX && cx <= menuX + BTN_W
                && realY >= by && realY <= by + BTN_H)
                return Action.ATTACK;
            by += BTN_H + GAP;
        }

        // Wait
        if (cx >= menuX && cx <= menuX + BTN_W
            && realY >= by && realY <= by + BTN_H)
            return Action.WAIT;
        by += BTN_H + GAP;

        // Cancel
        if (cx >= menuX && cx <= menuX + BTN_W
            && realY >= by && realY <= by + BTN_H)
            return Action.CANCEL;

        return Action.CANCEL; // klick utanför = avbryt
    }

    public void render(SpriteBatch batch, ShapeRenderer sr) {
        if (!visible) return;

        int count   = (canAttack ? 1 : 0) + 2; // attack? + wait + cancel
        float totalH = count * BTN_H + Math.max(0, count - 1) * GAP;

        if (batch.isDrawing()) batch.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Mörk bakgrundspanel
        sr.setColor(0.08f, 0.08f, 0.08f, 0.92f);
        sr.rect(menuX - PAD, menuY - PAD, BTN_W + PAD * 2, totalH + PAD * 2);

        float by = menuY;
        if (canAttack) {
            sr.setColor(0.58f, 0.10f, 0.10f, 1f); // röd – Attack
            sr.rect(menuX, by, BTN_W, BTN_H);
            by += BTN_H + GAP;
        }
        sr.setColor(0.17f, 0.37f, 0.57f, 1f); // blå – Wait
        sr.rect(menuX, by, BTN_W, BTN_H);
        by += BTN_H + GAP;
        sr.setColor(0.35f, 0.35f, 0.35f, 1f); // grå – Cancel
        sr.rect(menuX, by, BTN_W, BTN_H);

        sr.end();

        batch.begin();
        font.setColor(Color.WHITE);
        by = menuY;
        if (canAttack) {
            font.draw(batch, "Attack", menuX + 8, by + BTN_H - 8);
            by += BTN_H + GAP;
        }
        font.draw(batch, "Wait",   menuX + 8, by + BTN_H - 8);
        by += BTN_H + GAP;
        font.draw(batch, "Cancel", menuX + 8, by + BTN_H - 8);
        batch.end();
    }
}
