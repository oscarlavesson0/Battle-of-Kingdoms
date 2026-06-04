package popup;

import GuiMainGame.UnitType;
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
    private int hp      = 10;
    private int attack  = 1;
    private int speed   = 1;
    private int defence = 1;

    private UnitType selectedUnitType = UnitType.AXEMAN;

    private boolean infoMode = false;
    private unit.Unit infoUnit = null;

    private final int MAX_HP      = UnitSkapare.getMaxHP();
    private final int MAX_ATTACK  = UnitSkapare.getMaxAttack();
    private final int MAX_SPEED   = UnitSkapare.getMaxSpeed();
    private final int MAX_DEFENCE = UnitSkapare.getMaxDefence();

    private int pointsLeft = 7;

    private boolean showError = false;
    private String errorMessage = "";

    private final float BUTTON_MINUS_OFFSET = 140;
    private final float BUTTON_PLUS_OFFSET  = 180;
    private final float BUTTON_WIDTH  = 30;
    private final float BUTTON_HEIGHT = 30;

    private static final float ICON_SIZE  = 20f;
    private static final float ROW_H      = 36f;
    private static final float ICON_X_OFF = 10f;
    private static final float TEXT_X_OFF = 38f;

    // Typ-knappar
    private static final float TYPE_BTN_W   = 110f;
    private static final float TYPE_BTN_H   = 28f;
    private static final float TYPE_BTN_GAP = 8f;

    private final Color GOLD       = new Color(1f, 0.84f, 0.0f, 1f);
    private final Color SEL_COLOR  = new Color(0.2f, 0.55f, 0.2f, 1f);   // grön = vald
    private final Color UNSEL_COLOR = new Color(0.3f, 0.3f, 0.3f, 1f);   // grå = ej vald

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

    private float infoWidth    = 200f;
    private float infoHeight   = 200f;
    private float createWidth;
    private float createHeight;

    public UnitStatsPopup(int x, int y, int width, int height, OrthographicCamera camera) {
        this.x            = x;
        this.y            = y;
        this.createWidth  = width;
        this.createHeight = height;
        this.width        = width;
        this.height       = height;
        this.camera       = camera;

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

    public void setListener(StatsChosenListener listener) { this.listener = listener; }

    public void open(BaseStats base) {
        this.base             = base;
        this.hp               = 10;
        this.attack           = 1;
        this.speed            = 1;
        this.defence          = 1;
        this.pointsLeft       = 7;
        this.showError        = false;
        this.infoMode         = false;
        this.infoUnit         = null;
        this.selectedUnitType = UnitType.AXEMAN;
        this.width            = createWidth;
        this.height           = createHeight;
        visible = true;
    }

    public void showUnitInfo(unit.Unit unit) {
        if (unit == null) return;
        this.infoMode   = true;
        this.infoUnit   = unit;
        this.hp         = unit.getCurrentHp();
        this.attack     = unit.getAttack();
        this.speed      = unit.getSpeed();
        this.defence    = unit.getDefence();
        this.pointsLeft = 0;
        this.showError  = false;
        this.width      = infoWidth;
        this.height     = infoHeight;
        visible = true;
    }

    public void hide()         { visible = false; }
    public boolean isVisible() { return visible; }

    public void ShowError(String msg) {
        this.errorMessage = msg;
        this.showError    = true;
    }

    // Hjälp: x/y för de två typ-knapparna
    private float knightBtnX()  { return x + 10; }
    private float axemanBtnX()  { return x + 10 + TYPE_BTN_W + TYPE_BTN_GAP; }
    private float typeBtnY()    { return y + height - TYPE_BTN_H - 12f; }

    public void render() {
        if (!visible) return;

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        if (batch.isDrawing()) batch.end();

        // Bakgrund + ram
        batch.begin();
        batch.draw(woodBackground, x, y, width, height);
        batch.draw(frameTexture, x - 13, y - 15, width + 30, height + 30);
        batch.end();

        // --- Typ-väljare (bara skapa-läge) ---
        if (!infoMode) {
            if (shapeRenderer.isDrawing()) shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            shapeRenderer.setColor(selectedUnitType == UnitType.KNIGHT ? SEL_COLOR : UNSEL_COLOR);
            shapeRenderer.rect(knightBtnX(), typeBtnY(), TYPE_BTN_W, TYPE_BTN_H);

            shapeRenderer.setColor(selectedUnitType == UnitType.AXEMAN ? SEL_COLOR : UNSEL_COLOR);
            shapeRenderer.rect(axemanBtnX(), typeBtnY(), TYPE_BTN_W, TYPE_BTN_H);

            shapeRenderer.end();

            batch.begin();
            font.setColor(GOLD);
            font.draw(batch, "Knight",  knightBtnX() + 20, typeBtnY() + TYPE_BTN_H - 6);
            font.draw(batch, "Axeman",  axemanBtnX() + 20, typeBtnY() + TYPE_BTN_H - 6);
            batch.end();
        }

        // --- Stats ---
        batch.begin();
        font.setColor(GOLD);

        float topY = y + height;

        // Poäng-rad (bara skapa-läge)
        if (!infoMode) {
            float pRow = topY - TYPE_BTN_H - 28f;
            batch.draw(pointsIcon, x + ICON_X_OFF, pRow - ICON_SIZE, ICON_SIZE, ICON_SIZE);
            font.draw(batch, "Points: " + pointsLeft, x + TEXT_X_OFF, pRow);
        }

        float startY = topY - (infoMode ? 35f : TYPE_BTN_H + 56f);
        drawStatRow(batch, hpIcon,     "HP: "  + hp,      startY);
        drawStatRow(batch, swordIcon,  "ATK: " + attack,  startY - ROW_H);
        drawStatRow(batch, speedIcon,  "SPD: " + speed,   startY - ROW_H * 2);
        drawStatRow(batch, shieldIcon, "DEF: " + defence, startY - ROW_H * 3);

        if (!infoMode) {
            float[] rows = { startY, startY - ROW_H, startY - ROW_H * 2, startY - ROW_H * 3 };
            for (float ry : rows) {
                font.draw(batch, "[-]", x + BUTTON_MINUS_OFFSET, ry);
                font.draw(batch, "[+]", x + BUTTON_PLUS_OFFSET,  ry);
            }
            float btnY = y + 35f;
            font.draw(batch, "[ Create Unit ]", x + 20, btnY);
            batch.draw(coinIcon, x + 155, btnY - 18, 20, 15);
            font.draw(batch, "100g", x + 178, btnY);
        }

        batch.draw(letterXIcon, x + width - 35, y + height - 35, 25, 25);

        if (showError) {
            if (batch.isDrawing()) batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.WHITE);
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

    private void drawStatRow(SpriteBatch batch, Texture icon, String label, float rowY) {
        batch.draw(icon, x + ICON_X_OFF, rowY - ICON_SIZE + 2, ICON_SIZE, ICON_SIZE);
        font.setColor(GOLD);
        font.draw(batch, label, x + TEXT_X_OFF, rowY);
    }

    public void handleClick(float screenX, float screenY) {
        if (!visible) return;

        if (showError) {
            if (screenX >= x + 20 && screenX <= x + 120 &&
                screenY >= y + height - 320 && screenY <= y + height - 280) {
                showError = false;
                return;
            }
            if (!(screenX >= x && screenX <= x + width && screenY >= y && screenY <= y + height))
                showError = false;
            return;
        }

        boolean inside = screenX >= x && screenX <= x + width &&
            screenY >= y && screenY <= y + height;
        if (!inside) { hide(); return; }

        // Stäng-kryss
        if (screenX >= x + width - 60 && screenX <= x + width - 10 &&
            screenY >= y + height - 50 && screenY <= y + height - 10) {
            hide();
            return;
        }

        if (infoMode) return;

        // Typ-knappar
        if (inRange(screenX, screenY, knightBtnX(), typeBtnY(), TYPE_BTN_W, TYPE_BTN_H)) {
            selectedUnitType = UnitType.KNIGHT;
            return;
        }
        if (inRange(screenX, screenY, axemanBtnX(), typeBtnY(), TYPE_BTN_W, TYPE_BTN_H)) {
            selectedUnitType = UnitType.AXEMAN;
            return;
        }

        // Plus/minus
        float topY   = y + height;
        float startY = topY - TYPE_BTN_H - 56f;
        float[] rowTops = { startY, startY - ROW_H, startY - ROW_H * 2, startY - ROW_H * 3 };
        int[]   maxs    = { MAX_HP, MAX_ATTACK, MAX_SPEED, MAX_DEFENCE };

        for (int i = 0; i < rowTops.length; i++) {
            float ry = rowTops[i];
            if (inRange(screenX, screenY, x + BUTTON_MINUS_OFFSET, ry - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT))
                dec(i);
            if (inRange(screenX, screenY, x + BUTTON_PLUS_OFFSET, ry - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT))
                inc(i, maxs[i]);
        }

        // Create Unit
        if (screenX >= x + 20 && screenX <= x + 200 &&
            screenY >= y + 20 && screenY <= y + 55) {
            if (listener != null)
                listener.onStatsChosen(base, hp, attack, speed, defence, selectedUnitType);
            if (!showError) hide();
        }
    }

    private void inc(int stat, int max) {
        if (pointsLeft <= 0) return;
        switch (stat) {
            case 0 -> { if (hp      < MAX_HP)      { hp++;      pointsLeft--; } }
            case 1 -> { if (attack  < MAX_ATTACK)  { attack++;  pointsLeft--; } }
            case 2 -> { if (speed   < MAX_SPEED)   { speed++;   pointsLeft--; } }
            case 3 -> { if (defence < MAX_DEFENCE) { defence++; pointsLeft--; } }
        }
    }

    private void dec(int stat) {
        switch (stat) {
            case 0 -> { if (hp      > 1) { hp--;      pointsLeft++; } }
            case 1 -> { if (attack  > 1) { attack--;  pointsLeft++; } }
            case 2 -> { if (speed   > 1) { speed--;   pointsLeft++; } }
            case 3 -> { if (defence > 1) { defence--; pointsLeft++; } }
        }
    }

    private boolean inRange(float cx, float cy, float bx, float by, float bw, float bh) {
        return cx >= bx && cx <= bx + bw && cy >= by && cy <= by + bh;
    }
}
