package hk.ust.comp3021;

import lombok.Data;

@Data
public class Location {
    private Double latitude;

    private Double altitude;

    public Location(String latitude, String altitude) {
        this.latitude = Double.valueOf(latitude);
        this.altitude = Double.valueOf(altitude);
    }

    public Double distanceTo(Location location) {
        return Math.sqrt(Math.pow(this.latitude - location.latitude, 2) + Math.pow(this.altitude - location.altitude, 2));
    }

    public static Double distanceBetween(Location location1, Location location2) {
        return Math.sqrt(Math.pow(location1.latitude - location2.latitude, 2) + Math.pow(location1.altitude - location2.altitude, 2));
    }

    @Override
    public String toString() {
        return "Location(" +
                "latitude=" + String.format("%.1f", latitude) +
                ", altitude=" + String.format("%.1f", altitude) +
                ')';
    }

}
