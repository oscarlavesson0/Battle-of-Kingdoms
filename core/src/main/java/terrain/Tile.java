package terrain;

import base.Base;
import unit.Unit;

import java.util.List;

public class Tile {
    int x;
    int y;

    Terrain terrain;
    Base base;
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

    public Base getBase(){
        return base;
    }

    public void setBase(Base base){
        this.base = base;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
