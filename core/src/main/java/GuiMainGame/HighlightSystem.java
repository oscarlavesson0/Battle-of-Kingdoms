package GuiMainGame;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import unit.Unit;
import unit.UnitController;

import java.util.ArrayList;
import java.util.List;

public class HighlightSystem {

    private UnitController unitController;
    private ShapeRenderer shapeRenderer;

    private List<int[]> highlightedTiles = new ArrayList<>();

    public HighlightSystem(UnitController unitController) {
        this.unitController = unitController;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void updateHighlight(Unit unit) {
        if (unit == null) {
            highlightedTiles.clear();
            return;
        }
        highlightedTiles = unitController.getMovementTiles(unit);
    }

    public void clear() {
        highlightedTiles.clear();
    }

    public void render() {
        if (highlightedTiles.isEmpty()) return;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 1f, 0.35f);

        for (int[] pos : highlightedTiles) {
            int x = pos[0] * WorldMap.TILE_SIZE;
            int y = pos[1] * WorldMap.TILE_SIZE;
            shapeRenderer.rect(x, y, WorldMap.TILE_SIZE, WorldMap.TILE_SIZE);
        }

        shapeRenderer.end();
    }
}
