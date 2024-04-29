package cargo.model;

public class Storage implements Standstill {
    private int storageId;
    private Location location;

    public Storage() {
    }

    public Storage(int storageId, Location location) {
        this.storageId = storageId;
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getStorageId() {
        return storageId;
    }

    public void setStorageId(int storageId) {
        this.storageId = storageId;
    }
}
