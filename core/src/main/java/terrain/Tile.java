package terrain;

public class Tile {
    int x;
    int y;

    Terrain terrain;
    //List<Unit> units;
    //Base base;
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

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
