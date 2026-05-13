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
    private ActionMenu actionMenu;

    private Unit pendingPostMoveUnit = null;

    private boolean awaitingAttackTarget = false;
    private Unit attackingUnit = null;

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
        this.btnX = btnX; this.btnY = btnY;
        this.btnW = btnW; this.btnH = btnH;
        this.actionMenu = new ActionMenu();
    }

    public ActionMenu getActionMenu() { return actionMenu; }

    public void update() {
        if (pendingPostMoveUnit != null) {
            UnitView view = findViewFor(pendingPostMoveUnit);
            if (view != null && !view.isMoving()) {
                showPreWaitMenu(pendingPostMoveUnit);
                pendingPostMoveUnit = null;
            }
        }
    }

    private void showPreWaitMenu(Unit unit) {

        boolean canAttack = false;
        highlightSystem.updateAttackOnlyHighlight(unit);

        for (int[] tile : highlightSystem.getInteractionTiles()) {
            if (hasEnemyAt(tile[0], tile[1], unit)) {
                canAttack = true;
                break;
            }
        }

        float wx = unit.getX() * WorldMap.TILE_SIZE;
        float wy = unit.getY() * WorldMap.TILE_SIZE;

        actionMenu.showPreWait(wx + WorldMap.TILE_SIZE, wy, canAttack);
        unitController.selectUnit(unit);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        float realY = Gdx.graphics.getHeight() - screenY;

        // End turn
        float bx = Gdx.graphics.getWidth() - btnW - 20;
        float by = 20;
        if (screenX >= bx && screenX <= bx + btnW && realY >= by && realY <= by + btnH) {
            commitAnyPending();
            turnManager.endTurn();
            cancelAll();
            return true;
        }

        // Popups
        if (statsPopup != null && statsPopup.isVisible()) {
            statsPopup.handleClick(screenX, screenY);
            return true;
        }
        if (basePopup != null && basePopup.isVisible()) {
            basePopup.handleClick(screenX, screenY);
            return true;
        }

        // Action menu
        if (actionMenu.isVisible()) {
            ActionMenu.Action action = actionMenu.handleClick(screenX, realY);
            if (action != null) handleMenuAction(action);
            return true;
        }

        // Attack target
        if (awaitingAttackTarget) {
            handleAttackTargetClick(screenX, realY);
            return true;
        }

        // Tile click
        int tileX = screenX / WorldMap.TILE_SIZE;
        int tileY = (Gdx.graphics.getHeight() - screenY) / WorldMap.TILE_SIZE;

        Tile[][] grid = tileController.getTileGrid();
        if (tileX < 0 || tileY < 0 || tileY >= grid.length || tileX >= grid[0].length)
            return false;

        Tile clickedTile = grid[tileY][tileX];
        Unit selected    = unitController.getSelectedUnit();

        if (selected != null) {

            // Klick på annan unit → STÄNG BARA
            List<Unit> currentUnits =
                unitController.getUnitsForPlayer(turnManager.getCurrentPlayer());
            for (Unit u : currentUnits) {
                if (u == selected) continue;
                if (u.getX() == tileX && u.getY() == tileY) {
                    cancelAll();
                    return true;
                }
            }

            // Klick på blå ruta → flytta
            if (highlightSystem.isHighlighted(tileX, tileY)) {

                UnitView view      = findViewFor(selected);
                Unit     movedUnit = selected;

                if (unitController.moveSelectedUnit(tileX, tileY, view)) {

                    actionMenu.hide();
                    highlightSystem.clear();
                    pendingPostMoveUnit = movedUnit;
                }
                return true;
            }

            cancelAll();
            if (clickedTile.getBase() != null) basePopup.show(clickedTile.getBase());
            return true;
        }

        // Välj unit
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

    private void handleMenuAction(ActionMenu.Action action) {
        Unit selected = unitController.getSelectedUnit();
        if (selected == null) return;

        switch (action) {

            case MOVE -> {
                // Visa rörelsemarkering från PRE_MOVE
                highlightSystem.updateHighlight(selected);
            }

            case WAIT -> {
                // Oavsett om det är PRE_MOVE eller PRE_WAIT:
                // commit uniten och lås den för resten av rundan
                unitController.commitMove(selected);
                cancelAll();
            }

            case ATTACK -> {
                // Committa uniten på sin nuvarande plats innan attack
                // så den låses oavsett om den rört sig eller inte
                unitController.commitMove(selected);
                highlightSystem.updateAttackOnlyHighlight(selected);
                awaitingAttackTarget = true;
                attackingUnit        = selected;
            }

            case CANCEL -> {
                // Endast i PRE_WAIT: animera tillbaka till ursprungsposition
                cancelMoveForSelected(selected);
            }

            case CLOSE -> {
                cancelAll();
            }
        }
    }

    private void cancelMoveForSelected(Unit unit) {

        int[] origin = unitController.cancelMove(unit);
        if (origin == null) {
            cancelAll();
            return;
        }

        actionMenu.hide();

        UnitView view = findViewFor(unit);
        if (view != null) {
            view.setCancelPath(origin[0], origin[1]);
        } else {
            unit.setPosition(origin[0], origin[1]);
        }

        highlightSystem.clear();

        float wx = origin[0] * WorldMap.TILE_SIZE;
        float wy = origin[1] * WorldMap.TILE_SIZE;
        boolean canAttack = hasEnemyAdjacent(unit);

        actionMenu.showPreMove(wx + WorldMap.TILE_SIZE, wy, canAttack);

        unitController.selectUnit(unit);
    }

    private void handleAttackTargetClick(float screenX, float realY) {
        int tileX = (int) (screenX / WorldMap.TILE_SIZE);
        int tileY = (int) (realY   / WorldMap.TILE_SIZE);

        if (highlightSystem.isInteractionTile(tileX, tileY)) {
            Unit target = getEnemyAt(tileX, tileY, attackingUnit);
            if (target != null) {
                unitController.attackUnit(attackingUnit, target);
            }
        }

        cancelAll();
    }

    private void selectUnit(Unit u) {
        if (u == null) return;

        unitController.selectUnit(u);

        if (unitController.hasMovedThisTurn(u)) {
            // Uniten har redan väntat – visa after-wait menyn (bara Close)
            float wx = u.getX() * WorldMap.TILE_SIZE;
            float wy = u.getY() * WorldMap.TILE_SIZE;
            actionMenu.showAfterWait(wx + WorldMap.TILE_SIZE, wy);
            return;
        }

        showPreMoveMenu(u);
    }

    private void showPreMoveMenu(Unit unit) {
        boolean canAttack = hasEnemyAdjacent(unit);

        float wx = unit.getX() * WorldMap.TILE_SIZE;
        float wy = unit.getY() * WorldMap.TILE_SIZE;

        actionMenu.showPreMove(wx + WorldMap.TILE_SIZE, wy, canAttack);
    }

    private void deselect() {
        unitController.selectUnit(null);
        highlightSystem.clear();
        awaitingAttackTarget = false;
        attackingUnit        = null;
        pendingPostMoveUnit  = null;
    }

    private void cancelAll() {
        deselect();
        actionMenu.hide();
    }

    private void commitAnyPending() {
        Unit sel = unitController.getSelectedUnit();
        if (sel != null && unitController.hasPendingMove(sel))
            unitController.commitMove(sel);
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

    private UnitView findViewFor(Unit unit) {
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i) == unit) return unitViews.get(i);
        }
        return null;
    }

    private boolean hasEnemyAdjacent(Unit unit) {
        int x = unit.getX();
        int y = unit.getY();

        return hasEnemyAt(x + 1, y, unit) ||
            hasEnemyAt(x - 1, y, unit) ||
            hasEnemyAt(x, y + 1, unit) ||
            hasEnemyAt(x, y - 1, unit);
    }
}
