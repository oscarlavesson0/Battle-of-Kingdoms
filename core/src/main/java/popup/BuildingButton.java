package popup;

import building.BuildingType;

/**
 * Represents a clickable button used for selecting a building type
 * inside the building menu popup.
 *
 * Stores position, size, and the associated BuildingType.
 *
 * Author:
 * JoelAxel Olsson
 */
public class BuildingButton {

    /** X position of the button. */
    private int x;

    /** Y position of the button. */
    private int y;

    /** Width of the button. */
    private int width;

    /** Height of the button. */
    private int height;

    /** The building type this button represents. */
    private BuildingType buildingType;

    /**
     * Creates a new BuildingButton with the given position, size, and type.
     *
     * @param x button X position
     * @param y button Y position
     * @param height button height
     * @param width button width
     * @param buildingType the building type represented by this button
     */
    public BuildingButton(int x, int y, int height, int width, BuildingType buildingType) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buildingType = buildingType;
    }

    /** @return the X position of the button */
    public int getX() {
        return x;
    }

    /** @return the Y position of the button */
    public int getY() {
        return y;
    }

    /** @return the width of the button */
    public int getWidth() {
        return width;
    }

    /** @return the height of the button */
    public int getHeight() {
        return height;
    }

    /** @return the building type associated with this button */
    public BuildingType getBuildingType() {
        return buildingType;
    }
}
