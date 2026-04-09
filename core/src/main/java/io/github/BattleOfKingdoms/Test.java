package io.github.BattleOfKingdoms;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Test extends ApplicationAdapter {

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1); // Röd bakgrund
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
