package building;

import base.Player;
import terrain.TileController;
import unit.Unit;

public class Tower extends RangeBuilding{
    public Tower(int x, int y, Player owner, TileController tileController){
        super(x, y, owner, "Tower", 3, tileController, 3, -3);
    }
    public void affectUnit(Unit unit){
        if (unit.getPlayer() != getOwner()){
            unit.addToCurrentHP(affectPoints);
        }
    }
}
