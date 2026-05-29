package building;

import base.Player;
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
        for (int i = -healingRange; i <= healingRange; i++){
            for (int j = -healingRange; j <= healingRange; j++){
                int a = super.getX() + i;
                int b = super.getY() + j;

                if (a >= 0 && a < mapRange && b >= 0 && b < mapRange){
                    Unit unit = tileController.getTileGrid()[a][b].getUnit();
                    if (unit != null){
                        if (unit.getPlayer() == super.getOwner()){
                            unit.addToCurrentHP(healingPoints);
                            System.out.println("This unit is healed: x: " + unit.getX() + " y: " + unit.getY() + " a: " + a + " b: " + b);
                        }
                    }
                }
            }
        }
    }
}
