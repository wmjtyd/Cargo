package path.schedule;

import path.model.Request;
import path.model.Standstill;
import org.optaplanner.core.impl.heuristic.selector.common.nearby.NearbyDistanceMeter;

public class Nearby implements NearbyDistanceMeter<Request, Standstill> {
    @Override
    public double getNearbyDistance(Request origin, Standstill destination) {
        return origin.getLocation().getDistanceTo(
            destination.getLocation()
        );
    }
}
