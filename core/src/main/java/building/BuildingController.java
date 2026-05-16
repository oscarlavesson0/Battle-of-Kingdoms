package building;

import java.util.ArrayList;
import java.util.List;

public class BuildingController {

    List<BuildingType> buildingTypes;
    List<Building> buildings;

    public BuildingController(){
        buildings = new ArrayList<>();
        buildingTypes = new ArrayList<>();
        buildingTypes.add(BuildingType.Hospital);
        buildingTypes.add(BuildingType.Barracks);
        buildingTypes.add(BuildingType.Blacksmith);
    }

    public List<BuildingType> getBuildingTypes() {
        return buildingTypes;
    }

    public void addBuilding(Building building){
        buildings.add(building);
    }

    public void updateBuildings(){
        for (Building building : buildings){
            building.updateBuilding();
        }
    }

}
