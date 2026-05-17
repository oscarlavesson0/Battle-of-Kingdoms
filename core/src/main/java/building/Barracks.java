package building;

import base.Player;
import terrain.Tile;
import terrain.TileController;

public class Barracks extends Building {
    public Barracks(int x, int y, Player owner, int id, int constructionTime, TileController tileController) {
        super(x, y, owner, "Barracks", constructionTime, tileController);
    }

    @Override
    public void ActivateBuildingAbility(Tile[][] tileGrid) {

    }
}
