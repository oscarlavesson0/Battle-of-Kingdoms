package unit;

import terrain.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MovementTest extends JPanel {

    private final int TILE_SIZE = 50;
    private Tile[][] grid;
    private UnitController controller;

    private Unit[] units = new Unit[3];
    private Unit selectedUnit = null;
    private List<int[]> movementTiles = null;

    public MovementTest() {

        // --- Create a simple 10x10 grid ---
        grid = new Tile[10][10];
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                grid[y][x] = new Tile(x, y, null);
            }
        }

        controller = new UnitController(grid);

        // --- Units ---
        units[0] = new Swordman(0, 0);   // S
        units[1] = new Axeman(9, 9);     // A
        units[2] = new Spearman(5, 5);   // P

        // --- Mouse interaction ---
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int x = e.getX() / TILE_SIZE;
                int y = e.getY() / TILE_SIZE;

                // Select unit if clicked on one
                for (Unit u : units) {
                    if (u.getX() == x && u.getY() == y) {
                        selectedUnit = u;
                        movementTiles = controller.getMovementTiles(u);
                        repaint();
                        return;
                    }
                }

                // Move selected unit if tile is allowed
                if (selectedUnit != null && movementTiles != null) {
                    for (int[] tile : movementTiles) {
                        if (tile[0] == x && tile[1] == y) {
                            controller.selectUnit(selectedUnit);
                            controller.moveSelectedUnit(x, y);
                            movementTiles = controller.getMovementTiles(selectedUnit);
                            repaint();
                            return;
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw grid
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // Highlight movement tiles
        if (movementTiles != null) {
            g.setColor(new Color(0, 0, 255, 80));
            for (int[] t : movementTiles) {
                g.fillRect(t[0] * TILE_SIZE, t[1] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // Draw units with letters
        for (Unit u : units) {

            String label;
            Color color;

            if (u instanceof Swordman) {
                label = "S";
                color = Color.RED;
            } else if (u instanceof Axeman) {
                label = "A";
                color = Color.BLUE;
            } else {
                label = "P";
                color = Color.GREEN;
            }

            g.setColor(color);
            g.fillOval(u.getX() * TILE_SIZE + 10, u.getY() * TILE_SIZE + 10, 30, 30);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString(label, u.getX() * TILE_SIZE + 22, u.getY() * TILE_SIZE + 32);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Unit Movement Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520, 540);
        frame.add(new MovementTest());
        frame.setVisible(true);
    }
}
