package building;

import base.TurnManager;
import terrain.TileController;

import java.util.ArrayList;
import java.util.List;

public class BuildingController {

    TileController tileController;
    List<BuildingType> buildingTypes;
    List<Building> buildings;

    public BuildingController(TileController tileController) {
        this.tileController = tileController;
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

    public Building createBuilding(BuildingType buildingType, TurnManager turnManager){

        Building building = null;

        switch (buildingType){
            case Hospital:
                building = new Hospital(1, 1, turnManager.getCurrentPlayer(), 1, tileController);
                break;
            case Barracks:

        }
        addBuilding(building);

        return building;
    }

    public void updateBuildings(){
        for (Building building : buildings){
            building.updateBuilding();
        }
    }

}
