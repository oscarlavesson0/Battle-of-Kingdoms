package terrain;

public abstract class Terrain {

    String name;
    int id;

    public Terrain(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }


}
