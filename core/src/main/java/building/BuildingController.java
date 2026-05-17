package building;

import base.BaseStats;
import base.Player;
import terrain.TileController;

import java.util.ArrayList;
import java.util.List;

public class BuildingController {

    private TileController tileController;
    private List<BuildingType> buildingTypes;
    private List<Building> buildings;

    private BuildingRenderer buildingRenderer;

    public BuildingController(TileController tileController) {
        buildingRenderer = new BuildingRenderer(this);
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

    public List<Building> getBuildings() {
        return buildings;
    }

    public void addBuilding(Building building){
        buildings.add(building);
    }

    public Building createBuilding(BuildingType buildingType, BaseStats baseStats) {
        if (baseStats == null) return null;
        Player owner = baseStats.getOwner();
        if (owner == null) return null;
        if (owner.getGold() < buildingType.getCost()) return null;

        owner.subtractGold(buildingType.getCost());

        Building building = null;

        switch (buildingType){
            case Hospital:
                building = new Hospital(120, 100, baseStats.getOwner(), tileController);
                break;
            case Barracks:
                building = new Barracks(120, 40, baseStats.getOwner(), tileController);
                break;
            case Blacksmith:
                building = new Blacksmith(50, 120, baseStats.getOwner(), tileController);
                break;
        }
        addBuilding(building);

        return building;
    }

    public void updateBuildings(){
        for (Building building : buildings){
            building.updateBuilding();
        }
    }

    public BuildingRenderer getBuildingRenderer() {
        return buildingRenderer;
    }

}
