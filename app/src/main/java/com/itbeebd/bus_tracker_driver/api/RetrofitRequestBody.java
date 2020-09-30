package com.itbeebd.bus_tracker_driver.api;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

public class RetrofitRequestBody {
    private String api_key = "7EgGmA";

    public RetrofitRequestBody() {

    }

    Map<String, Object> signInRequirements(String email, String password){
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);
        map.put("api_key", this.api_key);
        return map;
    }

    Map<String, Object> updateLocation(double lat, double lon, int busId) {
        Map<String, Object> map = new HashMap<>();
        map.put("lat", lat);
        map.put("lon", lon);
        map.put("bus_id", busId);
        map.put("api_key", this.api_key);
        return map;
    }

    Map<String, Object> getBusCurrentPositionByBusId(int busId) {
        Map<String, Object> map = new HashMap<>();
        map.put("bus_id", busId);
        map.put("api_key", this.api_key);
        return map;
    }

    Map<String, Object> sendUserFeedbackAboutBusToServer(int bus_id, LatLng userLocation) {
        Map<String, Object> map = new HashMap<>();
        map.put("api_key", this.api_key);
        map.put("lat", userLocation.latitude);
        map.put("long", userLocation.longitude);
        map.put("bus_id", bus_id);

        return map;
    }


    String getApi_key() {
        return this.api_key;
    }
}
