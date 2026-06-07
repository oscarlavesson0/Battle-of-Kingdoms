package terrain;

/**
 * Terrain type representing walkable land.
 *
 * @author JoelAxel Olsson
 */
public class Land extends Terrain {

    /**
     * Creates a land terrain tile.
     *
     * @param name name of the terrain
     * @param id   terrain ID
     */
    public Land(String name, int id) {
        super(name, id);
    }
}
