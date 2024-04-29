package cargo.model;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;

@PlanningEntity
public class Request implements Standstill {
    private String orderId;
    private int storageId;
    private Location location;
    private double weight;
    @PlanningVariable(
        valueRangeProviderRefs = { "carProvider", "requestProvider" },
        graphType = PlanningVariableGraphType.CHAINED
    )
    private transient Standstill previous;

    public Request() {
    }

    public Request(String orderId, int storageId, double weight, Location location) {
        this.orderId = orderId;
        this.storageId = storageId;
        this.weight = weight;
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

    public int getStorageId() {
        return storageId;
    }

    public double getWeight() {
        return weight;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setStorageId(int storageId) {
        this.storageId = storageId;
    }
}
