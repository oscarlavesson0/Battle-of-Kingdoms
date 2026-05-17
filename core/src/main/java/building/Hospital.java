package building;

import base.Player;
import terrain.Tile;
import terrain.TileController;
import unit.Unit;

public class Hospital extends Building{

    int healingRange;
    int healingPoints;
    int mapRange;

    Tile[][] tileGrid;

    public Hospital(int x, int y, Player owner, int id, TileController tileController){
        super(x, y, owner, "Hospital", 3, tileController);
        healingRange = 3;
        healingPoints = 10;
        tileGrid = tileController.getTileGrid();
        mapRange = tileGrid.length;
    }

    /**
     * Hospital ger alla units av samma ägare som hospital
     * extra HP runtom hospitalbyggnaden varje turn.
     * @param tileGrid
     * @author Joel
     */
    @Override
    public void ActivateBuildingAbility(Tile[][] tileGrid) {
        for (int i = -healingRange; i < healingRange; i++){
            for (int j = -healingRange; j < healingRange; j++){
                int a = super.getX() + i;
                int b = super.getY() + j;
                if (a > 0 && a < mapRange && b > 0 && b < mapRange){
                    Unit unit = tileGrid[a][b].getUnit();
                    if (unit != null){
                        if (unit.getPlayer() == super.getOwner()){
                            unit.addToCurrentHP(healingPoints);
                        }
                    }
                }
            }
        }
    }
}
