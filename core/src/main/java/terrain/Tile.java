package terrain;

import base.BaseStats;
import unit.Unit;

import java.util.List;

public class Tile {
    int x;
    int y;

    Terrain terrain;
    BaseStats base;
    List<Unit> units;
    //Building building;
    //Resource resource;

    public Tile(int x, int y, Terrain terrain) {
        this.terrain = terrain;
        this.x = x;
        this.y = y;
    }

    public Terrain getTerrain(){
        return terrain;
    }

    public void setUnits(Unit unit){
        units.add(unit);
    }
    public List<Unit> getUnits(){
        return units;
    }

    public BaseStats getBase(){
        return base;
    }

    public void setBase(BaseStats base){
        this.base = base;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
