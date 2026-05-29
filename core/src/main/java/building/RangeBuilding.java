package building;

import base.Player;
import terrain.TileController;
import unit.Unit;

public abstract class RangeBuilding extends Building{

    int range;
    int affectPoints;
    int mapRange;
    TileController tileController;

    public RangeBuilding(int x, int y, Player owner, String name, int constructionTime, TileController tileController, int range, int affectPoints) {
        super(x, y, owner, name, constructionTime, tileController);
        this.tileController = tileController;
        this.range = range;
        this.affectPoints = affectPoints;
        mapRange = tileController.getTileGrid().length;
    }

    public abstract void affectUnit(Unit unit);

    @Override
    public void activateBuildingAbility(){
        for (int i = -range; i <= range; i++){
            for(int j = -range; j <= range; j++){
                int a = super.getX() + i;
                int b = super.getY() + j;
                if (a >= 0 && a < mapRange && b >= 0 && b < mapRange){
                    Unit unit = tileController.getTileGrid()[a][b].getUnit();
                    if (unit != null){
                        affectUnit(unit);
                        System.out.println("This unit is affected by: " + getName() + ", x: " + unit.getX() + " y: " + unit.getY() + " a: " + a + " b: " + b);
                    }
                }
            }
        }
    };
}
