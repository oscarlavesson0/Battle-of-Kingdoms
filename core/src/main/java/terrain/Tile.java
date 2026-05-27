package terrain;

import base.BaseStats;
import building.Building;
import unit.Unit;

public class Tile {

    private int x;
    private int y;
    private Terrain terrain;
    private BaseStats base;
    private Unit unit;
    private Building building;

    public Tile(int x, int y, Terrain terrain) {
        this.x = x;
        this.y = y;
        this.terrain = terrain;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public BaseStats getBase() {
        return base;
    }

    public void setBase(BaseStats base) {
        this.base = base;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
