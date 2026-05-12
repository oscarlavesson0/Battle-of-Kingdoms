package popup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import base.BaseStats;

public class UnitStatsPopup {

    private boolean visible = false;

    private int x;
    private int y;
    private int witdh;
    private int height;

    private int hp = 10;
    private int attack = 1;
    private int speed = 1;
    private int defence = 1;

    private BitmapFont font = new BitmapFont();
    private SpriteBatch batch = new SpriteBatch();

    private StatsChosenListener listener;
    private BaseStats base;

    public UnitStatsPopup(int x, int y, int witdh, int height){
        this.x = x;
        this.y = y;
        this.witdh = witdh;
        this.height = height;
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

        batch.begin();
        font.setColor(Color.BLACK);

        font.draw(batch, "Create Unit", x + 20, y + height - 20);

        font.draw(batch, "HP: " + hp, x + 20, y + height - 60);
        font.draw(batch, "Attack: " + attack, x + 20, y + height - 100);
        font.draw(batch, "Speed: " + speed, x + 20, y + height - 140);
        font.draw(batch, "Defence: " + defence, x + 20, y + height - 180);

        font.draw(batch, "[ Create Unit ]", x + 20, y + 40);

        batch.end();
    }

    public void handleClick(float screenX, float screenY){
        if(!visible){
            return;
        }

        float realY = Gdx.graphics.getHeight() - screenY;

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
