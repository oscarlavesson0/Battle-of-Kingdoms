package building;

import base.Player;
import terrain.TileController;
import unit.Unit;

public class Hospital extends RangeBuilding{
    public Hospital(int x, int y, Player owner, BuildingType buildingType, TileController tileController){
        super(x, y, owner, buildingType, tileController);
    }
    public void affectUnit(Unit unit){
        if (unit.getPlayer() == getOwner()){
            unit.addToCurrentHP(affectPoints);
        }
    }
}
