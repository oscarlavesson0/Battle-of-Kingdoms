package popup;

import base.BaseStats;
import base.TurnManager;
import building.BuildingType;

public interface BuildingChosenListener {
    void OnBuildingChosen(BuildingType buildingType, BaseStats baseStats);
}
