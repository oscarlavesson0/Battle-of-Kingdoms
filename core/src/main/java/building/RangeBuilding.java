package building;

import base.Player;
import terrain.TileController;
import unit.Unit;

public abstract class RangeBuilding extends Building{

    int range;
    int affectPoints;
    int mapRange;
    TileController tileController;

    public RangeBuilding(int x, int y, Player owner, BuildingType buildingType, TileController tileController) {
        super(x, y, owner, buildingType);
        this.tileController = tileController;
        this.range = buildingType.getRange();
        this.affectPoints = buildingType.getAffectPoints();
        mapRange = tileController.getTileGrid().length;
    }

    public abstract void affectUnit(Unit unit);

    @Override
    public void activateBuildingAbility(){
        for (int i = -range; i <= range; i++){
            for(int j = -range; j <= range; j++){
                int x = super.getX() + i;
                int y = super.getY() + j;
                if (x >= 0 && x < mapRange && y >= 0 && y < mapRange){
                    Unit unit = tileController.getTileGrid()[y][x].getUnit();
                    if (unit != null){
                        affectUnit(unit);
                    }
                }
            }
        }
    };
}
