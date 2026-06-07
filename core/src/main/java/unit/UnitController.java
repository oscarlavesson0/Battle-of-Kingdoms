package unit;

import GuiMainGame.UnitView;
import base.BaseDestroyedListener;
import base.BaseStats;
import building.Building;
import terrain.Tile;
import terrain.Water;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Controls units on the tile grid: selection, movement, attacks and spawning.
 *
 * @author Enid Becarevic
 * @author Emil Hadzic
 * @author Oscar Lavesson
 * @author JoelAxel Olsson
 */
public class UnitController {

    private final Tile[][] tileGrid;
    private Unit selectedUnit;
    private final List<Unit> units = new ArrayList<>();
    private final List<Unit> movedThisTurn = new ArrayList<>();
    private final List<Unit> attackedThisTurn = new ArrayList<>();
    private final Map<Unit, int[]> pendingMoveOrigin = new HashMap<>();

    private BaseDestroyedListener baseDestroyedListener;

    /**
     * Sets a listener that is notified when a base is destroyed.
     *
     * @param l listener to register
     */
    public void setBaseDestroyedListener(BaseDestroyedListener l) {
        this.baseDestroyedListener = l;
    }

    /**
     * Creates a controller for managing units on the given tile grid.
     *
     * @param tileGrid the grid of tiles
     */
    public UnitController(Tile[][] tileGrid) {
        this.tileGrid = tileGrid;
    }

    /**
     * Selects a unit for movement or actions.
     *
     * @param unit the unit to select
     */
    public void selectUnit(Unit unit) {
        this.selectedUnit = unit;
    }

    /**
     * Returns the currently selected unit.
     *
     * @return selected unit, or null
     */
    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    /**
     * Attempts to move the selected unit toward a target tile.
     *
     * @param targetX  target X coordinate
     * @param targetY  target Y coordinate
     * @param unitView view used for movement animation; may be null
     * @return true if movement started, false otherwise
     */
    public boolean moveSelectedUnit(int targetX, int targetY, UnitView unitView) {
        if (selectedUnit == null) {
            return false;
        }
        if (movedThisTurn.contains(selectedUnit)) {
            return false;
        }

        List<int[]> path = Pathfinder.findPath(tileGrid, selectedUnit, targetX, targetY);
        if (path.isEmpty()) {
            return false;
        }

        int maxSteps = selectedUnit.getSpeed();
        int steps = Math.min(maxSteps, path.size());
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < steps; i++) {
            queue.add(path.get(i));
        }

        pendingMoveOrigin.put(selectedUnit, new int[]{selectedUnit.getX(), selectedUnit.getY()});

