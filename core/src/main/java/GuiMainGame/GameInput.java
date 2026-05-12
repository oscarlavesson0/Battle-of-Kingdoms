package GuiMainGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import popup.BasePopup;
import popup.UnitStatsPopup;
import terrain.Tile;
import terrain.TileController;
import unit.Unit;
import unit.UnitController;
import java.util.List;

public class GameInput extends InputAdapter {
    private TileController tileController;
    private BasePopup basePopup;
    private UnitStatsPopup statsPopup;
    private UnitController unitController;
    private List<Unit> units;
    private List<UnitView> unitViews;
    private HighlightSystem highlightSystem;

    public GameInput(TileController tileController, BasePopup basePopup,
                     UnitStatsPopup statsPopup, UnitController unitController,
                     List<Unit> units, List<UnitView> unitViews,
                     HighlightSystem highlightSystem) {
        this.tileController = tileController;
        this.basePopup = basePopup;
        this.statsPopup = statsPopup;
        this.unitController = unitController;
        this.units = units;
        this.unitViews = unitViews;
        this.highlightSystem = highlightSystem;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (statsPopup != null && statsPopup.isVisible()) {
            statsPopup.handleClick(screenX, screenY);
            return true;
        }
        if (basePopup != null && basePopup.isVisible()) {
            basePopup.handleClick(screenX, screenY);
            return true;
        }

        int tileX = screenX / WorldMap.TILE_SIZE;
        int tileY = (Gdx.graphics.getHeight() - screenY) / WorldMap.TILE_SIZE;
        Tile[][] grid = tileController.getTileGrid();

        if (tileX < 0 || tileY < 0 || tileY >= grid.length || tileX >= grid[0].length)
            return false;

        Tile clickedTile = grid[tileY][tileX];

        // Klick på en unit = välj den och highlighta
        for (int i = 0; i < units.size(); i++) {
            Unit u = units.get(i);
            if (u.getX() == tileX && u.getY() == tileY) {
                unitController.selectUnit(u);
                highlightSystem.updateHighlight(u);
                return true;
            }
        }

        // Klick på bas = öppna popup
        if (clickedTile.getBase() != null) {
            basePopup.show(clickedTile.getBase());
            return true;
        }

        // Klick på tile = försök flytta markerad unit
        Unit selected = unitController.getSelectedUnit();
        if (selected != null) {
            UnitView selectedView = null;
            for (int i = 0; i < units.size(); i++) {
                if (units.get(i) == selected) {
                    selectedView = unitViews.get(i);
                    break;
                }
            }
            if (unitController.moveSelectedUnit(tileX, tileY, selectedView)) {
                highlightSystem.clear();
                return true;
            }
        }

        return false;
    }
}
