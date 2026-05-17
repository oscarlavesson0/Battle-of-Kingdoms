package building;

import base.Player;
import terrain.Tile;
import terrain.TileController;

public class Blacksmith extends Building {
    public Blacksmith(int x, int y, Player owner, int id, int constructionTime, TileController tileController) {
        super(x, y, owner, "Blacksmith", constructionTime, tileController);
    }

    @Override
    public void ActivateBuildingAbility(Tile[][] tileGrid) {

    }
}
