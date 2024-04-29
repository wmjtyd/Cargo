package cargo.model;

public class Car implements Standstill {
    private int carId;
    private double capacity;
    private Location location;

    public Car() {
    }

    public Car(int id, int capacity, Location start) {
        this.carId = id;
        this.capacity = capacity;
        this.location = start;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }
}
