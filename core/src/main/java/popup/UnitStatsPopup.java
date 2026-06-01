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

    // === Create Unit-läge ===
    private int hp = 10;
    private int attack = 1;
    private int speed = 1;
    private int defence = 1;

    private final int MAX_HP      = UnitSkapare.getMaxHP();
    private final int MAX_ATTACK  = UnitSkapare.getMaxAttack();
    private final int MAX_SPEED   = UnitSkapare.getMaxSpeed();
    private final int MAX_DEFENCE = UnitSkapare.getMaxDefence();

    private int pointsLeft = 7;

    // === Info-läge – separata variabler, rör aldrig create-unit-läget ===
    private boolean infoMode = false;
    private unit.Unit infoUnit = null;
    private int infoHp, infoAttack, infoSpeed, infoDefence;
    private static final float INFO_WIDTH   = 120f;
    private static final float INFO_HEIGHT  = 240f;

    // Originalstorlek för create-unit-läget
    private static final float CREATE_WIDTH  = 300f;
    private static final float CREATE_HEIGHT = 320f;

    // === Felmeddelande ===
    private boolean showError = false;
    private String errorMessage = "";

    // Plus/minus-knappar
    private final float BUTTON_MINUS_OFFSET = 140;
    private final float BUTTON_PLUS_OFFSET  = 180;
    private final float BUTTON_WIDTH        = 30;
    private final float BUTTON_HEIGHT       = 30;

    private BitmapFont font;
    private SpriteBatch batch;
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

    private OrthographicCamera camera;

    public UnitStatsPopup(int x, int y, int width, int height, OrthographicCamera camera) {
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;
        this.camera = camera;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            Gdx.files.internal("lwjgl3/assets/ui/font/PixelWarden.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 16;
        this.font = generator.generateFont(parameter);
        generator.dispose();

        this.shapeRenderer  = new ShapeRenderer();
        this.batch          = new SpriteBatch();

        this.pointsIcon     = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/gem.png"));
        this.swordIcon      = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/sword.png"));
        this.shieldIcon     = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/shield.png"));
        this.coinIcon       = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/coin.png"));
        this.speedIcon      = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/boot.png"));
        this.letterXIcon    = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/letter-x.png"));
        this.hpIcon         = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/heart.png"));
        this.woodBackground = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/woodtexture.png"));
        this.frameTexture   = new Texture(Gdx.files.internal("lwjgl3/assets/ui/StatIcons/frame.png"));
    }

    public void setListener(StatsChosenListener listener) {
        this.listener = listener;
    }

    // -----------------------------------------------------------------------
    // Öppna i CREATE UNIT-läge
    // -----------------------------------------------------------------------
    public void open(BaseStats base) {
        this.base       = base;
        this.hp         = 10;
        this.attack     = 1;
        this.speed      = 1;
        this.defence    = 1;
        this.pointsLeft = 7;
        this.showError  = false;
        this.infoMode   = false;
        this.width      = CREATE_WIDTH;
        this.height     = CREATE_HEIGHT;
        visible = true;
    }

    // -----------------------------------------------------------------------
    // Öppna i INFO-läge – skriver INTE över create-unit-variablerna
    // -----------------------------------------------------------------------
    public void showUnitInfo(unit.Unit unit) {
        if (unit == null) return;
        this.infoMode    = true;
        this.infoUnit    = unit;
        this.visible     = true;
        this.infoHp      = unit.getCurrentHp();
        this.infoAttack  = unit.getAttack();
        this.infoSpeed   = unit.getSpeed();
        this.infoDefence = unit.getDefence();
        this.pointsLeft  = 0;
    }

    public void hide()           { visible = false; }
    public boolean isVisible()   { return visible; }

    public void ShowError(String errorMessage) {
        this.errorMessage = errorMessage;
        this.showError    = true;
    }

    // -----------------------------------------------------------------------
    // RENDER
    // -----------------------------------------------------------------------
    public void render() {
        if (!visible) return;

        float w = infoMode ? INFO_WIDTH  : width;
        float h = infoMode ? INFO_HEIGHT : height;

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        if (batch.isDrawing()) batch.end();

        // Bakgrund + ram
        batch.begin();
        batch.draw(woodBackground, x, y, w, h);
        batch.draw(frameTexture, x - 13, y - 15, w + 30, h + 30);
        batch.end();

        batch.begin();
        font.setColor(Color.GOLD);

        // --- Points left: ikon till vänster, text till höger ---
        if (!infoMode) {
            batch.draw(pointsIcon, x + 20, y + h - 42, 20, 20);
            font.draw(batch, "Points left: " + pointsLeft, x + 46, y + h - 26);
        }

        // Stats-värden från rätt källa
        int displayHp      = infoMode ? infoHp      : hp;
        int displayAttack  = infoMode ? infoAttack  : attack;
        int displaySpeed   = infoMode ? infoSpeed   : speed;
        int displayDefence = infoMode ? infoDefence : defence;

        // Fasta rader med 45px mellanrum
        // Ikon ritas på row_Y (bottenkant), text på row_Y + 15 (mittlinje)
        float row1 = y + h - 90;   // HP
        float row2 = y + h - 135;  // Attack
        float row3 = y + h - 180;  // Speed
        float row4 = y + h - 225;  // Defence

        // HP
        batch.draw(hpIcon, x + 20, row1, 20, 20);
        font.draw(batch, "" + displayHp, x + 48, row1 + 15);
        if (!infoMode) {
            font.draw(batch, "[-]", x + BUTTON_MINUS_OFFSET, row1 + 15);
            font.draw(batch, "[+]", x + BUTTON_PLUS_OFFSET,  row1 + 15);
        }

        // Attack
        batch.draw(swordIcon, x + 20, row2, 20, 20);
        font.draw(batch, "" + displayAttack, x + 48, row2 + 15);
        if (!infoMode) {
            font.draw(batch, "[-]", x + BUTTON_MINUS_OFFSET, row2 + 15);
            font.draw(batch, "[+]", x + BUTTON_PLUS_OFFSET,  row2 + 15);
        }

        // Speed
        batch.draw(speedIcon, x + 20, row3, 20, 20);
        font.draw(batch, "" + displaySpeed, x + 48, row3 + 15);
        if (!infoMode) {
            font.draw(batch, "[-]", x + BUTTON_MINUS_OFFSET, row3 + 15);
            font.draw(batch, "[+]", x + BUTTON_PLUS_OFFSET,  row3 + 15);
        }

        // Defence
        batch.draw(shieldIcon, x + 20, row4, 20, 20);
        font.draw(batch, "" + displayDefence, x + 48, row4 + 15);
        if (!infoMode) {
            font.draw(batch, "[-]", x + BUTTON_MINUS_OFFSET, row4 + 15);
            font.draw(batch, "[+]", x + BUTTON_PLUS_OFFSET,  row4 + 15);
        }

        // Create Unit-knapp
        if (!infoMode) {
            font.draw(batch, " [ Create Unit ]", x + 20, y + 50);
            batch.draw(coinIcon, x + 150, y + 37, 20, 15);
            font.draw(batch, "100", x + 175, y + 50);
        }

        // Stäng-knapp (X)
        batch.draw(letterXIcon, x + w - 50, y + h - 50, 25, 25);

        // Felmeddelande
        if (showError) {
            if (batch.isDrawing()) batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(1f, 1f, 1f, 0.95f));
            shapeRenderer.rect(x + 10, y + h - 330, 260, 90);
            shapeRenderer.end();
            batch.begin();
            font.setColor(Color.RED);
            font.draw(batch, errorMessage, x + 20, y + h - 260);
            font.setColor(Color.BLACK);
            font.draw(batch, "[ OK ]", x + 20, y + h - 300);
        }

        batch.end();
    }

    // -----------------------------------------------------------------------
    // KLICK-HANTERING
    // -----------------------------------------------------------------------
    public void handleClick(float screenX, float screenY) {
        if (!visible) return;

        float w = infoMode ? INFO_WIDTH  : width;
        float h = infoMode ? INFO_HEIGHT : height;

        if (showError) {
            if (screenX >= x + 20 && screenX <= x + 120 &&
                screenY >= y + h - 320 && screenY <= y + h - 280) {
                showError = false;
                return;
            }
            boolean insidePopup = screenX >= x && screenX <= x + w &&
                screenY >= y && screenY <= y + h;
            if (!insidePopup) showError = false;
            return;
        }

        // Klick utanför → stäng
        boolean insidePopup = screenX >= x && screenX <= x + w &&
            screenY >= y && screenY <= y + h;
        if (!insidePopup) { hide(); return; }

        // Stäng-knapp (X)
        if (screenX >= x + w - 90 && screenX <= x + w - 20 &&
            screenY >= y + h - 35 && screenY <= y + h - 5) {
            hide();
            return;
        }

        // Knappar nedan bara i create-läge
        if (infoMode) return;

        // Hitboxar matchar render-raderna exakt
        float row1 = y + h - 90;
        float row2 = y + h - 135;
        float row3 = y + h - 180;
        float row4 = y + h - 225;

        // HP
        if (screenX >= x + BUTTON_MINUS_OFFSET && screenX <= x + BUTTON_MINUS_OFFSET + BUTTON_WIDTH &&
            screenY >= row1 && screenY <= row1 + 20) {
            if (hp > 1) { hp--; pointsLeft++; }
        }
        if (screenX >= x + BUTTON_PLUS_OFFSET && screenX <= x + BUTTON_PLUS_OFFSET + BUTTON_WIDTH &&
            screenY >= row1 && screenY <= row1 + 20) {
            if (hp < MAX_HP && pointsLeft > 0) { hp++; pointsLeft--; }
        }

        // Attack
        if (screenX >= x + BUTTON_MINUS_OFFSET && screenX <= x + BUTTON_MINUS_OFFSET + BUTTON_WIDTH &&
            screenY >= row2 && screenY <= row2 + 20) {
            if (attack > 1) { attack--; pointsLeft++; }
        }
        if (screenX >= x + BUTTON_PLUS_OFFSET && screenX <= x + BUTTON_PLUS_OFFSET + BUTTON_WIDTH &&
            screenY >= row2 && screenY <= row2 + 20) {
            if (attack < MAX_ATTACK && pointsLeft > 0) { attack++; pointsLeft--; }
        }

        // Speed
        if (screenX >= x + BUTTON_MINUS_OFFSET && screenX <= x + BUTTON_MINUS_OFFSET + BUTTON_WIDTH &&
            screenY >= row3 && screenY <= row3 + 20) {
            if (speed > 1) { speed--; pointsLeft++; }
        }
        if (screenX >= x + BUTTON_PLUS_OFFSET && screenX <= x + BUTTON_PLUS_OFFSET + BUTTON_WIDTH &&
            screenY >= row3 && screenY <= row3 + 20) {
            if (speed < MAX_SPEED && pointsLeft > 0) { speed++; pointsLeft--; }
        }

        // Defence
        if (screenX >= x + BUTTON_MINUS_OFFSET && screenX <= x + BUTTON_MINUS_OFFSET + BUTTON_WIDTH &&
            screenY >= row4 && screenY <= row4 + 20) {
            if (defence > 1) { defence--; pointsLeft++; }
        }
        if (screenX >= x + BUTTON_PLUS_OFFSET && screenX <= x + BUTTON_PLUS_OFFSET + BUTTON_WIDTH &&
            screenY >= row4 && screenY <= row4 + 20) {
            if (defence < MAX_DEFENCE && pointsLeft > 0) { defence++; pointsLeft--; }
        }

        // Create Unit-knapp
        if (screenX >= x + 20 && screenX <= x + 200 &&
            screenY >= y + 30 && screenY <= y + 70) {
            if (listener != null) {
                listener.onStatsChosen(base, hp, attack, speed, defence);
            }
            if (!showError) hide();
        }
    }
}
