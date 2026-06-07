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

/**
 * UnitView handles the visual representation and animation of a {@link Unit}.
 * It is responsible for:
 * <ul>
 *     <li>Rendering the unit sprite using {@link CharacterRenderer}</li>
 *     <li>Smooth movement animation between tiles</li>
 *     <li>Flipping the sprite depending on player ownership</li>
 *     <li>Displaying HP bars and HP text</li>
 *     <li>Notifying listeners when movement is complete</li>
 * </ul>
 */
public class UnitView {

    /**
     * Listener interface for receiving a callback when a movement animation finishes.
     * @author Enid Becarevic
     */
    public interface OnMoveCompleteListener {
        void onMoveComplete(UnitView view);
    }

    private Unit unit;
    private CharacterRenderer renderer;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private boolean flipX = false;

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

    /**
     * Creates a new UnitView for the given unit and renderer.
     *
     * @param unit     the logical unit this view represents
     * @param renderer the animation renderer for the unit
     *                      * @author Enid Becarevic
     */
    public UnitView(Unit unit, CharacterRenderer renderer) {
        this.unit = unit;
        this.renderer = renderer;
        this.shapeRenderer = new ShapeRenderer();
        this.font  = new BitmapFont();
        this.font.getData().setScale(0.6f);
        this.animX = unit.getX() * WorldMap.TILE_SIZE;
        this.animY = unit.getY() * WorldMap.TILE_SIZE;
    }

    /**
     * Sets a listener that will be notified when the unit finishes moving.
     *
     * @param l the listener to register
     *               * @author Enid Becarevic
     */    public void setOnMoveCompleteListener(OnMoveCompleteListener l) {
        this.moveCompleteListener = l;
    }

    /** @return true if the unit is currently animating movement
     *      * @author Enid Becarevic*/
    public boolean isMoving() { return isMoving; }
    /** @return the underlying logical unit
     *      * @author Enid Becarevic*/
    public Unit    getUnit()  { return unit; }

    /**
     * Instantly synchronizes the view's position with the unit's tile position.
     * Useful after teleportation or turn resets.
     *      * @author Enid Becarevic
     */
    public void syncPosition() {
        this.animX = unit.getX() * WorldMap.TILE_SIZE;
        this.animY = unit.getY() * WorldMap.TILE_SIZE;
    }

    /**
     * Sets a movement path for the unit and begins animating the first step.
     *
     * @param path a queue of tile coordinates the unit should move through
     *                  * @author Enid Becarevic
     */
    public void setMovementPath(Queue<int[]> path) {
        this.cancelDestination = null;
        this.movementQueue     = path;
        startNextStep();
    }


    /**
     * Sets a cancel movement path, returning the unit to its original tile.
     *
     * @param originX original X tile
     * @param originY original Y tile
     *                     * @author Enid Becarevic
     */
    public void setCancelPath(int originX, int originY) {
        this.cancelDestination = new int[]{ originX, originY };
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{ originX, originY });
        this.movementQueue = q;
        startNextStep();
    }

    /**
     * Starts animating the next step in the movement queue.
     * If no steps remain, the unit stops and notifies listeners.
     *      * @author Enid Becarevic
     */    private void startNextStep() {
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

    /**
     * Updates animation state and movement interpolation.
     *
     * @param delta time since last frame
     * @author Enid Becarevic
     * @author Oscar Lavesson
     */    public void update(float delta) {
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

    /**
     * Renders the unit sprite at its current animated position.
     *
     * @param batch the SpriteBatch used for drawing
     * @author Enid Becarevic
     */
    public void render(SpriteBatch batch) {
        batch.setColor(tintColor != null ? tintColor : Color.WHITE);
        TextureRegion frame = renderer.getCurrentFrame();

        if (flipX) {
            batch.draw(frame,
                animX - 8f + 32f, animY - 8f,
                -32f, 32f);
        } else {
            batch.draw(frame, animX - 8f, animY - 8f, 32, 32);
        }

        batch.setColor(Color.WHITE);
    }

    /**
     * Renders the unit's HP bar above the sprite.
     *
     * @param shapeRenderer the ShapeRenderer used for drawing rectangles
     *                           * @author Enid Becarevic
     */
    public void renderHpBar(ShapeRenderer shapeRenderer) {
        float barWidth   = 16f;
        float barHeight  = 3f;
        float barX       = animX - 8f;
        float barY       = animY + 26f;
        float hpPercent  = (float) unit.getCurrentHp() / unit.getMaxHp();

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(barX, barY, barWidth * hpPercent, barHeight);
    }

    /**
     * Renders the unit's HP text above the sprite.
     *
     * @param batch the SpriteBatch used for drawing text
     *                   * @author Enid Becarevic
     */
    public void renderHpText(SpriteBatch batch) {
        font.setColor(Color.WHITE);
        font.draw(batch, unit.getCurrentHp() + "/" + unit.getMaxHp(),
            animX - 8f, animY + 38f);
    }
    /**
     * Applies a color tint and sprite flip depending on the owning player.
     *
     * @param player the player owning the unit
     *                    * @author Enid Becarevic
     */
    public void applyPlayerColor(Player player) {
        switch (player) {
            case PLAYER_ONE -> {
                tintColor = new Color(1f, 0.3f, 0.3f, 1f);
                flipX = false;
            }
            case PLAYER_TWO -> {
                tintColor = new Color(0.3f, 0.3f, 1f, 1f);
                flipX = true;  // spegelvänd
            }
        }
    }

}
