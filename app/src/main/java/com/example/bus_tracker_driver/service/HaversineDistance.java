package com.example.bus_tracker_driver.service;

public class HaversineDistance {

    private final String TAG = "HaversineDistance";
    private final Double latitude;
    private final Double longitude;

    public HaversineDistance(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }

    public Double calculate(Double latitude2, Double longitude2) {
        final int R = 6371; // Radious of the earth
        Double latDistance = toRad(latitude2 - latitude);
        Double lonDistance = toRad(longitude2 - longitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(latitude)) * Math.cos(toRad(latitude2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // values in kilo meters.
    }

}