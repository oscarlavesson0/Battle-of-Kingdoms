package GuiMainGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import popup.BasePopup;
import terrain.Tile;
import terrain.TileController;
import unit.Unit;
import unit.UnitController;

public class GameInput extends InputAdapter {

    private TileController tileController;
    private BasePopup basePopup;
    private UnitController unitController;
    private Unit unit;
    private HighlightSystem highlightSystem;

    public GameInput(TileController tileController, BasePopup basePopup,
                     UnitController unitController, Unit unit,
                     HighlightSystem highlightSystem) {

        this.tileController = tileController;
        this.basePopup = basePopup;
        this.unitController = unitController;
        this.unit = unit;
        this.highlightSystem = highlightSystem;
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        // If popup is open then popup handle clicks
        if (basePopup != null && basePopup.isVisible()) {
            basePopup.handleClick(screenX, screenY);
            return true;
        }

        // Convert screen coords to tile coords
        int tileX = screenX / WorldMap.TILE_SIZE;
        int tileY = (Gdx.graphics.getHeight() - screenY) / WorldMap.TILE_SIZE;

        Tile[][] grid = tileController.getTileGrid();

        // Bounds check
        if (tileX < 0 || tileY < 0 || tileY >= grid.length || tileX >= grid[0].length)
            return false;

        Tile clickedTile = grid[tileY][tileX];

        // Klick på unit → välj + highlight
        if (unit.getX() == tileX && unit.getY() == tileY) {
            unitController.selectUnit(unit);
            highlightSystem.updateHighlight(unit);
            return true;
        }

        // Klick på base → popup
        if (clickedTile.getBase() != null) {
            basePopup.show(clickedTile.getBase());
            return true;
        }

        // Klick på tile → försök flytta
        if (unitController.moveSelectedUnit(tileX, tileY)) {
            highlightSystem.clear();
            return true;
        }

        return false;
    }
}
