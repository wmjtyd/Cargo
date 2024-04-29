package cargo.model;

public class Location {
    private String address;
    private double latitude;
    private double longitude;

    public Location(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getDistanceTo(Location other) {
        double latDiff = this.latitude - other.latitude;
        double longDiff = this.longitude - other.longitude;
        return Math.sqrt(latDiff * latDiff + longDiff * longDiff);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
