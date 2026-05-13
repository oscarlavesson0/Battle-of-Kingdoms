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

    //  Konstruktor
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
    //  update() – anropas varje frame från GameScreen.render()
    public void update() {
        if (pendingPostMoveUnit != null) {
            UnitView view = findViewFor(pendingPostMoveUnit);
            if (view != null && !view.isMoving()) {
                showPostMoveMenu(pendingPostMoveUnit, view);
                pendingPostMoveUnit = null;
            }
        }
    }

    //  Post-move meny
    private void showPostMoveMenu(Unit unit, UnitView view) {
        highlightSystem.updateAttackOnlyHighlight(unit);

        boolean hasEnemy = false;
        for (int[] tile : highlightSystem.getInteractionTiles()) {
            if (hasEnemyAt(tile[0], tile[1], unit)) { hasEnemy = true; break; }
        }

        float wx = unit.getX() * WorldMap.TILE_SIZE;
        float wy = unit.getY() * WorldMap.TILE_SIZE;
        actionMenu.showPostMove(wx + WorldMap.TILE_SIZE, wy, hasEnemy);

        unitController.selectUnit(unit);
    }

    //  Input
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        float realY = Gdx.graphics.getHeight() - screenY;

        // 1. End-turn-knapp
        float bx = Gdx.graphics.getWidth() - btnW - 20;
        float by = 20;
        if (screenX >= bx && screenX <= bx + btnW && realY >= by && realY <= by + btnH) {
            commitAnyPending();
            turnManager.endTurn();
            cancelAll();
            return true;
        }

        // 2. Popups
        if (statsPopup != null && statsPopup.isVisible()) {
            statsPopup.handleClick(screenX, screenY);
            return true;
        }
        if (basePopup != null && basePopup.isVisible()) {
            basePopup.handleClick(screenX, screenY);
            return true;
        }

        // 3. Post-move action-meny
        if (actionMenu.isVisible()) {
            ActionMenu.Action action = actionMenu.handleClick(screenX, realY);
            if (action != null) handleMenuAction(action);
            return true;
        }

        // 4. Väntar på attack-target
        if (awaitingAttackTarget) {
            handleAttackTargetClick(screenX, realY);
            return true;
        }

        // 5. Vanlig tile-klick
        int tileX = screenX / WorldMap.TILE_SIZE;
        int tileY = (Gdx.graphics.getHeight() - screenY) / WorldMap.TILE_SIZE;

        Tile[][] grid = tileController.getTileGrid();
        if (tileX < 0 || tileY < 0 || tileY >= grid.length || tileX >= grid[0].length)
            return false;

        Tile clickedTile = grid[tileY][tileX];
        Unit selected    = unitController.getSelectedUnit();

        if (selected != null) {
            // Klick på annan egen enhet – byt val
            List<Unit> currentUnits =
                unitController.getUnitsForPlayer(turnManager.getCurrentPlayer());
            for (Unit u : currentUnits) {
                if (u == selected) continue;
                if (u.getX() == tileX && u.getY() == tileY) {
                    deselect();
                    selectUnit(u);
                    return true;
                }
            }

            // Klick på blå rörelseruta → flytta, vänta på animation
            if (highlightSystem.isHighlighted(tileX, tileY)) {
                UnitView view      = findViewFor(selected);
                Unit     movedUnit = selected;
                if (unitController.moveSelectedUnit(tileX, tileY, view)) {
                    highlightSystem.clear();
                    pendingPostMoveUnit = movedUnit;
                }
                return true;
            }

            deselect();
            if (clickedTile.getBase() != null) basePopup.show(clickedTile.getBase());
            return true;
        }

        // Välj enhet
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

    //  Meny-hantering
    private void handleMenuAction(ActionMenu.Action action) {
        Unit selected = unitController.getSelectedUnit();
        switch (action) {
            case ATTACK -> {
                if (selected != null) {
                    unitController.commitMove(selected);
                    awaitingAttackTarget = true;
                    attackingUnit        = selected;
                }
            }
            case WAIT -> {
                if (selected != null) unitController.commitMove(selected);
                highlightSystem.clear();
                unitController.selectUnit(null);
            }
            case CANCEL -> cancelMoveForSelected(selected);
        }
    }

    private void cancelMoveForSelected(Unit unit) {
        if (unit == null) { cancelAll(); return; }

        int[] origin = unitController.cancelMove(unit);
        if (origin == null) { cancelAll(); return; }

        UnitView view = findViewFor(unit);
        if (view != null) {
            // setCancelPath sätter unit.setPosition(origin) när animationen är klar
            view.setCancelPath(origin[0], origin[1]);
        } else {
            unit.setPosition(origin[0], origin[1]);
        }

        highlightSystem.clear();
        actionMenu.hide();
        unitController.selectUnit(null);
        awaitingAttackTarget = false;
        attackingUnit        = null;
    }

    //  Attack-target
    private void handleAttackTargetClick(float screenX, float realY) {
        int tileX = (int) (screenX / WorldMap.TILE_SIZE);
        int tileY = (int) (realY   / WorldMap.TILE_SIZE);

        if (highlightSystem.isInteractionTile(tileX, tileY)) {
            Unit target = getEnemyAt(tileX, tileY, attackingUnit);
            if (target != null) {
                unitController.attackUnit(attackingUnit, target);
            }
        }

        highlightSystem.clear();
        awaitingAttackTarget = false;
        attackingUnit        = null;
        unitController.selectUnit(null);
    }

    //  Hjälpmetoder
    private void selectUnit(Unit u) {
        if (unitController.hasMovedThisTurn(u) && unitController.hasAttackedThisTurn(u))
            return;
        unitController.selectUnit(u);
        if (unitController.hasMovedThisTurn(u)) {
            UnitView view = findViewFor(u);
            if (view != null) showPostMoveMenu(u, view);
        } else {
            highlightSystem.updateHighlight(u);
        }
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
}
