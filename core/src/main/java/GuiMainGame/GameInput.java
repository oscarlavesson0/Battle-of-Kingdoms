package GuiMainGame;

import base.TurnManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import popup.BasePopup;
import terrain.Tile;
import terrain.TileController;
import unit.Unit;
import unit.UnitController;

import java.util.List;

public class GameInput extends InputAdapter {

    private final TileController  tileController;
    private final BasePopup       basePopup;
    private final UnitController  unitController;
    private final HighlightSystem highlightSystem;
    private final TurnManager     turnManager;
    private final ActionMenu      actionMenu;

    private int pendingTileX = -1;
    private int pendingTileY = -1;

    public GameInput(TileController tileController,
                     BasePopup      basePopup,
                     UnitController unitController,
                     HighlightSystem highlightSystem,
                     TurnManager    turnManager) {
        this.tileController  = tileController;
        this.basePopup       = basePopup;
        this.unitController  = unitController;
        this.highlightSystem = highlightSystem;
        this.turnManager     = turnManager;
        this.actionMenu      = new ActionMenu();
    }

    public ActionMenu getActionMenu() { return actionMenu; }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        float realY = Gdx.graphics.getHeight() - screenY;

        // 1. Base popup is open
        if (basePopup != null && basePopup.isVisible()) {
            basePopup.handleClick(screenX, screenY);
            return true;
        }

        // 2. Action menu is open
        if (actionMenu.isVisible()) {
            ActionMenu.Action action = actionMenu.handleClick(screenX, realY);
            if (action != null) handleAction(action);
            return true;
        }

        // 3. End-turn button (bottom-left strip)
        if (screenX >= 10 && screenX <= 150 && realY >= 10 && realY <= 40) {
            turnManager.endTurn();
            highlightSystem.clear();
            unitController.resetMovement();
            return true;
        }

        // 4. Tile click
        int tileX = screenX / WorldMap.TILE_SIZE;
        int tileY = (int) realY / WorldMap.TILE_SIZE;

        Tile[][] grid = tileController.getTileGrid();
        if (tileX < 0 || tileY < 0 || tileY >= grid.length || tileX >= grid[0].length)
            return false;

        Tile clickedTile = grid[tileY][tileX];
        Unit selected    = unitController.getSelectedUnit();

        //4a. A unit is already selected
        if (selected != null) {
            // Clicked a different own unit → switch selection
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
                    // Nothing available – deselect
                    deselect();
                    return true;
                }

                pendingTileX = tileX;
                pendingTileY = tileY;

                if (canMove && !canAttack) {
                    // Only option: just move
                    executeMove(selected);
                } else if (!canMove && canAttack) {
                    // Only option: just attack
                    executeAttack(selected);
                } else {
                    // Both possible: show menu at the clicked tile
                    float menuSX = tileX * WorldMap.TILE_SIZE;
                    float menuSY = tileY * WorldMap.TILE_SIZE;
                    actionMenu.show(menuSX, menuSY, true, true);
                }
                return true;
            }

            // Clicked outside any highlighted zone → deselect
            deselect();

            // Still honour base click after deselect
            if (clickedTile.getBase() != null) {
                basePopup.show(clickedTile.getBase());
            }
            return true;
        }

        // 4b. No unit selected – try selecting one
        List<Unit> currentUnits =
            unitController.getUnitsForPlayer(turnManager.getCurrentPlayer());
        for (Unit u : currentUnits) {
            if (u.getX() == tileX && u.getY() == tileY) {
                selectUnit(u);
                return true;
            }
        }

        // 4c. Click on a base
        if (clickedTile.getBase() != null) {
            basePopup.show(clickedTile.getBase());
            return true;
        }

        return false;
    }

    // action dispatch

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
        if (unitController.moveSelectedUnit(pendingTileX, pendingTileY)) {
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
            // Already adjacent – attack in place
            unitController.attackUnit(selected, target);
        } else {
            // In interaction zone but not adjacent – need to step closer first
            int[] moveTile = findAdjacentMoveTile(
                highlightSystem.getMovementTiles(), pendingTileX, pendingTileY);
            if (moveTile != null) {
                unitController.moveAndAttack(selected, moveTile[0], moveTile[1], target);
            }
        }

        highlightSystem.clear();
        pendingTileX = pendingTileY = -1;
    }

    // helpers

    private void selectUnit(Unit u) {
        // Allow re-selection if the unit still has an action left
        if (unitController.hasMovedThisTurn(u) && unitController.hasAttackedThisTurn(u))
            return;

        unitController.selectUnit(u);
        unitController.setActiveUnitView(unitController.getViewForUnit(u));

        if (unitController.hasMovedThisTurn(u)) {
            // Can only attack: show interaction zone only
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
}
