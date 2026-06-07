package terrain;

/**
 * Terrain type representing water, typically impassable for most units.
 *
 * @author JoelAxel Olsson
 */
public class Water extends Terrain {

    /**
     * Creates a water terrain tile.
     *
     * @param name name of the terrain
     * @param id   terrain ID
     */
    public Water(String name, int id) {
        super(name, id);
    }
}
