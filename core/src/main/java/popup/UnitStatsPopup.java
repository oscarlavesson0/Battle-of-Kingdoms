package popup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import base.BaseStats;
import unit.UnitSkapare;

public class UnitStatsPopup {

    private boolean visible = false;

    private float x, y, width, height;

    private int hp = 10;
    private int attack = 1;
    private int speed = 1;
    private int defence = 1;

    private final int MAX_HP = UnitSkapare.getMaxHP();
    private final int MAX_ATTACK = UnitSkapare.getMaxAttack();
    private final int MAX_SPEED = UnitSkapare.getMaxSpeed();
    private final int MAX_DEFENCE = UnitSkapare.getMaxDefence();

    // Plus and Minus button position
    private final float BUTTON_MINUS_OFFSET = 140;
    private final float BUTTON_PLUS_OFFSET = 180;
    private final float BUTTON_WIDTH = 30;
    private final float BUTTON_HEIGHT = 30;

    private BitmapFont font = new BitmapFont();
    private SpriteBatch batch = new SpriteBatch();

    private StatsChosenListener listener;
    private BaseStats base;
    private ShapeRenderer shapeRenderer;

    public UnitStatsPopup(int x, int y, int witdth, int height){
        this.x = x;
        this.y = y;
        this.width = witdth;
        this.height = height;

        this.font = new BitmapFont();
        this.shapeRenderer = new ShapeRenderer();
        this.batch = new SpriteBatch();
    }

    public void setListener(StatsChosenListener listener){
        this.listener = listener;
    }

    public void open(BaseStats base){
        this.base = base;
        visible = true;
    }

    public void hide(){
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void render(){
        if (!visible){
            return;
        }

        if (batch.isDrawing()){
            batch.end();
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(1f, 1f, 1f, 0.95f));
        shapeRenderer.rect(x, y, width, height);

        shapeRenderer.end();


        batch.begin();
        font.setColor(Color.BLACK);

        font.draw(batch, "Create Unit", x + 20, y + height - 20);

        // Stats with plus and minus button
        font.draw(batch, "HP: " + hp, x + 20, y + height - 60);
        font.draw(batch, "[-]", x + BUTTON_MINUS_OFFSET, y + height - 60);
        font.draw(batch, "[+]", x + BUTTON_PLUS_OFFSET, y + height - 60);

        font.draw(batch, "Attack: " + attack, x + 20, y + height - 100);
        font.draw(batch, "[-]", x + BUTTON_MINUS_OFFSET, y + height - 100);
        font.draw(batch, "[+]", x + BUTTON_PLUS_OFFSET, y + height - 100);

        font.draw(batch, "Speed: " + speed, x + 20, y + height - 140);
        font.draw(batch, "[-]", x + BUTTON_MINUS_OFFSET, y + height - 140);
        font.draw(batch, "[+]", x + BUTTON_PLUS_OFFSET, y + height - 140);

        font.draw(batch, "Defence: " + defence, x + 20, y + height - 180);
        font.draw(batch, "[-]", x + BUTTON_MINUS_OFFSET, y + height - 180);
        font.draw(batch, "[+]", x + BUTTON_PLUS_OFFSET, y + height - 180);

        // Create unit button
        font.draw(batch, "[ Create Unit ]", x + 20, y + 40);

        // Close-button
        font.setColor(Color.RED);
        font.draw(batch, "[ Close ]", x + width - 80, y + height - 20);
        batch.end();
    }

    public void handleClick(float screenX, float screenY){
        if(!visible){
            return;
        }

        float realY = Gdx.graphics.getHeight() - screenY;

        // Klick outside popup
        boolean insidePopup = screenX >= x && screenX <= x + width &&
            realY >= y && realY <= y + height;

        if(!insidePopup){
            hide();
            return;
        }

        //Close button
        float closeX1 = x + width - 90;
        float closeX2 = x + width - 20;
        float closeY1 = y + height - 35;
        float closeY2 = y + height - 5;

        if (screenX >= closeX1 && screenX <= closeX2 &&
            realY >= closeY1 && realY <= closeY2) {
            hide();
            return;
        }

        // HP minus
        if (screenX >= x + BUTTON_MINUS_OFFSET && screenX <= x + BUTTON_MINUS_OFFSET + BUTTON_WIDTH &&
            realY >= y + height - 80 && realY <= y + height - 50) {
            if (hp > 1) hp--;
        }

        // HP plus
        if (screenX >= x + BUTTON_PLUS_OFFSET && screenX <= x + BUTTON_PLUS_OFFSET + BUTTON_WIDTH &&
            realY >= y + height - 80 && realY <= y + height - 50) {
            if (hp < MAX_HP) hp++;
        }

        // Attack minus
        if (screenX >= x + BUTTON_MINUS_OFFSET && screenX <= x + BUTTON_MINUS_OFFSET + BUTTON_WIDTH &&
            realY >= y + height - 120 && realY <= y + height - 90) {
            if (attack > 1) attack--;
        }

        // Attack plus
        if (screenX >= x + BUTTON_PLUS_OFFSET && screenX <= x + BUTTON_PLUS_OFFSET + BUTTON_WIDTH &&
            realY >= y + height - 120 && realY <= y + height - 90) {
            if (attack < MAX_ATTACK) attack++;
        }

        // Speed minus
        if (screenX >= x + BUTTON_MINUS_OFFSET && screenX <= x + BUTTON_MINUS_OFFSET + BUTTON_WIDTH &&
            realY >= y + height - 160 && realY <= y + height - 130) {
            if (speed > 1) speed--;
        }
        // Soeed plus
        if (screenX >= x + BUTTON_PLUS_OFFSET && screenX <= x + BUTTON_PLUS_OFFSET + BUTTON_WIDTH &&
            realY >= y + height - 160 && realY <= y + height - 130) {
            if (speed < MAX_SPEED) speed++;
        }

        // Defence minus
        if (screenX >= x + BUTTON_MINUS_OFFSET && screenX <= x + BUTTON_MINUS_OFFSET + BUTTON_WIDTH &&
            realY >= y + height - 200 && realY <= y + height - 170) {
            if (defence > 1) defence--;
        }

        // Defence plus
        if (screenX >= x + BUTTON_PLUS_OFFSET && screenX <= x + BUTTON_PLUS_OFFSET + BUTTON_WIDTH &&
            realY >= y + height - 200 && realY <= y + height - 170) {
            if (defence < MAX_DEFENCE) defence++;
        }

        // Create Unit button
        if (screenX >= x + 20 && screenX <= x + 200 &&
            realY >= y + 20 && realY <= y + 60) {

            if (listener != null) {
                listener.onStatsChosen(base, hp, attack, speed, defence);
            }

            hide();
        }
    }
}
