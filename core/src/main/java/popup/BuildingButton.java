package popup;

import building.BuildingType;

public class BuildingButton {

    private int x;
    private int y;
    private int width;
    private int height;
    private BuildingType buildingType;

    public BuildingButton(int x, int y, int height, int width, BuildingType buildingType) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buildingType = buildingType;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public BuildingType getBuildingType() {
        return buildingType;
    }
}
