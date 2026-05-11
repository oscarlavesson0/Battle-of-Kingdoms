package unit;

import terrain.Tile;
import terrain.Water;

import java.util.*;

public class Pathfinder {

    private static final int[][] DIRECTIONS = {
        {0, -1},  // Up
        {0, 1},   // Down
        {-1, 0},  // Left
        {1, 0}    // Right
    };

    public static List<int[]> findPath(Tile[][] grid, Unit unit, int targetX, int targetY) {

        int startX = unit.getX();
        int startY = unit.getY();
        int rows = grid.length;
        int cols = grid[0].length;

        // Bounds check
        if (!inBounds(startX, startY, cols, rows) ||
            !inBounds(targetX, targetY, cols, rows)) {
            return Collections.emptyList();
        }

        // Already there
        if (startX == targetX && startY == targetY) {
            return Collections.emptyList();
        }

        boolean[][] visited = new boolean[rows][cols];
        int[][][] parent = new int[rows][cols][];

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        visited[startY][startX] = true;

        boolean found = false;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int cx = current[0];
            int cy = current[1];

            if (cx == targetX && cy == targetY) {
                found = true;
                break;
            }

            for (int[] dir : DIRECTIONS) {
                int nx = cx + dir[0];
                int ny = cy + dir[1];

                if (!inBounds(nx, ny, cols, rows)) continue;
                if (visited[ny][nx]) continue;

                //  BLOCK WATER
                if (grid[ny][nx].getTerrain() instanceof Water) continue;

                visited[ny][nx] = true;
                parent[ny][nx] = new int[]{cx, cy};
                queue.add(new int[]{nx, ny});
            }
        }

        if (!found) {
            return Collections.emptyList();
        }

        //  Reconstruct path
        LinkedList<int[]> path = new LinkedList<>();
        int cx = targetX;
        int cy = targetY;

        while (cx != startX || cy != startY) {
            path.addFirst(new int[]{cx, cy});
            int[] prev = parent[cy][cx];
            cx = prev[0];
            cy = prev[1];
        }

        return path;
    }

    private static boolean inBounds(int x, int y, int cols, int rows) {
        return x >= 0 && y >= 0 && x < cols && y < rows;
    }
}
