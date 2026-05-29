package popup;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.Texture;
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

    private unit.Unit unit;

    private boolean infoMode = false;
    private unit.Unit infoUnit = null;

    private final int MAX_HP = UnitSkapare.getMaxHP();
    private final int MAX_ATTACK = UnitSkapare.getMaxAttack();
    private final int MAX_SPEED = UnitSkapare.getMaxSpeed();
    private final int MAX_DEFENCE = UnitSkapare.getMaxDefence();

    //Skill points
    private int pointsLeft = 7;

    //Error message
    private boolean showError = false;
    private String errorMessage = "";

    // Plus and Minus button position
    private final float BUTTON_MINUS_OFFSET = 140;
    private final float BUTTON_PLUS_OFFSET = 180;
    private final float BUTTON_WIDTH = 30;
    private final float BUTTON_HEIGHT = 30;
    private final Color GOLD = new Color(1f, 0.84f, 0.0f, 1f);

    private BitmapFont font = new BitmapFont();
    private SpriteBatch batch = new SpriteBatch();

    private StatsChosenListener listener;
    private BaseStats base;
    private ShapeRenderer shapeRenderer;
    private Texture pointsIcon;
    private Texture swordIcon;
    private Texture shieldIcon;
    private Texture coinIcon;
    private Texture speedIcon;
    private Texture letterXIcon;
    private Texture hpIcon;
    private Texture woodBackground;
    private Texture frameTexture;
    private com.badlogic.gdx.graphics.OrthographicCamera camera;


    public UnitStatsPopup(int x, int y, int witdth, int height, OrthographicCamera camera){
        this.x = x;
        this.y = y;
        this.width = witdth;
        this.height = height;
        this.camera = camera;

        //this.font = new BitmapFont();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("lwjgl3/assets/ui/font/PixelWarden.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 16;
        this.font = generator.generateFont(parameter);
        generator.dispose();
        this.shapeRenderer = new ShapeRenderer();
        this.batch = new SpriteBatch();

        this.pointsIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/gem.png"));
        this.swordIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/sword.png"));
        this.shieldIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/shield.png"));
        this.coinIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/coin.png"));
        this.speedIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/boot.png"));
        this.letterXIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/letter-x.png"));
        this.hpIcon = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/heart.png"));
        this.woodBackground = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/woodtexture.png"));
        this.frameTexture = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/frame.png"));
    }

    public void setListener(StatsChosenListener listener){
        this.listener = listener;
    }

    public void open(BaseStats base){
        this.base = base;

        this.hp = 10;
        this.attack = 1;
        this.speed = 1;
        this.defence = 1;

        this.pointsLeft = 7;
        this.showError = false;
        visible = true;
    }

    public void showUnitInfo(unit.Unit unit) {
        if (unit == null) return;

        this.infoMode = true;
        this.infoUnit = unit;
        this.visible = true;

        // Gör popupen mindre i info-läge
        this.width = 120;
        this.height = 240;

        //Fyll stats
        this.hp      = unit.getCurrentHp();
        this.attack  = unit.getAttack();
        this.speed   = unit.getSpeed();
        this.defence = unit.getDefence();

        // Inga skill points i info-läge
        this.pointsLeft = 0;
    }



    public void hide(){
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void ShowError(String errorMessage){
        this.errorMessage = errorMessage;
        this.showError = true;
    }

    public void render(){
        if (!visible){
            return;
        }
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        if (batch.isDrawing()){
            batch.end();
        }

        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(new Color(1f, 1f, 1f, 0.95f));
        //shapeRenderer.rect(x, y, width, height);
        //shapeRenderer.end();

        batch.begin();
        batch.draw(woodBackground, x, y, width, height);
        batch.draw(frameTexture, x - 13, y - 15, width + 30, height + 30);
        batch.end();


        batch.begin();
        font.setColor(Color.GOLD);

        //font.draw(batch, "Create Unit", x + 20, y + height - 20);

        // Points left
        if (!infoMode) batch.draw(pointsIcon, x + 20, y + height - 48, 20, 20);
        if (!infoMode) font.draw(batch, "      Points left: " + pointsLeft, x + 20, y + height - 26);

        // Stats with plus and minus button
        batch.draw(hpIcon, x + 20, y + height - 80, 20, 20);
        font.draw(batch, "      " + hp, x + 20, y + height - 60);
        if (!infoMode) font.draw(batch, "[-]", x + BUTTON_MINUS_OFFSET, y + height - 60);
        if (!infoMode) font.draw(batch, "[+]", x + BUTTON_PLUS_OFFSET, y + height - 60);

        batch.draw(swordIcon, x + 20, y + height - 116, 20, 20);
        font.draw(batch, "      " + attack, x + 20, y + height - 100);
        if (!infoMode) font.draw(batch, "[-]", x + BUTTON_MINUS_OFFSET, y + height - 100);
        if (!infoMode) font.draw(batch, "[+]", x + BUTTON_PLUS_OFFSET, y + height - 100);

        batch.draw(speedIcon, x + 20, y + height - 160, 20, 20);
        font.draw(batch, "      " + speed, x + 20, y + height - 140);
        if (!infoMode) font.draw(batch, "[-]", x + BUTTON_MINUS_OFFSET, y + height - 140);
        if (!infoMode) font.draw(batch, "[+]", x + BUTTON_PLUS_OFFSET, y + height - 140);

        batch.draw(shieldIcon, x + 20, y + height - 200, 20, 20);
        font.draw(batch, "      " + defence, x + 20, y + height - 180);
        if (!infoMode)  font.draw(batch, "[-]", x + BUTTON_MINUS_OFFSET, y + height - 180);
        if (!infoMode) font.draw(batch, "[+]", x + BUTTON_PLUS_OFFSET, y + height - 180);

        // Create unit button
        if (!infoMode) {
            font.draw(batch, "    [ Create Unit ]", x + 20, y + 40);
            batch.draw(coinIcon, x + 150, y + 7 + 20, 20, 15);
            font.draw(batch, "100", x + 175, y + 40);
        }

        // Close-button
        //font.setColor(Color.RED);
        //font.draw(batch, "[ Close ]", x + width - 80, y + height - 20);

        batch.draw(letterXIcon, x + width - 50, y + height - 50, 25, 25);
        // Error message
        if (showError){
            if (batch.isDrawing()){
                batch.end();
            }

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(1f, 1f, 1f, 0.95f));
            shapeRenderer.rect(x + 10, y + height - 330, 260, 90);
            shapeRenderer.end();

            batch.begin();

            font.setColor(Color.RED);
            font.draw(batch, errorMessage, x + 20, y + height - 260);

            font.setColor(Color.BLACK);
            font.draw(batch, "[ OK ]", x + 20, y + height - 300);
        }
        batch.end();
    }

    public void handleClick(float screenX, float screenY){
        if(!visible){
            return;
        }

        if(showError){
            if (screenX >= x + 20 && screenX <= x + 120 &&
                screenY >= y + height - 320 && screenY <= y + height - 280) {

                showError = false;
                return;
            }

            boolean insidePopup = screenX >= x && screenX <= x + width &&
                screenY >= y && screenY <= y + height;

            if (!insidePopup) {
                showError = false;
            }

            return;
        }

        // Klick outside popup
        boolean insidePopup = screenX >= x && screenX <= x + width &&
            screenY >= y && screenY <= y + height;

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
            screenY >= closeY1 && screenY <= closeY2) {
            hide();
            return;
        }

        // HP minus
        if (screenX >= x + BUTTON_MINUS_OFFSET && screenX <= x + BUTTON_MINUS_OFFSET + BUTTON_WIDTH &&
            screenY >= y + height - 80 && screenY <= y + height - 50) {
            if (hp > 1) {
                hp--;
                pointsLeft++;
            }
        }

        // HP plus
        if (screenX >= x + BUTTON_PLUS_OFFSET && screenX <= x + BUTTON_PLUS_OFFSET + BUTTON_WIDTH &&
            screenY >= y + height - 80 && screenY <= y + height - 50) {
            if (hp < MAX_HP && pointsLeft > 0) {
                hp++;
                pointsLeft--;
            }
        }

        // Attack minus
        if (screenX >= x + BUTTON_MINUS_OFFSET && screenX <= x + BUTTON_MINUS_OFFSET + BUTTON_WIDTH &&
            screenY >= y + height - 120 && screenY <= y + height - 90) {
            if (attack > 1){
                attack--;
                pointsLeft++;
            }
        }

        // Attack plus
        if (screenX >= x + BUTTON_PLUS_OFFSET && screenX <= x + BUTTON_PLUS_OFFSET + BUTTON_WIDTH &&
            screenY >= y + height - 120 && screenY <= y + height - 90) {
            if (attack < MAX_ATTACK && pointsLeft > 0) {
                attack++;
                pointsLeft--;
            }
        }

        // Speed minus
        if (screenX >= x + BUTTON_MINUS_OFFSET && screenX <= x + BUTTON_MINUS_OFFSET + BUTTON_WIDTH &&
            screenY >= y + height - 160 && screenY <= y + height - 130) {
            if (speed > 1) {
                speed--;
                pointsLeft++;
            }
        }
        // Speed plus
        if (screenX >= x + BUTTON_PLUS_OFFSET && screenX <= x + BUTTON_PLUS_OFFSET + BUTTON_WIDTH &&
            screenY >= y + height - 160 && screenY <= y + height - 130) {
            if (speed < MAX_SPEED && pointsLeft > 0) {
                speed++;
                pointsLeft--;
            }
        }

        // Defence minus
        if (screenX >= x + BUTTON_MINUS_OFFSET && screenX <= x + BUTTON_MINUS_OFFSET + BUTTON_WIDTH &&
            screenY >= y + height - 200 && screenY <= y + height - 170) {
            if (defence > 1) {
                defence--;
                pointsLeft++;
            }
        }

        // Defence plus
        if (screenX >= x + BUTTON_PLUS_OFFSET && screenX <= x + BUTTON_PLUS_OFFSET + BUTTON_WIDTH &&
            screenY >= y + height - 200 && screenY <= y + height - 170) {
            if (defence < MAX_DEFENCE && pointsLeft > 0) {
                defence++;
                pointsLeft--;
            }
        }

        // Create Unit button
        if (screenX >= x + 20 && screenX <= x + 200 &&
            screenY >= y + 20 && screenY <= y + 60) {

            if (listener != null) {
                listener.onStatsChosen(base, hp, attack, speed, defence);
            }

            if(!showError){
                hide();
            }
        }
    }
}
