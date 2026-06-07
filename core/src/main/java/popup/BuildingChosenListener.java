package popup;

import base.BaseStats;
import building.BuildingType;

/**
 * Listener interface used by the building menu popup.
 * Called when the player selects a building type to construct.
 *
 * Author:
 * JoelAxel Olsson
 */
public interface BuildingChosenListener {

    /**
     * Triggered when the player chooses a building to construct.
     *
     * @param buildingType the selected building type
     * @param baseStats the base that will construct the building
     */
    void OnBuildingChosen(BuildingType buildingType, BaseStats baseStats);
}
