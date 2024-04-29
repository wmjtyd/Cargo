package path.model;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;

@PlanningEntity
public class Request implements Standstill {
    private String orderId;
    private Location location;
    @PlanningVariable(
        valueRangeProviderRefs = { "storageProvider", "requestProvider" },
        graphType = PlanningVariableGraphType.CHAINED
    )
    private transient Standstill previous;

    public Request() {
    }

    public Request(String orderId, Location location) {
        this.orderId = orderId;
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public Standstill getPrevious() {
        return previous;
    }

    public void setPrevious(Standstill previous) {
        this.previous = previous;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
