package GuiMainGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import popup.BasePopup;
import popup.UnitStatsPopup;
import terrain.Tile;
import terrain.TileController;
import unit.Unit;
import unit.UnitController;
import unit.UnitSkapare;

public class GameInput extends InputAdapter {

    private TileController tileController;
    private BasePopup basePopup;
    private UnitStatsPopup statsPopup;
    private UnitController unitController;
    private Unit unit;
    private HighlightSystem highlightSystem;

    public GameInput(TileController tileController, BasePopup basePopup, UnitStatsPopup statsPopup,
                     UnitController unitController, Unit unit,
                     HighlightSystem highlightSystem) {

        this.tileController = tileController;
        this.basePopup = basePopup;
        this.statsPopup = statsPopup;
        this.unitController = unitController;
        this.unit = unit;
        this.highlightSystem = highlightSystem;
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        //If stats-popup is open, let stats-popup handle clicks
        if(statsPopup != null && statsPopup.isVisible()){
            statsPopup.handleClick(screenX, screenY);
            return true;
        }

        // If popup is open, let popup handle clicks
        if (basePopup != null && basePopup.isVisible()) {
            basePopup.handleClick(screenX, screenY);
            return true;
        }

        int tileX = screenX / WorldMap.TILE_SIZE;
        int tileY = (Gdx.graphics.getHeight() - screenY) / WorldMap.TILE_SIZE;

        Tile[][] grid = tileController.getTileGrid();

        // Bounds check
        if (tileX < 0 || tileY < 0 || tileY >= grid.length || tileX >= grid[0].length)
            return false;

        Tile clickedTile = grid[tileY][tileX];

        // Click on unit = select + highlight
        if (unit.getX() == tileX && unit.getY() == tileY) {
            unitController.selectUnit(unit);
            highlightSystem.updateHighlight(unit);
            return true;
        }

        // Click on base = open popup
        if (clickedTile.getBase() != null) {
            basePopup.show(clickedTile.getBase());
            return true;
        }

        // Click on tile = try to move unit
        if (unitController.moveSelectedUnit(tileX, tileY)) {
            highlightSystem.clear();
            return true;
        }

        return false;
    }


}
