package path.model;

public class Storage implements Standstill {
    private Location location;

    public Storage() {
    }

    public Storage(Location location) {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
