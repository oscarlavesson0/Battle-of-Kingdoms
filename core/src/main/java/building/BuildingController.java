package building;

import java.util.ArrayList;
import java.util.List;

public class BuildingController {

    List<BuildingType> buildingTypes;

    public BuildingController(){
        buildingTypes = new ArrayList<>();
        buildingTypes.add(BuildingType.Hospital);
        buildingTypes.add(BuildingType.Barracks);
        buildingTypes.add(BuildingType.Blacksmith);
    }
}
