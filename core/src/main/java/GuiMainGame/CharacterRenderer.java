package GuiMainGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * CharacterRenderer handles all sprite animations for a unit, including idle, running,
 * attacking, taking damage, and dying. The renderer loads animations based on the
 * {@link UnitType} provided and returns the correct animation frame depending on the
 * unit's current state.
 *
 * <p>The class supports two types of spritesheets:
 * <ul>
 *     <li>Single-row sheets (e.g., Knight animations)</li>
 *     <li>Multi-row sheets where each row represents a different animation
 *         (e.g., Axeman/Minotaur spritesheet)</li>
 * </ul>
 *
 * <p>Animation playback is time‑based using LibGDX's {@link Animation} class.</p>
 */

public class CharacterRenderer {
    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> runAnim;
    private Animation<TextureRegion> attackAnim;
    private Animation<TextureRegion> deathAnim;
    private Animation<TextureRegion> damageAnim;
    private State currentState = State.IDLE;
    private float stateTime = 0f;
    private UnitType unitType;

    /**
     * Creates a CharacterRenderer for a specific unit type and loads all required animations.
     *
     * @param unitType the type of unit (Knight, Axeman, etc.)
     * @Author Oscar Lavesson
     */
    public CharacterRenderer(UnitType unitType) {
        this.unitType = unitType;
        loadCharacter();
    }

    /**
     * Loads animations depending on the unit type. Each unit has its own spritesheet
     * layout and animation timing.
     *
     * <p>KNIGHT uses separate PNG files for each animation.</p>
     * <p>AXEMAN uses a single multi‑row spritesheet where each row is a different animation.</p>
     * @Author Oscar Lavesson
     */
    public void loadCharacter() {
        switch (unitType) {
            case KNIGHT -> {
                idleAnim   = loadAnimation("lwjgl3/assets/ui/Knight Hero Platfomer/Combat Ready Idle.png", 22, 24, 0.5f);
                runAnim    = loadAnimation("lwjgl3/assets/ui/Knight Hero Platfomer/Run.png",               22, 24, 0.5f);
                attackAnim = loadAnimation("lwjgl3/assets/ui/Knight Hero Platfomer/Attack 1.png",          40, 30, 0.5f);
            }
            case AXEMAN -> {
                String path = "lwjgl3/assets/ui/Axeman/Minotaur.png";
                idleAnim   = loadAnimationRow(path, 94, 94, 0.25f, 0, 5);
                runAnim    = loadAnimationRow(path, 94, 94, 0.1f,  1, 8);
                attackAnim = loadAnimationRow(path, 94, 94, 0.1f,  3, 9);
                damageAnim = loadAnimationRow(path, 94, 94, 0.1f,  8, 3);
                deathAnim  = loadAnimationRow(path, 94, 94, 0.15f, 9, 5);
            }

        }
    }

    /**
     * Loads an animation from a spritesheet where all frames are on a single row.
     *
     * @param path        file path to the spritesheet
     * @param frameWidth  width of each frame
     * @param frameHeight height of each frame
     * @param speed       animation speed (seconds per frame)
     * @return an Animation containing all frames
     * @Author Oscar Lavesson
     */
    private Animation<TextureRegion> loadAnimation(String path, int frameWidth, int frameHeight, float speed) {
        Texture sheet = new Texture(path);
        int frameCount = sheet.getWidth() / frameWidth;
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++)
            frames[i] = new TextureRegion(sheet, i * frameWidth, 0, frameWidth, frameHeight);
        return new Animation<>(speed, frames);
    }

    /**
     * Loads an animation from a specific row in a multi‑row spritesheet.
     *
     * @param path        file path to the spritesheet
     * @param frameWidth  width of each frame
     * @param frameHeight height of each frame
     * @param speed       animation speed (seconds per frame)
     * @param row         which row to read frames from
     * @param frameCount  how many frames the animation contains
     * @return an Animation containing the selected row's frames
     * @Author Oscar Lavesson
     */
    private Animation<TextureRegion> loadAnimationRow(String path, int frameWidth,
                                                      int frameHeight, float speed,
                                                      int row, int frameCount) {
        Texture sheet = new Texture(path);
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++)
            frames[i] = new TextureRegion(sheet, i * frameWidth, row * frameHeight, frameWidth, frameHeight);
        return new Animation<>(speed, frames);
    }

    /**
     * Sets the current animation state. If the state changes, the animation timer resets.
     *
     * @param state the new animation state
     * @Author Oscar Lavesson
     */
    public void setState(State state) {
        if (state != currentState) { currentState = state; stateTime = 0f; }
    }

    /**
     * Updates the animation timer.
     *
     * @param delta time passed since last frame (in seconds)
     * @Author Oscar Lavesson
     */
    public void update(float delta) { stateTime += delta; }

    /**
     * Returns the current animation frame based on the unit's state and elapsed time.
     *
     * @return the TextureRegion representing the current frame
     * @Author Oscar Lavesson
     */
    public TextureRegion getCurrentFrame() {
        return switch (currentState) {
            case RUN    -> runAnim.getKeyFrame(stateTime, true);
            case ATTACK -> attackAnim.getKeyFrame(stateTime, false);
            case DAMAGE -> damageAnim != null
                ? damageAnim.getKeyFrame(stateTime, false)
                : idleAnim.getKeyFrame(stateTime, true);
            default     -> idleAnim.getKeyFrame(stateTime, true);
        };
    }
}
