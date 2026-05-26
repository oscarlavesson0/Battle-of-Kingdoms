package GuiMainGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import unit.Weapon;

public class CharacterRenderer {

    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> runAnim;
    private Animation<TextureRegion> attackAnim;
    private Animation<TextureRegion> deathAnim;

    private State currentState = State.IDLE;
        private float stateTime = 0f;

        private Animation<TextureRegion> damageAnim;
        private UnitType unitType;

        public CharacterRenderer(UnitType unitType) {
            this.unitType = unitType;
            loadCharacter();

        }
        public void loadCharacter() {
            switch (unitType) {

                case KNIGHT: {
                    idleAnim   = loadAnimation("lwjgl3/assets/ui/Knight Hero Platfomer/Combat Ready Idle.png", 22, 24, 0.5f);
                    runAnim    = loadAnimation("lwjgl3/assets/ui/Knight Hero Platfomer/Run.png", 22, 24, 0.5f);
                    attackAnim = loadAnimation("lwjgl3/assets/ui/Knight Hero Platfomer/Attack 1.png", 40, 30, 0.5f);
                    break;
                }

                case AXEMAN: {
                    String path = "lwjgl3/assets/ui/Axeman/Minotaur.png";
                    idleAnim   = loadAnimationRow(path, 94, 94, 0.25f, 0, 5);
                    runAnim    = loadAnimationRow(path, 94, 94, 0.1f,  1, 8);
                    attackAnim = loadAnimationRow(path, 94, 94, 0.1f,  3, 9);
                    damageAnim = loadAnimationRow(path, 94, 94, 0.1f,  8, 3);
                    deathAnim  = loadAnimationRow(path, 94, 94, 0.15f, 9, 5);
                    break;
                }
            }
        }

        private Animation<TextureRegion> loadAnimation(String path, int frameWidth, int frameHeight, float speed) {

            Texture sheet = new Texture(path);

            int sheetWidth = sheet.getWidth();
            int frameCount = sheetWidth / frameWidth;

            TextureRegion[] frames = new TextureRegion[frameCount];

            for (int i = 0; i < frameCount; i++) {
                frames[i] = new TextureRegion(sheet, i * frameWidth, 0, frameWidth, frameHeight);
            }

            return new Animation<>(speed, frames);
        }

        public void setState(State state) {
            if (state != currentState) {
                currentState = state;
                stateTime = 0f;
            }
        }

        public void update(float delta) {
            stateTime += delta;
        }

        public TextureRegion getCurrentFrame() {
            return switch (currentState) {
                case RUN    -> runAnim.getKeyFrame(stateTime, true);
                case ATTACK -> attackAnim.getKeyFrame(stateTime, false);
                case DAMAGE -> damageAnim != null ? damageAnim.getKeyFrame(stateTime, false) : idleAnim.getKeyFrame(stateTime, true);
                default     -> idleAnim.getKeyFrame(stateTime, true);
        };
    }
    private Animation<TextureRegion> loadAnimationRow(String path, int frameWidth,
                                                      int frameHeight, float speed,
                                                      int row, int frameCount) {
        Texture sheet = new Texture(path);
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = new TextureRegion(sheet, i * frameWidth, row * frameHeight,
                frameWidth, frameHeight);
        }
        return new Animation<>(speed, frames);
    }

}





