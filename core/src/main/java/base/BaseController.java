package base;

import terrain.Tile;
import unit.CustomUnit;
import unit.UnitController;
import unit.UnitSkapare;
import unit.Weapon;

public class BaseController {

    private UnitController unitController;
    private UnitSpawnListener spawnListener;


    public BaseController(UnitController unitController){
        this.unitController = unitController;
    }

    public void setSpawnListener(UnitSpawnListener listener) {
        this.spawnListener = listener;
    }

    public CustomUnit createUnit(BaseStats base) {
        CustomUnit unit = new UnitSkapare()
            .maxHP(10)
            .attack(2)
            .speed(2)
            .defence(1)
            .weapon(Weapon.SWORD)
            .player(base.getOwner())
            .startPosition(base.getPosition().getX(),
                base.getPosition().getY())
            .build();

        unitController.spawnUnitNearBase(base, unit);

        if (spawnListener != null) {
            spawnListener.onUnitSpawned(unit);
        }
        return unit;
    }
}

