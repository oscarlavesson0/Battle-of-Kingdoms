package building;

import base.Player;
import terrain.TileController;

public abstract class RangeBuilding extends Building{

    int range;
    int affectPoints;
    int mapRange;

    public RangeBuilding(int x, int y, Player owner, String name, int constructionTime, TileController tileController) {
        super(x, y, owner, name, constructionTime, tileController);
    }

    @Override
    public abstract void ActivateBuildingAbility();
}
