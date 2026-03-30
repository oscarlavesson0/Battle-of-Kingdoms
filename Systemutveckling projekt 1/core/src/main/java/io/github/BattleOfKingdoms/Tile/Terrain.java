package Tile;

public abstract class Terrain {
    String name;
    public Terrain(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
