package terrain;

/**
 * Base class for all terrain types on the map.
 * Stores a terrain name and an ID used for identification.
 *
 * @author JoelAxel Olsson
 */
public abstract class Terrain {

    private final String name;
    private final int id;

    /**
     * Creates a terrain type with a name and ID.
     *
     * @param name name of the terrain
     * @param id   terrain ID
     */
    public Terrain(String name, int id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Returns the terrain name.
     *
     * @return terrain name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the terrain ID.
     *
     * @return terrain ID
     */
    public int getId() {
        return id;
    }
}
