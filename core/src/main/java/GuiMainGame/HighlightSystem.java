package GuiMainGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import unit.Unit;
import unit.UnitController;

import java.util.ArrayList;
import java.util.List;
/**
 * HighlightSystem is responsible for calculating and rendering tile highlights
 * for unit movement and interaction zones (attack, building interaction, etc.).
 *
 * <p>This system does not decide game logic itself — it only visualizes the
 * tiles that the {@link UnitController} determines are valid for movement or
 * interaction. It draws colored overlays on the map using a {@link ShapeRenderer}.</p>
 *
 * <p>Types of highlights:</p>
 * <ul>
 *     <li><b>Movement tiles</b> – tiles the unit can move to</li>
 *     <li><b>Interaction tiles</b> – tiles where the unit can attack or interact</li>
 *     <li><b>Highlighted tiles</b> – general-purpose highlights (currently unused)</li>
 * </ul>
 */

public class HighlightSystem {

    private final UnitController  unitController;
    private final ShapeRenderer   shapeRenderer;

    private List<int[]> movementTiles = new ArrayList<>();
    private List<int[]> highlightedTiles = new ArrayList<>();
    private List<int[]> interactionTiles = new ArrayList<>();
    /**
     * Creates a new HighlightSystem.
     *
     * @param unitController the controller used to calculate movement and interaction tiles
     * @Author Enid Becarevic
     */
    public HighlightSystem(UnitController unitController) {
        this.unitController = unitController;
        this.shapeRenderer  = new ShapeRenderer();
    }

    /**
     * Updates both movement and interaction highlights for the given unit.
     * If the unit is null, all highlights are cleared.
     *
     * @param unit the unit to highlight tiles for
     * @Author Enid Becarevic
     */
    public void updateHighlight(Unit unit) {
        if (unit == null) { clear(); return; }
        movementTiles    = unitController.getMovementTiles(unit);
        interactionTiles = unitController.getInteractionZoneTiles(unit, movementTiles);
    }

    /**
     * Updates only interaction highlights (attack range), ignoring movement tiles.
     * Used when a unit has already moved and can only attack.
     *
     * @param unit the unit to highlight attack tiles for
     * @Author Enid Becarevic
     */
    public void updateAttackOnlyHighlight(Unit unit) {
        if (unit == null) { clear(); return; }
        movementTiles.clear();
        interactionTiles = unitController.getInteractionZoneTiles(unit, movementTiles);
    }

    /**
     * Clears all highlight lists.
     *@Author Enid Becarevic
     */
    public void clear() {
        movementTiles.clear();
        interactionTiles.clear();
        highlightedTiles.clear();
    }

    /**
     * Checks whether a tile is part of the movement highlight.
     *
     * @param tileX tile X coordinate
     * @param tileY tile Y coordinate
     * @return true if the tile is highlighted for movement
     * @Author Enid Becarevic
     */
    public boolean isHighlighted(int tileX, int tileY) {
        for (int[] pos : movementTiles)
            if (pos[0] == tileX && pos[1] == tileY) return true;
        return false;
    }

    /**
     * Checks whether a tile is part of the interaction highlight (attack zone).
     *
     * @param tileX tile X coordinate
     * @param tileY tile Y coordinate
     * @return true if the tile is highlighted for interaction
     * @Author Enid Becarevic
     */
    public boolean isInteractionTile(int tileX, int tileY) {
        for (int[] pos : interactionTiles)
            if (pos[0] == tileX && pos[1] == tileY) return true;
        return false;
    }

    /** @return list of movement tiles
     * @Author Enid Becarevic*/
    public List<int[]> getMovementTiles()    { return movementTiles;    }
    /** @return list of interaction tiles
     * @Author Enid Becarevic*/
    public List<int[]> getInteractionTiles() { return interactionTiles; }

    /**
     * Renders all highlight overlays using the given camera.
     * <p>Colors used:</p>
     * <ul>
     *     <li><b>Blue</b> – movement tiles</li>
     *     <li><b>Orange</b> – interaction/attack tiles</li>
     *     <li><b>Transparent blue</b> – general highlights</li>
     * </ul>
     *
     * @param camera the camera used for projection
     * @Author Enid Becarevic
     */
    public void render(OrthographicCamera camera) {
        if (highlightedTiles.isEmpty() && movementTiles.isEmpty() && interactionTiles.isEmpty()) return;
        if (shapeRenderer.isDrawing()) shapeRenderer.end();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0f, 0f, 1f, 0.35f);
        for (int[] pos : highlightedTiles) {
            int x = pos[0] * WorldMap.TILE_SIZE;
            int y = pos[1] * WorldMap.TILE_SIZE;
            shapeRenderer.rect(x, y, WorldMap.TILE_SIZE, WorldMap.TILE_SIZE);
        }

        if (movementTiles.isEmpty() && interactionTiles.isEmpty()) return;



        // Movement zone
        shapeRenderer.setColor(Color.BLUE);
        for (int[] pos : movementTiles)
            shapeRenderer.rect(pos[0] * WorldMap.TILE_SIZE,
                pos[1] * WorldMap.TILE_SIZE,
                WorldMap.TILE_SIZE, WorldMap.TILE_SIZE);

        // Interaction / attack zone
        shapeRenderer.setColor(Color.ORANGE);
        for (int[] pos : interactionTiles)
            shapeRenderer.rect(pos[0] * WorldMap.TILE_SIZE,
                pos[1] * WorldMap.TILE_SIZE,
                WorldMap.TILE_SIZE, WorldMap.TILE_SIZE);

        shapeRenderer.end();
    }
}
