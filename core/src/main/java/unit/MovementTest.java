package unit;

import terrain.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MovementTest extends JPanel {

    private final int TILE_SIZE = 50;
    private Tile[][] grid;
    private UnitController controller;

    private List<Unit> units = new ArrayList<>();
    private Unit selectedUnit = null;
    private List<int[]> movementTiles = null;

    private boolean placingUnit = false;

    private int customHP = 1;
    private int customAtk = 1;
    private int customSpd = 1;
    private int customDef = 1;
    private Weapon customWeapon = Weapon.None;

    public MovementTest() {
        setLayout(new BorderLayout());

        grid = new Tile[10][10];
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                grid[y][x] = new Tile(x, y, null);
            }
        }

        controller = new UnitController(grid);

        JPanel boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                for (int y = 0; y < 10; y++) {
                    for (int x = 0; x < 10; x++) {
                        g.setColor(Color.LIGHT_GRAY);
                        g.drawRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                }

                if (movementTiles != null) {
                    g.setColor(new Color(0, 0, 255, 80));
                    for (int[] t : movementTiles) {
                        g.fillRect(t[0] * TILE_SIZE, t[1] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                }

                for (Unit u : units) {
                    String label;
                    Color color;

                    if (u.getWeapon() == Weapon.SWORD) {
                        label = "S";
                        color = Color.RED;
                    } else if (u.getWeapon() == Weapon.AXE) {
                        label = "A";
                        color = Color.GREEN;
                    } else if (u.getWeapon() == Weapon.SPEAR) {
                        label = "Sp";
                        color = Color.BLUE;
                    } else  {
                        label = "n";
                        color = Color.PINK;


                    }

                    g.setColor(color);
                    g.fillOval(u.getX() * TILE_SIZE + 10, u.getY() * TILE_SIZE + 10, 30, 30);

                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.BOLD, 18));
                    g.drawString(label, u.getX() * TILE_SIZE + 22, u.getY() * TILE_SIZE + 32);
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(500, 500);
            }
        };

        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / TILE_SIZE;
                int y = e.getY() / TILE_SIZE;
                if (x < 0 || x >= 10 || y < 0 || y >= 10) return;

                if (placingUnit) {
                    for (Unit u : units) {
                        if (u.getX() == x && u.getY() == y) return; // rutan upptagen
                    }
                    units.add(new CustomUnit(customHP, customAtk, customSpd, customDef, customWeapon, x, y));
                    placingUnit = false;
                    boardPanel.repaint();
                    return;
                }

                for (Unit u : units) {
                    if (u.getX() == x && u.getY() == y) {
                        selectedUnit = u;
                        movementTiles = controller.getMovementTiles(u);
                        boardPanel.repaint();
                        return;
                    }
                }

                if (selectedUnit != null && movementTiles != null) {
                    for (int[] tile : movementTiles) {
                        if (tile[0] == x && tile[1] == y) {
                            controller.selectUnit(selectedUnit);
                            controller.moveSelectedUnit(x, y);
                            movementTiles = controller.getMovementTiles(selectedUnit);
                            boardPanel.repaint();
                            return;
                        }
                    }
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addBtn = new JButton("Lägg till custom unit");
        addBtn.addActionListener(e -> {
            if (!showCustomDialog()) return;
            placingUnit = true;
            selectedUnit = null;
            movementTiles = null;
            boardPanel.repaint();
        });
        buttonPanel.add(addBtn);

        add(boardPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private boolean showCustomDialog() {
        JPanel p = new JPanel(new GridLayout(5, 2, 6, 6));
        p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JSpinner hpSpin  = new JSpinner(new SpinnerNumberModel(customHP,  1, 25, 1));
        JSpinner atkSpin = new JSpinner(new SpinnerNumberModel(customAtk, 1,  5, 1));
        JSpinner spdSpin = new JSpinner(new SpinnerNumberModel(customSpd, 1,  5, 1));
        JSpinner defSpin = new JSpinner(new SpinnerNumberModel(customDef, 1,  5, 1));
        JComboBox<Weapon> weaponBox = new JComboBox<>(Weapon.values());
        weaponBox.setSelectedItem(customWeapon);

        p.add(new JLabel("Max HP (1-25):")); p.add(hpSpin);
        p.add(new JLabel("Attack (1-5):")); p.add(atkSpin);
        p.add(new JLabel("Speed (1-5):")); p.add(spdSpin);
        p.add(new JLabel("Defence (1-5):")); p.add(defSpin);
        p.add(new JLabel("Vapen:")); p.add(weaponBox);

        int result = JOptionPane.showConfirmDialog(
            this, p, "Skapa Custom Unit",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );
        if (result != JOptionPane.OK_OPTION) return false;

        customHP = (int) hpSpin.getValue();
        customAtk = (int) atkSpin.getValue();
        customSpd = (int) spdSpin.getValue();
        customDef = (int) defSpin.getValue();
        customWeapon = (Weapon) weaponBox.getSelectedItem();
        return true;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Unit Movement Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MovementTest mt = new MovementTest();
        frame.add(mt);
        frame.pack();
        frame.setVisible(true);
    }
}
