package GuiMainGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import popup.BasePopup;
import terrain.Tile;
import terrain.TileController;

public class GameInput extends InputAdapter {

    private TileController tileController;
    private BasePopup basePopup;

    public GameInput(TileController tileController, BasePopup basePopup) {
        this.tileController = tileController;
        this.basePopup = basePopup;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        // If popup is open → let popup handle click
        if (basePopup != null && basePopup.isVisible()) {
            basePopup.handleClick(screenX, screenY);
            return true;
        }

        // Convert screen → tile
        int tileX = screenX / WorldMap.TILE_SIZE;
        int tileY = (Gdx.graphics.getHeight() - screenY) / WorldMap.TILE_SIZE;

        Tile[][] grid = tileController.getTileGrid();

        // Bounds check
        if (tileX < 0 || tileY < 0 || tileX >= grid.length || tileY >= grid[0].length)
            return false;

        Tile clickedTile = grid[tileX][tileY];

        // If tile has a base → show popup
        if (clickedTile.getBase() != null) {
            basePopup.show(clickedTile.getBase());
            return true;
        }

        return false;
    }
}
