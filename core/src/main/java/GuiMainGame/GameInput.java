package GuiMainGame;

import base.TurnManager;
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
    private TurnManager turnManager;
    private float btnX, btnY, btnW, btnH;

    public GameInput(TileController tileController, BasePopup basePopup,
                     UnitStatsPopup statsPopup, UnitController unitController,
                     List<Unit> units, List<UnitView> unitViews,
                     HighlightSystem highlightSystem,
                     TurnManager turnManager,
                     float btnX, float btnY, float btnW, float btnH) {
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
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        float realY = Gdx.graphics.getHeight() - screenY;
        float btnX = Gdx.graphics.getWidth() - btnW - 20;
        float btnY = 20;
        if(screenX >= btnX && screenX <= btnX + btnW && realY >= btnY && realY <= btnY + btnH){
            turnManager.endTurn();
            return true;
        }

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
            if(!highlightSystem.isHighlighted(tileX, tileY)){
                return false;
            }
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
