package building;

import base.BaseStats;
import base.Player;
import terrain.Tile;
import terrain.TileController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BuildingController {

    private TileController tileController;
    private List<BuildingType> buildingTypes;
    private List<Building> buildings;

    private Tile[][] tileGrid;

    private BuildingRenderer buildingRenderer;

    private int buildSpawnRange;
    private Random random;

    public BuildingController(TileController tileController) {
        buildingRenderer = new BuildingRenderer(this);
        this.tileController = tileController;
        tileGrid = tileController.getTileGrid();
        buildings = new ArrayList<>();
        buildingTypes = new ArrayList<>();
        buildingTypes.add(BuildingType.Hospital);
        buildingTypes.add(BuildingType.Tower);

        buildSpawnRange = 5;
        random = new Random();
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

        int baseX = baseStats.getPosition().getX();
        int baseY = baseStats.getPosition().getY();

        System.out.println(baseX + " buildingcontrollers: " + baseY);
        int buildingY = 0;
        int buildingX = 0;

        boolean canSpawn = false;
        while (!canSpawn) {
            buildingX = random.nextInt(-buildSpawnRange, buildSpawnRange) + baseX;
            buildingY = random.nextInt(-buildSpawnRange, buildSpawnRange) + baseY;

            if (buildingX > 0 && buildingX < tileGrid.length && buildingY > 0 && buildingY < tileGrid[0].length) {
                if (tileGrid[buildingY][buildingX].getBase() == null) {
                    if (tileGrid[buildingY][buildingX].getBuilding() == null) {
                        canSpawn = true;
                    }
                }
            }
        }

        Building building = null;

        switch (buildingType){
            case Hospital:
                building = new Hospital(buildingX, buildingY, owner, buildingType, tileController);
                break;
            case Tower:
                building = new Tower(buildingX, buildingY, owner, buildingType, tileController);
                break;
        }
        addBuilding(building);
        tileGrid[buildingY][buildingX].setBuilding(building);
        System.out.println("bX: " +  buildingX + " bY: " + buildingY);
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
