package unit;

import base.BaseStats;
import base.Player;
import terrain.Tile;

import java.util.ArrayList;
import java.util.List;


//Kontrollerar movement för units och spawnar dem bredvid baserna. Detta kommer att uppdateras senare då detta e för testdagen.
public class UnitController {

    private Unit selectedUnit;
    private Tile[][] tileGrid;

    public UnitController(Tile[][] tileGrid) {
        this.tileGrid = tileGrid;
    }

    // Markerar en unit som vald.
    public void selectUnit(Unit unit) {
        this.selectedUnit = unit;
    }

    // Avmarkerar vald unit.
    public void deselectUnit() {
        this.selectedUnit = null;
    }

    // Flyttar den valda uniten om avståndet är tillåtet.
    public boolean moveSelectedUnit(int newX, int newY) {
        if (selectedUnit == null) return false;

        if (selectedUnit.canMoveTo(newX, newY)) {
            selectedUnit.moveTo(newX, newY);
            return true;
        }

        return false;
    }

    // Returnerar alla tiles som uniten kan nå baserat på speed.
    public List<int[]> getMovementTiles(Unit unit) {
        List<int[]> tiles = new ArrayList<>();

        int speed = unit.getSpeed();
        int startX = unit.getX();
        int startY = unit.getY();

        for (int dx = -speed; dx <= speed; dx++) {
            for (int dy = -speed; dy <= speed; dy++) {

                int newX = startX + dx;
                int newY = startY + dy;

                // Bounds check
                if (newX < 0 || newY < 0 || newX >= tileGrid[0].length || newY >= tileGrid.length)
                    continue;

                if (unit.canMoveTo(newX, newY)) {
                    tiles.add(new int[]{newX, newY});
                }
            }
        }

        return tiles;
    }

    // Spawnar units bredvid spelarens bas. Player1: bas i övre högra hörnet → spawn nedanför + vänster. Player2: bas i nedre vänstra hörnet → spawn ovanför + höger.
    public Unit spawnUnitNearBase(Player player, Unit unit) {

        BaseStats base = player.getBase();
        Tile baseTile = base.getPosition();

        int baseX = baseTile.getX();
        int baseY = baseTile.getY();

        int spawnX;
        int spawnY;

        if (player.getId().equals("Player1")) {
            spawnX = baseX - 1;
            spawnY = baseY - 1;
        } else {
            spawnX = baseX + 1;
            spawnY = baseY + 1;
        }

        unit.moveTo(spawnX, spawnY);
        return unit;
    }
}
