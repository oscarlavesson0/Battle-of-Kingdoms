package building;

import base.Player;
import terrain.Tile;
import terrain.TileController;

public class Barracks extends Building {
    public Barracks(int x, int y, Player owner, TileController tileController) {
        super(x, y, owner, "Barracks", 5, tileController);
    }

    @Override
    public void ActivateBuildingAbility() {

    }
}
