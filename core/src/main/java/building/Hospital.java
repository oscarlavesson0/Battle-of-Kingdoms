package building;

import base.Player;
import terrain.TileController;

public class Hospital extends Building{

    int healingRange;

    TileController tileController;

    public Hospital(int x, int y, Player owner, int id, TileController tileController){
        super(x, y, owner, id, 3, tileController);
        healingRange = 3;
    }

    @Override
    public void ActivateBuildingAbility(TileController tileController) {
        for (int i = -healingRange; i < healingRange; i++){
            for (int j = -healingRange; j < healingRange; j++){
                // Gör heal på units av samma owner.

            }
        }
    }
}
