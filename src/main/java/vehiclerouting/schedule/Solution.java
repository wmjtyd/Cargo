package vehiclerouting.schedule;

import java.util.List;

/**
 * Represents the entire routing solution containing all plans for all vehicles.
 */
public class Solution {
    private List<Plan> plans;

    public Solution() {
    }

    public List<Plan> getPlans() {
        return plans;
    }

    public void setPlans(List<Plan> plans) {
        this.plans = plans;
    }
}

/**
 * Encapsulates a plan for a single vehicle, including the vehicle itself and the ordered list of customers
 * that the vehicle is supposed to visit.
 */
class Plan {
    private Car car;
    private List<Path> path;

    public Plan(Car car, List<Path> path) {
        this.car = car;
        this.path = path;
    }

    public Car getCar() {
        return car;
    }

    public List<Path> getPath() {
        return path;
    }
}

/**
 * Defines a vehicle with an ID, capacity, associated depot, and a list of customers it is tasked to serve.
 */
class Car {
    private String carId;

    protected int capacity;
    private Location location;

    public Car(String id, int capacity, Location location) {
        this.carId = id;
        this.capacity = capacity;
        this.location = location;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}

/**
 * Represents a customer with a unique ID, a location, and the demand (load) that needs to be fulfilled.
 */
class Path {
    private String orderId;
    private Location location;
    private int demand;

    public Path(String id, Location location, int demand) {
        this.orderId = id;
        this.location = location;
        this.demand = demand;
    }

    public String getOrderId() {
        return orderId;
    }

    public Location getLocation() {
        return location;
    }

    public int getDemand() {
        return demand;
    }
}

/**
 * A simple location class holding latitude and longitude for depots and customers.
 */
class Location {
    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

/**
 * Depot class to represent the starting point of a vehicle. Each depot has a unique ID and a location.
 */
class Depot {
    private String id;
    private Location location;

    public Depot(String id, Location location) {
        this.id = id;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }
}
