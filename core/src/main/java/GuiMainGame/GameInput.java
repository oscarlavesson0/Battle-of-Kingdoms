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
import popup.SettingsPopup;
import popup.ConfirmPopup;

import java.util.List;
/**
 * GameInput handles all player input for the main game screen.
 * It manages:
 * <ul>
 *     <li>Unit selection and movement</li>
 *     <li>Attack targeting</li>
 *     <li>Opening and interacting with popups</li>
 *     <li>Displaying and handling the ActionMenu</li>
 *     <li>Turn-ending logic</li>
 *     <li>Interaction with bases and buildings</li>
 * </ul>
 *
 * <p>The class acts as the central input router for the tactical game,
 * ensuring that UI layers (popups, menus) take priority over world clicks,
 * and that unit actions follow the correct state flow.</p>
 * @Author Oscar Lavesson
 * @Author Enid Becarevic
 * @Author Stefan Rajkovic
 * @Author JoelAxel Olsson
 */

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
    private SettingsPopup settingsPopup;
    private ConfirmPopup  confirmPopup;
    private float gearX, gearY, gearSize;

    /** Stores an enemy unit awaiting an INFO click in the action menu. */
    private Unit pendingEnemyInfoUnit = null;

    /**
     * Creates a new GameInput handler with all required controllers and UI components.
     *
     * @param tileController controller for terrain tiles
     * @param basePopup popup for base information
     * @param statsPopup popup for unit stats
     * @param buildingMenuPopup popup for building actions
     * @param buildingInfoPopup popup for building details
     * @param unitController controller for unit logic
     * @param units list of all units
     * @param unitViews list of unit view objects (rendering)
     * @param highlightSystem system for movement/attack highlighting
     * @param turnManager manages turn order and turn transitions
     * @param btnX X position of end-turn button
     * @param btnY Y position of end-turn button
     * @param btnW width of end-turn button
     * @param btnH height of end-turn button
     * @param bases list of all bases on the map
     * @param viewport camera viewport for coordinate conversion
     * @param settingsPopup popup for game settings
     * @param confirmPopup popup for confirmations
     * @param gearX X position of settings gear icon
     * @param gearY Y position of settings gear icon
     * @param gearSize size of settings gear icon
     * @Author Oscar Lavesson
     * @Author Enid Becarevic
     * @Author Stefan Rajkovic
     * @Author JoelAxel Olsson
     */
    public GameInput(TileController tileController, BasePopup basePopup,
                     UnitStatsPopup statsPopup,
                     BuildingMenuPopup buildingMenuPopup,
                     BuildingInfoPopup buildingInfoPopup,
                     UnitController unitController,
                     List<Unit> units, List<UnitView> unitViews,
                     HighlightSystem highlightSystem,
                     TurnManager turnManager,
                     float btnX, float btnY, float btnW, float btnH,
                     List<BaseStats> bases, Viewport viewport,
                     SettingsPopup settingsPopup, ConfirmPopup confirmPopup,
                     float gearX, float gearY, float gearSize) {
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
        this.settingsPopup      = settingsPopup;
        this.confirmPopup       = confirmPopup;
        this.gearX              = gearX;
        this.gearY              = gearY;
        this.gearSize           = gearSize;
    }

    /** @return the ActionMenu instance used for unit actions
     * @author Enid Becarevic
     * */
    public ActionMenu getActionMenu() { return actionMenu; }

    /**
     * Updates internal state, such as waiting for a unit to finish moving
     * before showing the post-move action menu.
     * @author Enid Becarevic
     */
    public void update() {
        if (pendingPostMoveUnit != null) {
            UnitView view = findViewFor(pendingPostMoveUnit);
            if (view != null && !view.isMoving()) {
                showPreWaitMenu(pendingPostMoveUnit);
                pendingPostMoveUnit = null;
            }
        }
    }

    /**
     * Shows the PRE_WAIT action menu after a unit finishes moving.
     *
     * @param unit the unit that just moved
     * @author Enid Becarevic
     * @author JoelAxel Olsson
     */
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

    /**
     * Handles all click input in the game world, including:
     * <ul>
     *     <li>UI popups</li>
     *     <li>Action menu</li>
     *     <li>Unit selection</li>
     *     <li>Movement and attack targeting</li>
     *     <li>Base and building interactions</li>
     *  @Author Oscar Lavesson
     *  @Author Enid Becarevic
     *  @Author Stefan Rajkovic
     *  @Author JoelAxel Olsson
     * </ul>
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        com.badlogic.gdx.math.Vector3 worldCoords =
            new com.badlogic.gdx.math.Vector3(screenX, screenY, 0);
        viewport.getCamera().unproject(worldCoords,
            viewport.getScreenX(), viewport.getScreenY(),
            viewport.getScreenWidth(), viewport.getScreenHeight());

        int worldX = (int)(worldCoords.x / WorldMap.TILE_SIZE);
        int worldY = (int)(worldCoords.y / WorldMap.TILE_SIZE);
        System.out.println("touchd " + worldX + " , " + worldY);
        float pixelX = worldCoords.x;
        float pixelY = worldCoords.y;
        float realY  = Gdx.graphics.getHeight() - screenY;

        // ConfirmPopup har högsta prioritet (ligger överst)
        if (confirmPopup != null && confirmPopup.isVisible()) {
            confirmPopup.handleClick(pixelX, pixelY);
            return true;
        }

        // SettingsPopup
        if (settingsPopup != null && settingsPopup.isVisible()) {
            settingsPopup.handleClick(pixelX, pixelY);
            return true;
        }

        // Kugghjul-knapp (öppnar settings) — world-koordinater
        if (pixelX >= gearX && pixelX <= gearX + gearSize &&
            pixelY >= gearY && pixelY <= gearY + gearSize) {
            settingsPopup.show();
            return true;
        }

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

    /**
     * Handles actions selected from the ActionMenu.
     *
     * @param action the selected action
     * @Author Enid Becarevic
     */
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

    /**
     * Cancels a unit's movement and returns it to its original tile.
     * @Author Enid Becarevic
     * @param unit the unit whose move is being cancelled
     * @
     */
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

    /**
     * Handles selecting a tile as an attack target.
     * @Author Enid Becarevic
     */
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

    /**
     * Selects a unit and shows the appropriate action menu depending on whether
     * the unit has already moved this turn.
     * @Author Enid Becarevic
     */
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

    /**
     * Shows the PRE_MOVE action menu for a unit.
     * @Author Enid Becarevic
     */
    private void showPreMoveMenu(Unit unit) {
        boolean canAttack = hasEnemyAdjacent(unit) || hasEnemyBaseAdjacent(unit) || hasEnemyBuildingAdjacent(unit);
        float wx = unit.getX() * WorldMap.TILE_SIZE;
        float wy = unit.getY() * WorldMap.TILE_SIZE;
        actionMenu.showPreMove(wx + WorldMap.TILE_SIZE, wy, canAttack);
    }

    /** Clears all selection and resets input state.
     * @Author Enid Becarevic*/
    private void deselect() {
        unitController.selectUnit(null);
        highlightSystem.clear();
        awaitingAttackTarget    = false;
        attackingUnit           = null;
        pendingPostMoveUnit     = null;
        pendingEnemyInfoUnit    = null;
    }

    /** Cancels all active UI and selection states.
     * @Author Enid Becarevic*/
    private void cancelAll() {
        deselect();
        actionMenu.hide();
    }

    /** Commits any pending movement before ending the turn.
     * @Author Enid Becarevic*/
    private void commitAnyPending() {
        Unit sel = unitController.getSelectedUnit();
        if (sel != null && unitController.hasPendingMove(sel))
            unitController.commitMove(sel);
    }

    //
    // Hjälpmetoder
    //
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
        Building enemyBuilding = tileController.getTileGrid()[tileY][tileX].getBuilding();
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
