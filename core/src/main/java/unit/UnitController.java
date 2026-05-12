package unit;

import base.BaseStats;
import base.Player;
import terrain.Tile;
import terrain.Water;

import java.util.*;

public class UnitController {

    private Tile[][] tileGrid;
    private Unit selectedUnit;
    private GuiMainGame.UnitView unitView;

    private final List<Unit>                  allUnits       = new ArrayList<>();
    private final List<GuiMainGame.UnitView>  allUnitViews   = new ArrayList<>();
    private final List<Unit>                  movedThisTurn  = new ArrayList<>();
    private final List<Unit>                  attackedThisTurn = new ArrayList<>();

    public UnitController(Tile[][] tileGrid) {
        this.tileGrid = tileGrid;
    }

    // unit registration

    public void setUnitView(GuiMainGame.UnitView view)       { this.unitView = view; }
    public void setActiveUnitView(GuiMainGame.UnitView view) { this.unitView = view; }

    public void registerUnit(Unit unit, GuiMainGame.UnitView view) {
        allUnits.add(unit);
        allUnitViews.add(view);
    }

    public List<Unit> getAllUnits() { return allUnits; }

    public List<Unit> getUnitsForPlayer(Player player) {
        List<Unit> result = new ArrayList<>();
        for (Unit u : allUnits)
            if (u.getPlayer() == player && u.isAlive()) result.add(u);
        return result;
    }

    public GuiMainGame.UnitView getViewForUnit(Unit unit) {
        int index = allUnits.indexOf(unit);
        return index >= 0 ? allUnitViews.get(index) : null;
    }

    // selection

    public void selectUnit(Unit unit) { this.selectedUnit = unit; }
    public Unit getSelectedUnit()     { return selectedUnit; }

    // turn tracking

    public boolean hasMovedThisTurn(Unit unit)    { return movedThisTurn.contains(unit);    }
    public boolean hasAttackedThisTurn(Unit unit) { return attackedThisTurn.contains(unit); }

    public void resetMovement() {
        movedThisTurn.clear();
        attackedThisTurn.clear();
        selectedUnit = null;
    }

    // movement
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
        // Build a set of all "owned" positions (current + reachable)
        Set<String> movSet = new HashSet<>();
        movSet.add(unit.getX() + "," + unit.getY());
        for (int[] t : movementTiles) movSet.add(t[0] + "," + t[1]);

        List<int[]> result  = new ArrayList<>();
        Set<String> added   = new HashSet<>();
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

        // Expand one step outward from every owned tile
        List<int[]> sources = new ArrayList<>(movementTiles);
        sources.add(new int[]{unit.getX(), unit.getY()});

        for (int[] tile : sources) {
            for (int[] d : dirs) {
                int nx  = tile[0] + d[0];
                int ny  = tile[1] + d[1];
                String key = nx + "," + ny;
                if (movSet.contains(key) || added.contains(key)) continue;
                if (nx < 0 || ny < 0 || ny >= tileGrid.length || nx >= tileGrid[0].length) continue;
                result.add(new int[]{nx, ny});
                added.add(key);
            }
        }
        return result;
    }


    public boolean moveSelectedUnit(int targetX, int targetY) {
        if (selectedUnit == null) return false;
        if (movedThisTurn.contains(selectedUnit)) return false;

        List<int[]> path = Pathfinder.findPath(tileGrid, selectedUnit, targetX, targetY);
        if (path.isEmpty()) return false;

        int maxSteps = selectedUnit.getSpeed();
        int steps    = Math.min(maxSteps, path.size());

        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < steps; i++) queue.add(path.get(i));

        if (unitView != null) unitView.setMovementPath(queue);

        movedThisTurn.add(selectedUnit);
        selectedUnit = null;
        return true;
    }

    // attack

    public boolean attackUnit(Unit attacker, Unit target) {
        if (attackedThisTurn.contains(attacker)) return false;
        if (!target.isAlive()) return false;

        target.takeDamage(attacker.getAttack());
        attackedThisTurn.add(attacker);

        if (!target.isAlive())
            tileGrid[target.getY()][target.getX()].setUnit(null);

        selectedUnit = null;
        return true;
    }


    public boolean moveAndAttack(Unit attacker, int moveTileX, int moveTileY, Unit target) {
        if (attackedThisTurn.contains(attacker)) return false;
        if (!target.isAlive()) return false;

        boolean samePos = (attacker.getX() == moveTileX && attacker.getY() == moveTileY);

        if (!samePos && !movedThisTurn.contains(attacker)) {
            List<int[]> path = Pathfinder.findPath(tileGrid, attacker, moveTileX, moveTileY);
            if (!path.isEmpty()) {
                int steps = Math.min(attacker.getSpeed(), path.size());
                Queue<int[]> queue = new LinkedList<>();
                for (int i = 0; i < steps; i++) queue.add(path.get(i));

                GuiMainGame.UnitView view = getViewForUnit(attacker);
                if (view != null) view.setMovementPath(queue);

                attacker.setPosition(moveTileX, moveTileY);   // logical move
                movedThisTurn.add(attacker);
            }
        }

        target.takeDamage(attacker.getAttack());
        attackedThisTurn.add(attacker);

        if (!target.isAlive())
            tileGrid[target.getY()][target.getX()].setUnit(null);

        selectedUnit = null;
        return true;
    }

    // ── deprecated helper kept for back-compat

    public List<Unit> getAttackableUnits(Unit attacker) {
        List<Unit> targets = new ArrayList<>();
        for (Unit u : allUnits) {
            if (u.getPlayer() != attacker.getPlayer() && u.isAlive()) {
                int dist = Math.abs(u.getX() - attacker.getX())
                    + Math.abs(u.getY() - attacker.getY());
                if (dist == 1) targets.add(u);
            }
        }
        return targets;
    }

    // spawn

    public void spawnUnitNearBase(BaseStats base, Unit unit) {
        int bx = base.getPosition().getX();
        int by = base.getPosition().getY();

        int[][] dirs = {{2,0},{-2,0},{0,2},{0,-2},{1,0},{-1,0},{0,1},{0,-1}};
        for (int[] d : dirs) {
            int nx = bx + d[0];
            int ny = by + d[1];
            if (nx >= 0 && ny >= 0
                && ny < tileGrid.length
                && nx < tileGrid[0].length
                && tileGrid[ny][nx].getUnit() == null
                && !(tileGrid[ny][nx].getTerrain() instanceof Water)) {
                unit.setPosition(nx, ny);
                tileGrid[ny][nx].setUnit(unit);
                return;
            }
        }
    }
}
