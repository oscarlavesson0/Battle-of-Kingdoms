package GuiMainGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import unit.Unit;
import unit.UnitController;

import java.util.ArrayList;
import java.util.List;

public class HighlightSystem {

    private final UnitController  unitController;
    private final ShapeRenderer   shapeRenderer;

    private List<int[]> movementTiles    = new ArrayList<>();

    private List<int[]> interactionTiles = new ArrayList<>();

    public HighlightSystem(UnitController unitController) {
        this.unitController = unitController;
        this.shapeRenderer  = new ShapeRenderer();
    }

    public void updateHighlight(Unit unit) {
        if (unit == null) { clear(); return; }
        movementTiles    = unitController.getMovementTiles(unit);
        interactionTiles = unitController.getInteractionZoneTiles(unit, movementTiles);
    }

    public void updateAttackOnlyHighlight(Unit unit) {
        if (unit == null) { clear(); return; }
        movementTiles.clear();
        interactionTiles = unitController.getInteractionZoneTiles(unit, movementTiles);
    }

    public void clear() {
        movementTiles.clear();
        interactionTiles.clear();
    }

    public boolean isHighlighted(int tileX, int tileY) {
        for (int[] pos : movementTiles)
            if (pos[0] == tileX && pos[1] == tileY) return true;
        return false;
    }

    public boolean isInteractionTile(int tileX, int tileY) {
        for (int[] pos : interactionTiles)
            if (pos[0] == tileX && pos[1] == tileY) return true;
        return false;
    }

    public List<int[]> getMovementTiles()    { return movementTiles;    }
    public List<int[]> getInteractionTiles() { return interactionTiles; }

    // rendering
    public void render() {
        if (movementTiles.isEmpty() && interactionTiles.isEmpty()) return;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

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
