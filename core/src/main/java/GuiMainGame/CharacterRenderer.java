package GuiMainGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CharacterRenderer {

        private Animation<TextureRegion> idleAnim;
        private Animation<TextureRegion> runAnim;
        private Animation<TextureRegion> attackAnim;

        private State currentState = State.IDLE;
        private float stateTime = 0f;

        public CharacterRenderer() {

            idleAnim   = loadAnimation("lwjgl3/assets/ui/Knight Hero Platfomer/Combat Ready Idle.png",   22, 24, 0.5f);
            runAnim    = loadAnimation("lwjgl3/assets/ui/Knight Hero Platfomer/Run.png",    22, 24, 0.5f);
            attackAnim = loadAnimation("lwjgl3/assets/ui/Knight Hero Platfomer/Attack 1.png", 40, 30, 0.5f);
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
                default     -> idleAnim.getKeyFrame(stateTime, true);
            };
        }
    }





