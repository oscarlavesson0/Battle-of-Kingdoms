package popup;

import base.TurnManager;
import building.BuildingType;

public interface BuildingChosenListener {
    void OnBuildingChosen(BuildingType buildingType, TurnManager turnManager);
}
