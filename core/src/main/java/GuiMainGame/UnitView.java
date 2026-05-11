package GuiMainGame;

import base.Player;
import com.badlogic.gdx.graphics.Color;
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
    private Color tintColor;


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

            batch.setColor(tintColor);   // färga uniten

            int tileX = unit.getX();
            int tileY = unit.getY();

            int px = tileX * WorldMap.TILE_SIZE;
            int py = tileY * WorldMap.TILE_SIZE;

            TextureRegion frame = renderer.getCurrentFrame();

            batch.draw(frame, px, py, 16, 16);

            batch.setColor(Color.WHITE); // återställ färgen
    }
    public void applyPlayerColor(Player player) {
        switch (player) {
            case PLAYER_ONE -> tintColor = new Color(1f, 0.3f, 0.3f, 1f); // röd
            case PLAYER_TWO -> tintColor = new Color(0.3f, 0.3f, 1f, 1f); // blå
        }
    }
}
