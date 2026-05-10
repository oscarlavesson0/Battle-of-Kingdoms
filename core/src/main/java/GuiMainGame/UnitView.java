package GuiMainGame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import unit.Unit;

import java.util.LinkedList;
import java.util.Queue;

public class UnitView {

    private Unit unit;
    private CharacterRenderer renderer;

    // Animation movement queue
    private Queue<int[]> movementQueue = new LinkedList<>();

    // Smooth movement
    private float animX;
    private float animY;
    private float moveSpeed = 4f; // pixels per frame

    private boolean isMoving = false;
    private int targetTileX;
    private int targetTileY;

    public UnitView(Unit unit, CharacterRenderer renderer) {
        this.unit = unit;
        this.renderer = renderer;

        // Startposition i pixlar
        this.animX = unit.getX() * WorldMap.TILE_SIZE;
        this.animY = unit.getY() * WorldMap.TILE_SIZE;
    }

    // Called by UnitController
    public void setMovementPath(Queue<int[]> path) {
        this.movementQueue = path;
        startNextStep();
    }

    private void startNextStep() {
        if (movementQueue.isEmpty()) {
            isMoving = false;
            renderer.setState(State.IDLE);
            return;
        }

        int[] next = movementQueue.poll();
        targetTileX = next[0];
        targetTileY = next[1];

        isMoving = true;
        renderer.setState(State.RUN);
    }

    public void update(float delta) {
        renderer.update(delta);

        if (!isMoving) return;

        float targetX = targetTileX * WorldMap.TILE_SIZE;
        float targetY = targetTileY * WorldMap.TILE_SIZE;

        // Move smoothly
        if (Math.abs(animX - targetX) > 1) {
            animX += Math.signum(targetX - animX) * moveSpeed;
        }
        if (Math.abs(animY - targetY) > 1) {
            animY += Math.signum(targetY - animY) * moveSpeed;
        }

        // Reached tile
        if (Math.abs(animX - targetX) <= 1 && Math.abs(animY - targetY) <= 1) {
            animX = targetX;
            animY = targetY;

            // Update unit logic position
            unit.setPosition(targetTileX, targetTileY);

            startNextStep();
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(frame, animX, animY, 16, 16);
    }
}
