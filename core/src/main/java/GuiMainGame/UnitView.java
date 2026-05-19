package GuiMainGame;

import base.Player;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import unit.Unit;

import java.util.LinkedList;
import java.util.Queue;

public class UnitView {

    public interface OnMoveCompleteListener {
        void onMoveComplete(UnitView view);
    }

    private Unit unit;
    private CharacterRenderer renderer;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private Queue<int[]> movementQueue = new LinkedList<>();
    private float animX;
    private float animY;
    private final float moveSpeed = 4f;
    private boolean isMoving = false;
    private int targetTileX;
    private int targetTileY;
    private Color tintColor;

    private OnMoveCompleteListener moveCompleteListener;

    private int[] cancelDestination = null;

    public UnitView(Unit unit, CharacterRenderer renderer) {
        this.unit = unit;
        this.renderer = renderer;
        this.shapeRenderer = new ShapeRenderer();
        this.font  = new BitmapFont();
        this.font.getData().setScale(0.6f);
        this.animX = unit.getX() * WorldMap.TILE_SIZE;
        this.animY = unit.getY() * WorldMap.TILE_SIZE;
    }

    //  Publika metoder
    public void setOnMoveCompleteListener(OnMoveCompleteListener l) {
        this.moveCompleteListener = l;
    }

    public boolean isMoving() { return isMoving; }
    public Unit    getUnit()  { return unit; }

    public void syncPosition() {
        this.animX = unit.getX() * WorldMap.TILE_SIZE;
        this.animY = unit.getY() * WorldMap.TILE_SIZE;
    }

    public void setMovementPath(Queue<int[]> path) {
        this.cancelDestination = null;
        this.movementQueue     = path;
        startNextStep();
    }


    public void setCancelPath(int originX, int originY) {
        this.cancelDestination = new int[]{ originX, originY };
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{ originX, originY });
        this.movementQueue = q;
        startNextStep();
    }

    //  Intern rörelselogik
    private void startNextStep() {
        if (movementQueue.isEmpty()) {
            isMoving = false;
            renderer.setState(State.IDLE);
            if (moveCompleteListener != null) {
                moveCompleteListener.onMoveComplete(this);
            }
            return;
        }
        int[] next  = movementQueue.poll();
        targetTileX = next[0];
        targetTileY = next[1];
        isMoving    = true;
        renderer.setState(State.RUN);
    }

    //  Update & Render
    public void update(float delta) {
        renderer.update(delta);
        if (!isMoving) return;

        float targetX = targetTileX * WorldMap.TILE_SIZE;
        float targetY = targetTileY * WorldMap.TILE_SIZE;

        if (Math.abs(animX - targetX) > 1)
            animX += Math.signum(targetX - animX) * moveSpeed;
        if (Math.abs(animY - targetY) > 1)
            animY += Math.signum(targetY - animY) * moveSpeed;

        if (Math.abs(animX - targetX) <= 1 && Math.abs(animY - targetY) <= 1) {
            animX = targetX;
            animY = targetY;

            if (cancelDestination != null) {
                // Cancel-läge: sätt unit till ursprungsrutan
                unit.setPosition(cancelDestination[0], cancelDestination[1]);
                cancelDestination = null;
            } else {
                unit.setPosition(targetTileX, targetTileY);
            }

            startNextStep();
        }
    }

    public void render(SpriteBatch batch) {
        batch.setColor(tintColor != null ? tintColor : Color.WHITE);
        TextureRegion frame = renderer.getCurrentFrame();
        batch.draw(frame, animX, animY, 16, 16);
        batch.setColor(Color.WHITE);
    }

    public void renderHpBar(ShapeRenderer shapeRenderer) {
        float barWidth   = 16f;
        float barHeight  = 3f;
        float barX       = animX;
        float barY       = animY + 18f;
        float hpPercent  = (float) unit.getCurrentHp() / unit.getMaxHp();

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(barX, barY, barWidth * hpPercent, barHeight);
    }

    public void renderHpText(SpriteBatch batch) {
        font.setColor(Color.WHITE);
        font.draw(batch, unit.getCurrentHp() + "/" + unit.getMaxHp(),
            animX + 18f, animY + 22f);
    }

    public void applyPlayerColor(Player player) {
        switch (player) {
            case PLAYER_ONE -> tintColor = new Color(1f, 0.3f, 0.3f, 1f);
            case PLAYER_TWO -> tintColor = new Color(0.3f, 0.3f, 1f, 1f);
        }
    }
    public Unit getUnit() {
        return unit;
    }
}
