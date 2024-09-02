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

}
