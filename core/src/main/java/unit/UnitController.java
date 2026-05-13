package unit;

import GuiMainGame.UnitView;
import base.BaseStats;
import terrain.Tile;
import terrain.Water;
import java.util.*;

public class UnitController {
    private Tile[][] tileGrid;
    private Unit selectedUnit;
    private List<Unit> units = new ArrayList<>();
    private List<Unit> movedThisTurn = new ArrayList<>();
    private List <Unit> attackedThisTurn = new ArrayList<>();

    public UnitController(Tile[][] tileGrid) {
        this.tileGrid = tileGrid;
    }

    public void selectUnit(Unit unit) {
        this.selectedUnit = unit;
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public boolean moveSelectedUnit(int targetX, int targetY, UnitView unitView) {
        if (selectedUnit == null) return false;
        if(movedThisTurn.contains(selectedUnit)){
            return false;
        }

        List<int[]> path = Pathfinder.findPath(tileGrid, selectedUnit, targetX, targetY);
        if (path.isEmpty()) return false;

        int maxSteps = selectedUnit.getSpeed();
        int steps = Math.min(maxSteps, path.size());
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < steps; i++) {
            queue.add(path.get(i));
        }

        // Rensa gamla tile-referensen
        tileGrid[selectedUnit.getY()][selectedUnit.getX()].setUnit(null);

        if (unitView != null) {
            unitView.setMovementPath(queue);
        }

        movedThisTurn.add(selectedUnit);
        selectedUnit = null;
        return true;
    }

    public List<int[]> getMovementTiles(Unit unit) {
        List<int[]> tiles = new LinkedList<>();
        int ux    = unit.getX();
        int uy    = unit.getY();
        int speed = unit.getSpeed();

        for (int dx = -speed; dx <= speed; dx++) {
            for (int dy = -speed; dy <= speed; dy++) {
                if (Math.abs(dx) + Math.abs(dy) > speed) continue;
                int tx = ux + dx;
                int ty = uy + dy;
                if (tx < 0 || ty < 0 || ty >= tileGrid.length || tx >= tileGrid[0].length) continue;
                if (tileGrid[ty][tx].getTerrain() instanceof Water) continue;
                if (tx == ux && ty == uy) continue;
                List<int[]> path = Pathfinder.findPath(tileGrid, unit, tx, ty);
                if (path.isEmpty() || path.size() > speed) continue;
                tiles.add(new int[]{tx, ty});
            }
        }
        return tiles;
    }
    
    public List<int[]> getInteractionZoneTiles(Unit unit, List<int[]> movementTiles) {
        Set<String> movSet = new HashSet<>();
        movSet.add(unit.getX() + "," + unit.getY());
        for (int[] t : movementTiles) movSet.add(t[0] + "," + t[1]);

        List<int[]> result = new ArrayList<>();
        Set<String> added = new HashSet<>();
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

        List<int[]> sources = new ArrayList<>(movementTiles);
        sources.add(new int[]{unit.getX(), unit.getY()});

        for (int[] tile : sources) {
            for (int[] d : dirs) {
                int nx = tile[0] + d[0];
                int ny = tile[1] + d[1];
                String key = nx + "," + ny;
                if (movSet.contains(key) || added.contains(key)) continue;
                if (nx < 0 || ny < 0 || ny >= tileGrid.length || nx >= tileGrid[0].length) continue;
                result.add(new int[]{nx, ny});
                added.add(key);
            }
        }
        return result;
    }

    public boolean hasMovedThisTurn(Unit unit)    { return movedThisTurn.contains(unit);    }
    public boolean hasAttackedThisTurn(Unit unit) { return attackedThisTurn.contains(unit); }

    public void resetMovement() {
        movedThisTurn.clear();
        attackedThisTurn.clear();
        selectedUnit = null;
    }

    public List<Unit> getAllUnits() { return units; }

    public List<Unit> getUnitsForPlayer(base.Player player) {
        List<Unit> result = new ArrayList<>();
        for (Unit u : units) {
            if (u.getPlayer() == player && u.isAlive()) {
                result.add(u);
            }
        }
        return result;
    }

    public boolean attackUnit(Unit attacker, Unit target) {
        if (attackedThisTurn.contains(attacker)) return false;
        if (!target.isAlive()) return false;

        target.takeDamage(attacker.getAttack());
        attackedThisTurn.add(attacker);

        if (!target.isAlive()) {
            tileGrid[target.getY()][target.getX()].setUnit(null);
        }

        selectedUnit = null;
        return true;
    }

    public boolean moveAndAttack(Unit attacker, int moveTileX, int moveTileY, Unit target, UnitView attackerView) {
        if (attackedThisTurn.contains(attacker)) return false;
        if (!target.isAlive()) return false;

        boolean samePos = (attacker.getX() == moveTileX && attacker.getY() == moveTileY);

        if (!samePos && !movedThisTurn.contains(attacker)) {
            List<int[]> path = Pathfinder.findPath(tileGrid, attacker, moveTileX, moveTileY);
            if (!path.isEmpty()) {
                int steps = Math.min(attacker.getSpeed(), path.size());
                Queue<int[]> queue = new LinkedList<>();
                for (int i = 0; i < steps; i++) queue.add(path.get(i));

                tileGrid[attacker.getY()][attacker.getX()].setUnit(null);

                if (attackerView != null) attackerView.setMovementPath(queue);

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

    public void spawnUnitNearBase(BaseStats base, Unit unit) {
        int bx = base.getPosition().getX();
        int by = base.getPosition().getY();
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        for (int[] d : dirs) {
            int nx = bx + d[0];
            int ny = by + d[1];
            if (nx >= 0 && ny >= 0 &&
                ny < tileGrid.length &&
                nx < tileGrid[0].length &&
                tileGrid[ny][nx].getUnit() == null) {
                unit.setPosition(nx, ny);
                tileGrid[ny][nx].setUnit(unit);
                units.add(unit);
                return;
            }
        }
    }

    public List<Unit> getUnits() {
        return units;
    }
}
