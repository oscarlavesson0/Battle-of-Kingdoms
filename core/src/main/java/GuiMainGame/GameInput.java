package GuiMainGame;

import base.Player;
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
    private ActionMenu actionMenu;
    private int pendingTileX = -1;
    private int pendingTileY = -1;

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
        this.actionMenu = new ActionMenu();
    }

    public ActionMenu getActionMenu(){
        return actionMenu;
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

        if(actionMenu.isVisible()){
            ActionMenu.Action action = actionMenu.handleClick(screenX, realY);
            if(action != null) handleAction(action);
            return true;
        }

        // 5. Tile-koordinater
        int tileX = screenX / WorldMap.TILE_SIZE;
        int tileY = (Gdx.graphics.getHeight() - screenY) / WorldMap.TILE_SIZE;
        Tile[][] grid = tileController.getTileGrid();

        if (tileX < 0 || tileY < 0 || tileY >= grid.length || tileX >= grid[0].length)
            return false;

        Tile clickedTile = grid[tileY][tileX];
        Unit selected = unitController.getSelectedUnit();


        if (selected != null) {
            List<Unit> currentUnits =
                unitController.getUnitsForPlayer(turnManager.getCurrentPlayer());
            for (Unit u : currentUnits) {
                if (u == selected) continue;
                if (u.getX() == tileX && u.getY() == tileY) {
                    selectUnit(u);
                    return true;
                }
            }

            boolean inMovement    = highlightSystem.isHighlighted(tileX, tileY);
            boolean inInteraction = highlightSystem.isInteractionTile(tileX, tileY);
            boolean hasEnemy      = hasEnemyAt(tileX, tileY, selected);

            if (inMovement || (inInteraction && hasEnemy)) {
                boolean canMove   = inMovement
                    && !unitController.hasMovedThisTurn(selected);
                boolean canAttack = hasEnemy
                    && !unitController.hasAttackedThisTurn(selected);

                if (!canMove && !canAttack) {
                    deselect();
                    return true;
                }

                pendingTileX = tileX;
                pendingTileY = tileY;

                if (canMove && !canAttack) {
                    executeMove(selected);
                } else if (!canMove && canAttack) {
                    executeAttack(selected);
                } else {
                    float menuSX = tileX * WorldMap.TILE_SIZE;
                    float menuSY = tileY * WorldMap.TILE_SIZE;
                    actionMenu.show(menuSX, menuSY, true, true);
                }
                return true;
            }

            deselect();

            if (clickedTile.getBase() != null) {
                basePopup.show(clickedTile.getBase());
            }
            return true;
        }

        List<Unit> currentUnits =
            unitController.getUnitsForPlayer(turnManager.getCurrentPlayer());
        for (Unit u : currentUnits) {
            if (u.getX() == tileX && u.getY() == tileY) {
                selectUnit(u);
                return true;
            }
        }

        if (clickedTile.getBase() != null) {
            basePopup.show(clickedTile.getBase());
            return true;
        }

        return false;
    }
    private void handleAction(ActionMenu.Action action) {
        Unit selected = unitController.getSelectedUnit();
        if (selected == null) return;
        switch (action) {
            case MOVE   -> executeMove(selected);
            case ATTACK -> executeAttack(selected);
            case CANCEL -> deselect();
        }
    }

    private void executeMove(Unit selected) {
        UnitView selectedView = findViewFor(selected);
        if (unitController.moveSelectedUnit(pendingTileX, pendingTileY, selectedView)) {
            highlightSystem.clear();
        }
        pendingTileX = pendingTileY = -1;
    }

    private void executeAttack(Unit selected) {
        Unit target = getEnemyAt(pendingTileX, pendingTileY, selected);
        if (target == null) { deselect(); return; }

        int dist = Math.abs(selected.getX() - pendingTileX)
            + Math.abs(selected.getY() - pendingTileY);

        if (dist == 1) {
            unitController.attackUnit(selected, target);
        } else {
            int[] moveTile = findAdjacentMoveTile(
                highlightSystem.getMovementTiles(), pendingTileX, pendingTileY);
            if (moveTile != null) {
                UnitView selectedView = findViewFor(selected);
                unitController.moveAndAttack(selected, moveTile[0], moveTile[1], target, selectedView);
            }
        }

        highlightSystem.clear();
        pendingTileX = pendingTileY = -1;
    }

    private void selectUnit(Unit u) {
        if (unitController.hasMovedThisTurn(u) && unitController.hasAttackedThisTurn(u))
            return;

        unitController.selectUnit(u);

        System.out.println("Select: hasMoved=" + unitController.hasMovedThisTurn(u)
            + ", hasAttacked=" + unitController.hasAttackedThisTurn(u));

        if (unitController.hasMovedThisTurn(u)) {
            highlightSystem.updateAttackOnlyHighlight(u);
        } else {
            highlightSystem.updateHighlight(u);
        }
    }

    private void deselect() {
        unitController.selectUnit(null);
        highlightSystem.clear();
        pendingTileX = pendingTileY = -1;
    }

    private boolean hasEnemyAt(int tileX, int tileY, Unit friendly) {
        return getEnemyAt(tileX, tileY, friendly) != null;
    }

    private Unit getEnemyAt(int tileX, int tileY, Unit friendly) {
        for (Unit u : unitController.getAllUnits()) {
            if (u.isAlive()
                && u.getPlayer() != friendly.getPlayer()
                && u.getX() == tileX
                && u.getY() == tileY)
                return u;
        }
        return null;
    }

    private int[] findAdjacentMoveTile(List<int[]> movTiles, int tx, int ty) {
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        for (int[] t : movTiles) {
            for (int[] d : dirs) {
                if (t[0] + d[0] == tx && t[1] + d[1] == ty) return t;
            }
        }
        return null;
    }
    private UnitView findViewFor(Unit unit) {
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i) == unit) {
                return unitViews.get(i);
            }
        }
        return null;
    }
}
