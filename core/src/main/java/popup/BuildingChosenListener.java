package popup;

import base.BaseStats;
import building.BuildingType;

public interface BuildingChosenListener {
    void OnBuildingChosen(BuildingType buildingType, BaseStats baseStats);
}
