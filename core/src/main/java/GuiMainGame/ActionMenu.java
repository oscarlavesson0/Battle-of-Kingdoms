package GuiMainGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ActionMenu {

    public enum Action { MOVE, ATTACK, CANCEL }

    private boolean visible   = false;
    private float   menuX, menuY;
    private boolean canMove, canAttack;

    private static final float BTN_W = 90f;
    private static final float BTN_H = 26f;
    private static final float GAP   =  4f;
    private static final float PAD   =  6f;

    private final BitmapFont font;

    public ActionMenu() {
        font = new BitmapFont();
        font.getData().setScale(0.85f);
    }

    public void show(float screenX, float screenY, boolean canMove, boolean canAttack) {
        this.menuX     = screenX + 4;
        this.menuY     = screenY + 4;
        this.canMove   = canMove;
        this.canAttack = canAttack;
        this.visible   = true;
    }

    public void    hide()         { visible = false; }
    public boolean isVisible()    { return visible;  }

    public Action handleClick(float cx, float realY) {
        if (!visible) return null;
        hide();

        float by = menuY;
        if (canMove) {
            if (cx >= menuX && cx <= menuX + BTN_W && realY >= by && realY <= by + BTN_H)
                return Action.MOVE;
            by += BTN_H + GAP;
        }
        if (canAttack) {
            if (cx >= menuX && cx <= menuX + BTN_W && realY >= by && realY <= by + BTN_H)
                return Action.ATTACK;
        }
        return Action.CANCEL;
    }

    public void render(SpriteBatch batch, ShapeRenderer sr) {
        if (!visible) return;

        int   count  = (canMove ? 1 : 0) + (canAttack ? 1 : 0);
        float totalH = count * BTN_H + Math.max(0, count - 1) * GAP;

        if (batch.isDrawing()) batch.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        // dark outer border / background
        sr.setColor(0.08f, 0.08f, 0.08f, 0.92f);
        sr.rect(menuX - PAD, menuY - PAD, BTN_W + PAD * 2, totalH + PAD * 2);

        float by = menuY;
        if (canMove) {
            sr.setColor(0.17f, 0.47f, 0.17f, 1f);   // green – Move
            sr.rect(menuX, by, BTN_W, BTN_H);
            by += BTN_H + GAP;
        }
        if (canAttack) {
            sr.setColor(0.58f, 0.10f, 0.10f, 1f);   // red   – Attack
            sr.rect(menuX, by, BTN_W, BTN_H);
        }
        sr.end();

        batch.begin();
        font.setColor(Color.WHITE);
        by = menuY;
        if (canMove) {
            font.draw(batch, "Move",   menuX + 8, by + BTN_H - 8);
            by += BTN_H + GAP;
        }
        if (canAttack) {
            font.draw(batch, "Attack", menuX + 8, by + BTN_H - 8);
        }
        batch.end();
    }
}
