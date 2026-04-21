package terrain;

public abstract class Terrain {

    String name;
    String id;

    public Terrain(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }


}