        tileGrid[selectedUnit.getY()][selectedUnit.getX()].setUnit(null);
        tileGrid[targetY][targetX].setUnit(selectedUnit);
        if (unitView != null) {
            unitView.setMovementPath(queue);
        }
        selectedUnit = null;
        return true;
    }

    /**
     * Confirms a unit's movement for this turn.
     *
     * @param unit the unit that moved
     */
    public void commitMove(Unit unit) {
        pendingMoveOrigin.remove(unit);
        if (!movedThisTurn.contains(unit)) {
            movedThisTurn.add(unit);
        }
    }

    /**
     * Cancels a pending movement and returns the original position.
     *
     * @param unit the unit whose movement is cancelled
     * @return original position {x, y}, or null if no pending move exists
     */
    public int[] cancelMove(Unit unit) {
        int[] origin = pendingMoveOrigin.remove(unit);
        if (origin == null) {
            return null;
        }
        tileGrid[unit.getY()][unit.getX()].setUnit(null);
        tileGrid[origin[1]][origin[0]].setUnit(unit);
        return origin;
    }

    /**
     * Checks if a unit has a pending movement.
     *
     * @param unit the unit to check
     * @return true if the unit has a pending move
     */
    public boolean hasPendingMove(Unit unit) {
        return pendingMoveOrigin.containsKey(unit);
    }

    /**
     * Returns all tiles the unit can move to this turn.
     *
     * @param unit the unit to evaluate
     * @return list of reachable tiles
     */
    public List<int[]> getMovementTiles(Unit unit) {
        List<int[]> tiles = new LinkedList<>();
        int ux = unit.getX();
        int uy = unit.getY();
        int speed = unit.getSpeed();
        for (int dx = -speed; dx <= speed; dx++) {
            for (int dy = -speed; dy <= speed; dy++) {
                if (Math.abs(dx) + Math.abs(dy) > speed) {
                    continue;
                }
                int tx = ux + dx;
                int ty = uy + dy;
                if (tx < 0 || ty < 0 || ty >= tileGrid.length || tx >= tileGrid[0].length) {
                    continue;
                }
                if (tileGrid[ty][tx].getTerrain() instanceof Water) {
                    continue;
                }
                if (tx == ux && ty == uy) {
                    continue;
                }
                List<int[]> path = Pathfinder.findPath(tileGrid, unit, tx, ty);
                if (path.isEmpty() || path.size() > speed) {
                    continue;
                }
                tiles.add(new int[]{tx, ty});
            }
        }
        return tiles;
    }

    /**
     * Returns tiles adjacent to all movement tiles, used for attacks or interactions.
     *
     * @param unit          the unit
     * @param movementTiles tiles the unit can move to
     * @return list of interaction tiles
     */
    public List<int[]> getInteractionZoneTiles(Unit unit, List<int[]> movementTiles) {
        Set<String> movSet = new HashSet<>();
        movSet.add(unit.getX() + "," + unit.getY());
        for (int[] t : movementTiles) {
            movSet.add(t[0] + "," + t[1]);
        }

        List<int[]> result = new ArrayList<>();
        Set<String> added = new HashSet<>();
        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        List<int[]> sources = new ArrayList<>(movementTiles);
        sources.add(new int[]{unit.getX(), unit.getY()});

        for (int[] tile : sources) {
            for (int[] d : dirs) {
                int nx = tile[0] + d[0];
                int ny = tile[1] + d[1];
                String key = nx + "," + ny;
                if (movSet.contains(key) || added.contains(key)) {
                    continue;
                }
                if (nx < 0 || ny < 0 || ny >= tileGrid.length || nx >= tileGrid[0].length) {
                    continue;
                }
                result.add(new int[]{nx, ny});
                added.add(key);
            }
        }
        return result;
    }

    /**
     * Checks if the unit has moved this turn.
     *
     * @param unit the unit to check
     * @return true if the unit has moved
     */
    public boolean hasMovedThisTurn(Unit unit) {
        return movedThisTurn.contains(unit);
    }

    /**
     * Checks if the unit has attacked this turn.
     *
     * @param unit the unit to check
     * @return true if the unit has attacked
     */
    public boolean hasAttackedThisTurn(Unit unit) {
        return attackedThisTurn.contains(unit);
    }

    /**
     * Resets all movement and attack state for a new turn.
     */
    public void resetMovement() {
        movedThisTurn.clear();
        attackedThisTurn.clear();
        pendingMoveOrigin.clear();
        selectedUnit = null;
    }

    /**
     * Returns all units in the game.
     *
     * @return list of units
     */
    public List<Unit> getAllUnits() {
        return units;
    }

    /**
     * Returns all units.
     *
     * @return list of units
     */
    public List<Unit> getUnits() {
        return units;
    }

    /**
     * Returns all alive units belonging to a specific player.
     *
     * @param player the player
     * @return list of units belonging to the player
     */
    public List<Unit> getUnitsForPlayer(base.Player player) {
        List<Unit> result = new ArrayList<>();
        for (Unit u : units) {
            if (u.getPlayer() == player && u.isAlive()) {
                result.add(u);
            }
        }
        return result;
    }

    /**
     * Performs an attack from one unit to another.
     *
     * @param attacker the attacking unit
     * @param target   the target unit
     * @return true if the attack succeeded
     */
    public boolean attackUnit(Unit attacker, Unit target) {
        if (attackedThisTurn.contains(attacker)) {
            return false;
        }
        if (!target.isAlive()) {
            return false;
        }

        target.takeDamage(attacker.getAttack());
        attackedThisTurn.add(attacker);
        if (!target.isAlive()) {
            tileGrid[target.getY()][target.getX()].setUnit(null);
        }
        selectedUnit = null;
        return true;
    }

    /**
     * Attacks a base with a unit.
     *
     * @param attacker the attacking unit
     * @param base     the base to attack
     * @return true if the attack succeeded
     */
    public boolean attackBase(Unit attacker, BaseStats base) {
        if (attackedThisTurn.contains(attacker)) {
            return false;
        }
        if (base == null || base.isDestroyed()) {
            return false;
        }

        base.takeDamage(attacker.getAttack());
        attackedThisTurn.add(attacker);
        commitMove(attacker);
        selectedUnit = null;

        if (base.isDestroyed() && baseDestroyedListener != null) {
            baseDestroyedListener.onBaseDestroyed(base, attacker.getPlayer());
        }
        return true;
    }

    /**
     * Attacks a building with a unit.
     *
     * @param attacker the attacking unit
     * @param building the building to attack
     * @return true if the attack succeeded
     */
    public boolean attackBuilding(Unit attacker, Building building) {
        if (attackedThisTurn.contains(attacker)) {
            return false;
        }
        if (!building.isBuilt()) {
            return false;
        }

        building.setHealth(-attacker.getAttack());
        attackedThisTurn.add(attacker);
        commitMove(attacker);
        selectedUnit = null;
        return true;
    }

    /**
     * Moves a unit and attacks a target in the same action.
     *
     * @param attacker     the attacking unit
     * @param moveTileX    X coordinate to move to
     * @param moveTileY    Y coordinate to move to
     * @param target       the target unit
     * @param attackerView view for movement animation; may be null
     * @return true if the action succeeded
     */
    public boolean moveAndAttack(Unit attacker, int moveTileX, int moveTileY, Unit target, UnitView attackerView) {
        if (attackedThisTurn.contains(attacker)) {
            return false;
        }
        if (!target.isAlive()) {
            return false;
        }

        boolean samePos = (attacker.getX() == moveTileX && attacker.getY() == moveTileY);
        if (!samePos && !movedThisTurn.contains(attacker)) {
            List<int[]> path = Pathfinder.findPath(tileGrid, attacker, moveTileX, moveTileY);
            if (!path.isEmpty()) {
                int steps = Math.min(attacker.getSpeed(), path.size());
                Queue<int[]> queue = new LinkedList<>();
                for (int i = 0; i < steps; i++) {
                    queue.add(path.get(i));
                }
                tileGrid[attacker.getY()][attacker.getX()].setUnit(null);
                if (attackerView != null) {
                    attackerView.setMovementPath(queue);
                }
                attacker.setPosition(moveTileX, moveTileY);
                tileGrid[moveTileY][moveTileX].setUnit(attacker);
                movedThisTurn.add(attacker);
            }
        }
        target.takeDamage(attacker.getAttack());
        attackedThisTurn.add(attacker);
        if (!target.isAlive()) {
            tileGrid[target.getY()][target.getX()].setUnit(null);
        }
        selectedUnit = null;
        return true;
    }

    /**
     * Spawns a unit near a base if a valid tile is found.
     *
     * @param base the base
     * @param unit the unit to spawn
     */
    public void spawnUnitNearBase(BaseStats base, Unit unit) {
        int baseRow = base.getPosition().getY();
        int baseCol = base.getPosition().getX();

        int spawnRow = baseRow - 1;

        for (int rowOffset = 1; rowOffset <= 5; rowOffset++) {
            spawnRow = baseRow - rowOffset;
            for (int col = baseCol; col < baseCol + 4; col++) {
                if (spawnRow >= 0
                    && spawnRow < tileGrid.length
                    && col >= 0
                    && col < tileGrid[0].length
                    && tileGrid[spawnRow][col].getUnit() == null
                    && !(tileGrid[spawnRow][col].getTerrain() instanceof Water)) {
                    unit.setPosition(col, spawnRow);
                    tileGrid[spawnRow][col].setUnit(unit);
                    units.add(unit);
                    return;
                }
            }
        }
        for (int rowOffset = 4; rowOffset <= 8; rowOffset++) {
            spawnRow = baseRow + rowOffset;
            for (int col = baseCol; col < baseCol + 4; col++) {
                if (spawnRow < tileGrid.length
                    && col >= 0
                    && col < tileGrid[0].length
                    && tileGrid[spawnRow][col].getUnit() == null
                    && !(tileGrid[spawnRow][col].getTerrain() instanceof Water)) {
                    unit.setPosition(col, spawnRow);
                    tileGrid[spawnRow][col].setUnit(unit);
                    units.add(unit);
                    return;
                }
            }
        }
    }
}
