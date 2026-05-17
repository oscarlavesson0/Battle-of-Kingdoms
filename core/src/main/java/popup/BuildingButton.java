package popup;

import building.BuildingType;

public class BuildingButton {

    int x;
    int y;
    int width;
    int height;
    BuildingType buildingType;

    public BuildingButton(int x, int y, int height, int width, BuildingType buildingType) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buildingType = buildingType;
    }
}
