package GuiMainGame;

/**
 * Represents the animation state of a unit.
 *
 * <p>This enum is used by {@link CharacterRenderer} and {@link UnitView} to determine
 * which animation should be played at any given moment. Each state corresponds to a
 * specific animation sequence in the unit's spritesheet.</p>
 */
public enum State {
    IDLE,
    RUN,
    ATTACK,
    DAMAGE
}
