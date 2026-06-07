package building;

import base.BaseStats;
import base.Player;
import terrain.Tile;
import terrain.TileController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Controls all buildings in the game. Handles creation, storage,
 * updating and rendering of buildings. Also manages building types
 * and determines valid spawn positions around a base.
 *
 * Author:
 * JoelAxel Olsson
 */
public class BuildingController {

    /** Controller used to access the tile grid. */
    private TileController tileController;

    /** List of all available building types. */
    private List<BuildingType> buildingTypes;

    /** List of all buildings currently placed on the map. */
    private List<Building> buildings;

    /** Reference to the tile grid. */
    private Tile[][] tileGrid;

    /** Renderer responsible for drawing buildings. */
    private BuildingRenderer buildingRenderer;

    /** Maximum distance from a base where buildings may spawn. */
    private int buildSpawnRange;

    /** Random generator used for spawn positions. */
    private Random random;

    /**
     * Creates a BuildingController using the given TileController.
     * Initializes building types and prepares the renderer.
     *
     * @param tileController controller used to access the tile grid
     */
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

    /** @return list of all available building types */
    public List<BuildingType> getBuildingTypes() {
        return buildingTypes;
    }

    /** @return list of all buildings currently placed */
    public List<Building> getBuildings() {
        return buildings;
    }

    /**
     * Adds a building to the internal list.
     *
     * @param building the building to add
     */
    public void addBuilding(Building building) {
        buildings.add(building);
    }

    /**
     * Attempts to create a new building of the given type near the given base.
     * Ensures the owner has enough gold and finds a valid tile within spawn range.
     *
     * @param buildingType the type of building to create
     * @param baseStats the base constructing the building
     * @return the created building, or null if creation failed
     */
    public Building createBuilding(BuildingType buildingType, BaseStats baseStats) {
        if (baseStats == null) return null;

        Player owner = baseStats.getOwner();
        if (owner == null) return null;

        if (owner.getGold() < buildingType.getCost()) return null;
        owner.subtractGold(buildingType.getCost());

        int baseX = baseStats.getPosition().getX();
        int baseY = baseStats.getPosition().getY();

        int buildingX = 0;
        int buildingY = 0;

        boolean canSpawn = false;

        while (!canSpawn) {
            buildingX = random.nextInt(-buildSpawnRange, buildSpawnRange) + baseX;
            buildingY = random.nextInt(-buildSpawnRange, buildSpawnRange) + baseY;

            if (buildingX > 0 && buildingX < tileGrid.length &&
                buildingY > 0 && buildingY < tileGrid[0].length) {

                if (tileGrid[buildingY][buildingX].getBase() == null &&
                    tileGrid[buildingY][buildingX].getBuilding() == null) {

                    canSpawn = true;
                }
            }
        }

        Building building = switch (buildingType) {
            case Hospital -> new Hospital(buildingX, buildingY, owner, buildingType, tileController);
            case Tower    -> new Tower(buildingX, buildingY, owner, buildingType, tileController);
        };

        addBuilding(building);
        tileGrid[buildingY][buildingX].setBuilding(building);

        return building;
    }

    /**
     * Updates all buildings by calling their update logic.
     * Removes or deactivates buildings that reach zero health.
     */
    public void updateBuildings() {
        for (Building building : buildings) {
            building.updateBuilding();
        }
    }

    /** @return the renderer responsible for drawing buildings */
    public BuildingRenderer getBuildingRenderer() {
        return buildingRenderer;
    }
}
