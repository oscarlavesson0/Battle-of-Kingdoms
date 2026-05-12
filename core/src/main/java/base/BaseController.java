package base;

import unit.*;

public class BaseController {

    public final int unitCost = 100;
    private UnitController unitController;

    public BaseController(UnitController unitController){
        this.unitController = unitController;
    }

    /*public CustomUnit createUnit(BaseStats base){

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
    }*/
    public CustomUnit createUnitFromChoice(BaseStats base){
        if(base == null){
            return null;
        }

        Player owner = base.getOwner();
        if(owner == null){
            return null;
        }

        if(owner.getGold() < unitCost){
            return null;
        }

        owner.addGold(-unitCost);

        CustomUnit unit = new UnitSkapare()
            .maxHP(10)
            .attack(2)
            .speed(2)
            .defence(2)
            .weapon(Weapon.SWORD)
            .player(owner)
            .startPosition(base.getPosition().getX(), base.getPosition().getY())
            .build();

        unitController.spawnUnitNearBase(base, unit);
        return unit;
    }
}

