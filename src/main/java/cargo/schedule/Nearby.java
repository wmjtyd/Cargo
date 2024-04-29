package cargo.schedule;

import org.optaplanner.core.impl.heuristic.selector.common.nearby.NearbyDistanceMeter;
import cargo.model.Request;
import cargo.model.Standstill;

public class Nearby implements NearbyDistanceMeter<Request, Standstill> {
    @Override
    public double getNearbyDistance(Request origin, Standstill destination) {
        return origin.getLocation().getDistanceTo(
            destination.getLocation()
        );
    }
}