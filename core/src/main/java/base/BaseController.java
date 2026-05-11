package base;

import terrain.Tile;
import unit.CustomUnit;
import unit.UnitController;
import unit.UnitSkapare;
import unit.Weapon;

public class BaseController {

    private UnitController unitController;

    public BaseController(UnitController unitController){
        this.unitController = unitController;
    }

    public CustomUnit createUnit(BaseStats base){

        //Create a unit
        CustomUnit unit = new UnitSkapare()
            .maxHP(10)
            .attack(2)
            .speed(2)
            .defence(1)
            .weapon(Weapon.SWORD)
            .player(base.getOwner())
            .startPosition(base.getPosition().getX(), base.getPosition().getY())
            .build();
        unitController.spawnUnitNearBase(base, unit);

        return unit;
    }
}
