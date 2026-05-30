package building;

import base.Player;
import terrain.Tile;
import terrain.TileController;

public class Blacksmith extends Building {
    public Blacksmith(int x, int y, Player owner, BuildingType buildingType, TileController tileController) {
        super(x, y, owner, buildingType, tileController);
    }

    @Override
    public void activateBuildingAbility() {

    }
}
