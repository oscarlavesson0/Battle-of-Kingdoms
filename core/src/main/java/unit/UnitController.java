package unit;

import base.BaseStats;
import terrain.Tile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class UnitController {

    private Tile[][] tileGrid;
    private Unit selectedUnit;
    private GuiMainGame.UnitView unitView;
    private List<Unit> units = new ArrayList<>();


    public UnitController(Tile[][] tileGrid) {
        this.tileGrid = tileGrid;
    }

    public void setUnitView(GuiMainGame.UnitView view) {
        this.unitView = view;
    }

    public void selectUnit(Unit unit) {
        this.selectedUnit = unit;
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public boolean moveSelectedUnit(int targetX, int targetY) {
        if (selectedUnit == null) {
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

        if (unitView != null) {
            unitView.setMovementPath(queue);
        }

        return true;
    }

    public List<int[]> getMovementTiles(Unit unit) {
        List<int[]> tiles = new LinkedList<>();

        int ux = unit.getX();
        int uy = unit.getY();
        int speed = unit.getSpeed();

        for (int dx = -speed; dx <= speed; dx++) {
            for (int dy = -speed; dy <= speed; dy++) {
                if (Math.abs(dx) + Math.abs(dy) <= speed) {
                    int tx = ux + dx;
                    int ty = uy + dy;

                    if (tx >= 0 && ty >= 0 &&
                        ty < tileGrid.length &&
                        tx < tileGrid[0].length) {

                        tiles.add(new int[]{tx, ty});
                    }
                }
            }
        }

        return tiles;
    }

    public void spawnUnitNearBase(BaseStats base, Unit unit) {

        int bx = base.getPosition().getX();
        int by = base.getPosition().getY();

        int[][] dirs = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };

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
