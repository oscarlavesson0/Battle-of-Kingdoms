package building;

import base.Player;
import terrain.Tile;
import terrain.TileController;

public class Blacksmith extends Building {
    public Blacksmith(int x, int y, Player owner, TileController tileController) {
        super(x, y, owner, "Blacksmith", 7, tileController);
    }

    @Override
    public void ActivateBuildingAbility(Tile[][] tileGrid) {

    }
}
