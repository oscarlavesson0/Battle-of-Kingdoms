package Tile;

import java.util.List;

public class Tile {

    Terrain terrain;
    Resource resource;
    List<Unit> units;

    public Tile(int x, int y, Terrain terrain) {
        this.terrain = terrain;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void addUnit(){

    }

}
