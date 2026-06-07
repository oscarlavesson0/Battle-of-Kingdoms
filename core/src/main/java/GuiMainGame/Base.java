package GuiMainGame;

import base.BaseStats;
import base.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Represents a Base structure on the game map. A Base is rendered using a tile layout
 * inherited from {@link StructureRenderer}, and contains additional logic for handling
 * explosions and base statistics.
 *
 * <p>The Base loads its tile graphics from a spritesheet and maps tile IDs to specific
 * TextureRegions. It can also play an explosion animation when destroyed.</p>
 */

public class Base extends StructureRenderer {

    private TextureRegion[][] parts;
    private Animation<TextureRegion> explosionAnim;
    private float explosionTime = 0f;
    private boolean exploding = false;
    private BaseStats stats;
    SpriteSheetLoader sheet;

    /**
     * Creates a new Base instance with the given spritesheet loader and base statistics.
     *
     * @param sheet the SpriteSheetLoader used for loading textures
     * @param stats the BaseStats object containing owner, HP, and other base attributes
     * @author Oscar Lavesson
     */
    public Base(SpriteSheetLoader sheet, BaseStats stats) {
        this.sheet = sheet;
        this.stats = stats;

        Texture castleTexture = new Texture("lwjgl3/assets/ui/Castle Walls/Castle_door.png");

        parts = TextureRegion.split(castleTexture, 16, 16);

        layout = new int[][]{
            {1012, 1013, 1014, 1015},
            {1004, 1009, 1010, 1007},
            {1008, 1005, 1006, 1011},
            {1000, 1001, 1002, 1003}
        };
    }

    /**
     * Returns the TextureRegion associated with a specific tile ID.
     * The Base uses a 4x4 tile layout, where each ID maps to a region in the spritesheet.
     *
     * @param id the tile ID to retrieve
     * @return the corresponding TextureRegion, or null if the ID is invalid
     * @author Oscar Lavesson
     */
    @Override
    public TextureRegion getTile(int id) {
        return switch (id) {
            case 1000 -> parts[0][0];
            case 1001 -> parts[0][1];
            case 1002 -> parts[0][2];
            case 1003 -> parts[0][3];

            case 1004 -> parts[1][0];
            case 1005 -> parts[1][1];
            case 1006 -> parts[1][2];
            case 1007 -> parts[1][3];

            case 1008 -> parts[2][0];
            case 1009 -> parts[2][1];
            case 1010 -> parts[2][2];
            case 1011 -> parts[2][3];

            case 1012 -> parts[3][0];
            case 1013 -> parts[3][1];
            case 1014 -> parts[3][2];
            case 1015 -> parts[3][3];

            default -> null;
        };
    }
}


