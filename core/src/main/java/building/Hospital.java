package building;

import base.Player;

public class Hospital extends Building{

    int healingRange;

    public Hospital(int x, int y, Player owner, int id){
        super(x, y, owner, id);
        healingRange = 3;
    }

    @Override
    public void ActivateBuildingAbility() {
        for (int i = -healingRange; i < healingRange; i++){
            for (int j = -healingRange; j < healingRange; j++){
                // Gör heal på units av samma owner.
            }
        }
    }
}
