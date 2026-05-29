package building;

import base.Player;
import terrain.TileController;
import unit.Unit;

public class Hospital extends RangeBuilding{
    public Hospital(int x, int y, Player owner, TileController tileController){
        super(x, y, owner, "Hospital", 3, tileController, 3, 2);
    }
    public void affectUnit(Unit unit){
        if (unit.getPlayer() == getOwner()){
            unit.addToCurrentHP(affectPoints);
        }
    }
}
