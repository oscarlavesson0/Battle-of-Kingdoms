package GuiMainGame;

import base.BaseStats;
import base.TurnManager;
import building.Building;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.viewport.Viewport;
import popup.BasePopup;
import popup.BuildingInfoPopup;
import popup.BuildingMenuPopup;
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
    private BuildingMenuPopup buildingMenuPopup;
    private BuildingInfoPopup buildingInfoPopup;
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
    private Viewport viewport;
    private List<BaseStats> bases;

    // Håller koll på fiende-unit som väntar på Info-klick i menyn
    private Unit pendingEnemyInfoUnit = null;

    public GameInput(TileController tileController, BasePopup basePopup,
                     UnitStatsPopup statsPopup,
                     BuildingMenuPopup buildingMenuPopup,
                     BuildingInfoPopup buildingInfoPopup,
                     UnitController unitController,
                     List<Unit> units, List<UnitView> unitViews,
                     HighlightSystem highlightSystem,
                     TurnManager turnManager,
                     float btnX, float btnY, float btnW, float btnH,
                     List<BaseStats> bases, Viewport viewport) {
        this.tileController     = tileController;
        this.basePopup          = basePopup;
        this.statsPopup         = statsPopup;
        this.buildingMenuPopup  = buildingMenuPopup;
        this.buildingInfoPopup  = buildingInfoPopup;
        this.unitController     = unitController;
        this.units              = units;
        this.unitViews          = unitViews;
        this.highlightSystem    = highlightSystem;
        this.turnManager        = turnManager;
        this.btnX = btnX; this.btnY = btnY;
        this.btnW = btnW; this.btnH = btnH;
        this.actionMenu         = new ActionMenu();
        this.bases              = bases;
        this.viewport           = viewport;
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
            if (hasEnemyAt(tile[0], tile[1], unit))     { canAttack = true; break; }
            if (hasEnemyBaseAt(tile[0], tile[1], unit)) { canAttack = true; break; }
            if (hasEnemyBuildingAt(tile[0], tile[1], unit)) { canAttack = true; break; }
        }
        float wx = unit.getX() * WorldMap.TILE_SIZE;
        float wy = unit.getY() * WorldMap.TILE_SIZE;
        actionMenu.showPreWait(wx + WorldMap.TILE_SIZE, wy, canAttack);
        unitController.selectUnit(unit);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        com.badlogic.gdx.math.Vector3 worldCoords =
            new com.badlogic.gdx.math.Vector3(screenX, screenY, 0);
        viewport.getCamera().unproject(worldCoords,
            viewport.getScreenX(), viewport.getScreenY(),
            viewport.getScreenWidth(), viewport.getScreenHeight());

        int worldX = (int)(worldCoords.x / WorldMap.TILE_SIZE);
        int worldY = (int)(worldCoords.y / WorldMap.TILE_SIZE);

        float pixelX = worldCoords.x;
        float pixelY = worldCoords.y;
        float realY  = Gdx.graphics.getHeight() - screenY;

        // Slut-tur-knapp
        float bx = Gdx.graphics.getWidth() - btnW - 20;
        float by = 20;
        if (screenX >= bx && screenX <= bx + btnW && realY >= by && realY <= by + btnH) {
            commitAnyPending();
            turnManager.endTurn();
            cancelAll();
            return true;
        }

        // Popups har prioritet
        if (buildingMenuPopup != null && buildingMenuPopup.isVisible()) {
            buildingMenuPopup.handleClick(pixelX, pixelY);
            return true;
        }
        if (statsPopup != null && statsPopup.isVisible()) {
            statsPopup.handleClick(pixelX, pixelY);
            return true;
        }
        if (basePopup != null && basePopup.isVisible()) {
            basePopup.handleClick(pixelX, pixelY);
            return true;
        }
        if (buildingInfoPopup != null && buildingInfoPopup.isVisible()) {
            buildingInfoPopup.handleClick(pixelX, pixelY);
            return true;
        }

        // Action-meny
        if (actionMenu.isVisible()) {
            ActionMenu.Action action = actionMenu.handleClick(pixelX, pixelY);
            if (action != null) handleMenuAction(action);
            return true;
        }

        // Attack-mål
        if (awaitingAttackTarget) {
            handleAttackTargetClick(screenX, realY);
            return true;
        }

        // Tile-klick
        Tile[][] grid = tileController.getTileGrid();
        if (worldX < 0 || worldY < 0 || worldY >= grid.length || worldX >= grid[0].length)
            return false;

        Tile clickedTile = grid[worldY][worldX];
        Unit selected    = unitController.getSelectedUnit();

        if (selected != null) {
            // Klick på annan egen unit → avbryt
            List<Unit> currentUnits =
                unitController.getUnitsForPlayer(turnManager.getCurrentPlayer());
            for (Unit u : currentUnits) {
                if (u == selected) continue;
                if (u.getX() == worldX && u.getY() == worldY) {
                    cancelAll();
                    return true;
                }
            }
            // Klick på markerad ruta → flytta
            if (highlightSystem.isHighlighted(worldX, worldY)) {
                UnitView view    = findViewFor(selected);
                Unit     movedUnit = selected;
                if (unitController.moveSelectedUnit(worldX, worldY, view)) {
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

        // Ingen unit vald – kolla vad som klickades
        Unit clickedUnit = null;
        for (Unit u : unitController.getAllUnits()) {
            if (u.getX() == worldX && u.getY() == worldY) {
                clickedUnit = u;
                break;
            }
        }

        if (clickedUnit != null) {
            if (clickedUnit.getPlayer() != turnManager.getCurrentPlayer()) {
                // Fiendeunit → visa ENEMY_INFO-meny med bara Info-knapp
                pendingEnemyInfoUnit = clickedUnit;
                float wx = clickedUnit.getX() * WorldMap.TILE_SIZE;
                float wy = clickedUnit.getY() * WorldMap.TILE_SIZE;
                actionMenu.showEnemyInfoMenu(wx + WorldMap.TILE_SIZE, wy);
            } else {
                // Egen unit → vanlig meny
                selectUnit(clickedUnit);
            }
            return true;
        }

        if (clickedTile.getBase() != null) {
            basePopup.show(clickedTile.getBase());
            return true;
        }
        if (clickedTile.getBuilding() != null) {
            buildingInfoPopup.open(clickedTile.getBuilding());
            return true;
        }
        return false;
    }

    private void handleMenuAction(ActionMenu.Action action) {
        // Om vi är i fiende-info-läget, hantera det separat
        if (action == ActionMenu.Action.INFO && pendingEnemyInfoUnit != null) {
            statsPopup.showUnitInfo(pendingEnemyInfoUnit);
            pendingEnemyInfoUnit = null;
            return;
        }

        Unit selected = unitController.getSelectedUnit();
        if (selected == null) return;

        switch (action) {
            case MOVE   -> highlightSystem.updateHighlight(selected);
            case WAIT   -> { unitController.commitMove(selected); cancelAll(); }
            case ATTACK -> {
                unitController.commitMove(selected);
                highlightSystem.updateAttackOnlyHighlight(selected);
                awaitingAttackTarget = true;
                attackingUnit = selected;
            }
            case CANCEL -> cancelMoveForSelected(selected);
            case INFO   -> statsPopup.showUnitInfo(selected);
            case CLOSE  -> cancelAll();
        }
    }

    private void cancelMoveForSelected(Unit unit) {
        int[] origin = unitController.cancelMove(unit);
        if (origin == null) { cancelAll(); return; }
        actionMenu.hide();
        UnitView view = findViewFor(unit);
        if (view != null) view.setCancelPath(origin[0], origin[1]);
        else unit.setPosition(origin[0], origin[1]);
        highlightSystem.clear();
        float wx = origin[0] * WorldMap.TILE_SIZE;
        float wy = origin[1] * WorldMap.TILE_SIZE;
        boolean canAttack = hasEnemyAdjacent(unit);
        actionMenu.showPreMove(wx + WorldMap.TILE_SIZE, wy, canAttack);
        unitController.selectUnit(unit);
    }

    private void handleAttackTargetClick(float screenX, float screenY) {
        com.badlogic.gdx.math.Vector3 worldCoords =
            new com.badlogic.gdx.math.Vector3(screenX, screenY, 0);
        viewport.getCamera().unproject(worldCoords,
            viewport.getScreenX(), viewport.getScreenY(),
            viewport.getScreenWidth(), viewport.getScreenHeight());
        int tileX = (int)(worldCoords.x / WorldMap.TILE_SIZE);
        int tileY = 59 - (int)(worldCoords.y / WorldMap.TILE_SIZE);

        if (highlightSystem.isInteractionTile(tileX, tileY)) {
            System.out.println("target unit time ----------");
            Unit target = getEnemyAt(tileX, tileY, attackingUnit);
            if (target != null) {
                unitController.attackUnit(attackingUnit, target);
                cancelAll();
                return;
            }
            BaseStats targetBase = getEnemyBaseAt(tileX, tileY, attackingUnit);
            if (targetBase != null) {
                unitController.attackBase(attackingUnit, targetBase);
                cancelAll();
                return;
            }
            System.out.println("targetBuidling time -----------------");
            Building targetBuilding = getEnemyBuildingAt(tileX, tileY, attackingUnit);
            if (targetBuilding != null) {
                unitController.attackBuilding(attackingUnit, targetBuilding);
                cancelAll();;
                return;
            }
        }
        cancelAll();
    }

    private void selectUnit(Unit u) {
        if (u == null) return;
        unitController.selectUnit(u);
        if (unitController.hasMovedThisTurn(u)) {
            float wx = u.getX() * WorldMap.TILE_SIZE;
            float wy = u.getY() * WorldMap.TILE_SIZE;
            actionMenu.showAfterWait(wx + WorldMap.TILE_SIZE, wy);
            return;
        }
        showPreMoveMenu(u);
    }

    private void showPreMoveMenu(Unit unit) {
        boolean canAttack = hasEnemyAdjacent(unit) || hasEnemyBaseAdjacent(unit);
        float wx = unit.getX() * WorldMap.TILE_SIZE;
        float wy = unit.getY() * WorldMap.TILE_SIZE;
        actionMenu.showPreMove(wx + WorldMap.TILE_SIZE, wy, canAttack);
    }

    private void deselect() {
        unitController.selectUnit(null);
        highlightSystem.clear();
        awaitingAttackTarget    = false;
        attackingUnit           = null;
        pendingPostMoveUnit     = null;
        pendingEnemyInfoUnit    = null;
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

    // -----------------------------------------------------------------------
    // Hjälpmetoder
    // -----------------------------------------------------------------------
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

    private boolean hasEnemyAdjacent(Unit unit) {
        int x = unit.getX(), y = unit.getY();
        return hasEnemyAt(x+1,y,unit) || hasEnemyAt(x-1,y,unit)
            || hasEnemyAt(x,y+1,unit) || hasEnemyAt(x,y-1,unit);
    }

    private BaseStats getEnemyBaseAt(int tileX, int tileY, Unit friendly) {
        if (bases == null) return null;
        for (BaseStats b : bases) {
            if (b.isDestroyed()) continue;
            if (b.getOwner() == friendly.getPlayer()) continue;
            int bx = b.getPosition().getX();
            int by = b.getPosition().getY();
            if (tileX >= bx && tileX < bx + 4 && tileY >= by && tileY < by + 4)
                return b;
            for (int dr = 0; dr < 4; dr++) {
                for (int dc = 0; dc < 4; dc++) {
                    int tx = bx + dc, ty = by + dr;
                    if ((Math.abs(tileX - tx) == 1 && tileY == ty) ||
                        (Math.abs(tileY - ty) == 1 && tileX == tx))
                        return b;
                }
            }
        }
        return null;
    }

    private boolean hasEnemyBaseAt(int tileX, int tileY, Unit friendly) {
        return getEnemyBaseAt(tileX, tileY, friendly) != null;
    }

    private boolean hasEnemyBaseAdjacent(Unit unit) {
        int x = unit.getX(), y = unit.getY();
        return hasEnemyBaseAt(x+1,y,unit) || hasEnemyBaseAt(x-1,y,unit)
            || hasEnemyBaseAt(x,y+1,unit) || hasEnemyBaseAt(x,y-1,unit);
    }

    private Building getEnemyBuildingAt(int tileX, int tileY, Unit friendly){
        Building enemyBuilding = tileController.getTileGrid()[tileX][tileY].getBuilding();
        System.out.println("X: " + tileX + " Y: " + tileY + " b: " + enemyBuilding);
        if (enemyBuilding != null) {
            if (enemyBuilding.getOwner() != friendly.getPlayer()){
                return enemyBuilding;
            }
        }
        return null;
    }

    private boolean hasEnemyBuildingAt(int tileX, int tileY, Unit friendly){
        return getEnemyBuildingAt(tileX, tileY, friendly) != null;
    }

    private boolean hasEnemyBuildingAdjacent(Unit unit){
        int x = unit.getX(), y = unit.getY();
        return hasEnemyBuildingAt(x+1,y,unit) || hasEnemyBuildingAt(x-1,y,unit)
            || hasEnemyBuildingAt(x, y+1,unit) || hasEnemyBuildingAt(x, y-1,unit);
    }

    private UnitView findViewFor(Unit unit) {
        for (int i = 0; i < units.size(); i++)
            if (units.get(i) == unit) return unitViews.get(i);
        return null;
    }
}
