package GuiMainGame;

import base.TurnManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
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
    private TurnManager turnManager;
    private float btnX, btnY, btnW, btnH;
    private Viewport viewport;

    public GameInput(TileController tileController, BasePopup basePopup,
                     UnitStatsPopup statsPopup, UnitController unitController,
                     List<Unit> units, List<UnitView> unitViews,
                     HighlightSystem highlightSystem,
                     TurnManager turnManager,
                     float btnX, float btnY, float btnW, float btnH, Viewport viewport) {
        this.tileController = tileController;
        this.basePopup = basePopup;
        this.statsPopup = statsPopup;
        this.unitController = unitController;
        this.units = units;
        this.unitViews = unitViews;
        this.highlightSystem = highlightSystem;
        this.turnManager = turnManager;
        this.btnX = btnX;
        this.btnY = btnY;
        this.btnW = btnW;
        this.btnH = btnH;
        this.viewport = viewport;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector3 worldCoords = new Vector3(screenX, screenY, 0);
        viewport.getCamera().unproject(worldCoords,
            viewport.getScreenX(), viewport.getScreenY(),
            viewport.getScreenWidth(), viewport.getScreenHeight());

        float worldX = worldCoords.x;
        float worldY = worldCoords.y;

        int tileX = (int) (worldX / WorldMap.TILE_SIZE);
        int tileY = (int) (worldY / WorldMap.TILE_SIZE);

        // Popup-klick använder också världskoordinater
        if (statsPopup != null && statsPopup.isVisible()) {
            statsPopup.handleClick(worldX, viewport.getWorldHeight() - worldY);
            return true;
        }
        if (basePopup != null && basePopup.isVisible()) {
            basePopup.handleClick(worldX, viewport.getWorldHeight() - worldY);
            return true;
        }

        // End Turn-knapp (HUD ligger i skärmkoordinater — använd råa koordinater)
        float rawY = Gdx.graphics.getHeight() - screenY;
        if (screenX >= btnX && screenX <= btnX + btnW &&
            rawY >= btnY && rawY <= btnY + btnH) {
            turnManager.endTurn();
            return true;
        }

        Tile[][] grid = tileController.getTileGrid();
        if (tileX < 0 || tileY < 0 || tileY >= grid.length || tileX >= grid[0].length)
            return false;

        Tile clickedTile = grid[tileY][tileX];

        // Klick på unit
        for (Unit u : units) {
            if (u.getX() == tileX && u.getY() == tileY) {
                unitController.selectUnit(u);
                highlightSystem.updateHighlight(u);
                return true;
            }
        }

        // Klick på bas
        if (clickedTile.getBase() != null) {
            basePopup.show(clickedTile.getBase());
            return true;
        }

        Unit selected = unitController.getSelectedUnit();
        if (selected != null) {
            // Hitta UnitView som tillhör den valda uniten
            UnitView selectedView = null;
            for (UnitView uv : unitViews) {
                if (uv.getUnit() == selected) {
                    selectedView = uv;
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
