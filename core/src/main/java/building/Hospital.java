package building;

import base.Player;
import terrain.Tile;
import terrain.TileController;
import unit.Unit;

public class Hospital extends Building{

    int healingRange;
    int healingPoints;
    int mapRange;

    TileController tileController;

    public Hospital(int x, int y, Player owner, TileController tileController){
        super(x, y, owner, "Hospital", 3, tileController);
        healingRange = 3;
        healingPoints = 10;
        this.tileController = tileController;
        mapRange = tileController.getTileGrid().length;
    }

    /**
     * Hospital ger alla units av samma ägare som hospital
     * extra HP runtom hospitalbyggnaden varje turn.
     * @author Joel
     */
    @Override
    public void ActivateBuildingAbility() {
        System.out.println("Activation!! ---------");
        for (int i = -healingRange; i < healingRange; i++){
            for (int j = -healingRange; j < healingRange; j++){
                int a = super.getX() + i;
                int b = super.getY() + j;
                //System.out.println("building pos: x: " + super.getX() + ", y: " + super.getY());
                if (a > 0 && a < mapRange && b > 0 && b < mapRange){
                    System.out.println(b + " - " + a);
                    Unit unit = tileController.getTileGrid()[b][a].getUnit();
                    if (unit != null){
                        System.out.println("heeealll!!1 --------");
                        if (unit.getPlayer() == super.getOwner()){
                            unit.addToCurrentHP(healingPoints);
                        }
                    }
                }
            }
        }
    }
}
