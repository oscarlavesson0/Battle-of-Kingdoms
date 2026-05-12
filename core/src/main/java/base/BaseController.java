package base;
import unit.*;

public class BaseController {
    public final int unitCost = 100;
    private UnitController unitController;
    private UnitSpawnListener spawnListener;

    public BaseController(UnitController unitController){
        this.unitController = unitController;
    }

    public void setSpawnListener(UnitSpawnListener listener) {
        this.spawnListener = listener;
    }

    public CustomUnit createUnitFromChoice(BaseStats base, int hp, int attack, int speed, int defence){
        if(base == null) return null;
        Player owner = base.getOwner();
        if(owner == null) return null;
        //if(owner.getGold() < unitCost) return null;

        //owner.addGold(-unitCost);
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

        if (spawnListener != null) {
            spawnListener.onUnitSpawned(unit);
        }

        return unit;
    }
}
