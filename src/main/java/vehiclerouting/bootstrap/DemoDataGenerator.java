package vehiclerouting.bootstrap;

import vehiclerouting.domain.Location;
import vehiclerouting.domain.VehicleRoutingSolution;

public class DemoDataGenerator {

    public DemoDataGenerator() {}

    public VehicleRoutingSolution generateDemoData() {
        VehicleRoutingSolution problem = DemoDataBuilder.builder()
                .setMinDemand(1)
                .setMaxDemand(2)
                .setVehicleCapacity(25)
                .setCustomerCount(77)
                .setVehicleCount(6)
                .setDepotCount(2)
                .setSouthWestCorner(new Location(0L, 43.751466, 11.177210))
                .setNorthEastCorner(new Location(0L, 43.809291, 11.290195))
                .build();
        return problem;
    }
}
