package com.itbeebd.bus_tracker_driver.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class GpsService extends Service {
    private static final String TAG = "GpsService";
    private static final int LOCATION_INTERVAL = 30000; // this is in milisec. after every this interval the user location will send to server
    private static final float LOCATION_DISTANCE = 0.0f; // this is in meter.
    LocationListener[] locationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    private LocationManager locationManager = null;
    private CheckNetworkState checkNetworkState;
    private HaversineDistance haversineDistance;

    private void sendTravelingUserLocationToServer(Location mLastLocation) {
        if (mLastLocation == null) {
            Toast.makeText(getApplicationContext(), "mLastLocation is null", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkNetworkState.haveNetworkConnection()) {
            Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_SHORT).show();
            CustomSharedPref.getInstance(getApplicationContext()).setInternetConnectionHasDisabled(true);
            return;
        }

        if (CustomSharedPref.getInstance(getApplicationContext()).wasInternetConnectionDisabled() ||
                CustomSharedPref.getInstance(getApplicationContext()).wasGpsDisabled()) {
            //Sending GPS to server was Paused
            Toast.makeText(getApplicationContext(), "wasInternetConnectionDisabled/wasGpsDisabled true", Toast.LENGTH_SHORT).show();

            checkIfUserIsWithTheTrainIfNotStopService(mLastLocation);
        } else {
            Toast.makeText(getApplicationContext(), "Gps is going to the server", Toast.LENGTH_SHORT).show();
            sendGPSToServer(mLastLocation);
        }
    }

    public void checkIfUserIsWithTheTrainIfNotStopService(Location mLastLocation) {
        /*
        new ApiCalls().getTrainCurrentPositionByTrainId(false,
                getApplicationContext(),
                new Train(CustomSharedPref.getInstance(getApplicationContext()).getTravelingTrainId()),(latitude, longitude) -> {
                    haversineDistance = new HaversineDistance(
                            mLastLocation.getLatitude(),
                            mLastLocation.getLongitude());

                    double driverAndBusDistance = haversineDistance.calculate(latitude, longitude);

                    if(driverAndBusDistance > 1){
                        Toast.makeText(getApplicationContext(), "user is too far from train current location.", Toast.LENGTH_SHORT).show();
                        this.stopSelf();
                    }
                    CustomSharedPref.getInstance(getApplicationContext()).setGpsHasDisabled(false);
                    CustomSharedPref.getInstance(getApplicationContext()).setInternetConnectionHasDisabled(false);
                });

         */
    }

    public void sendGPSToServer(Location mLastLocation) {
        /*
        new ApiCalls().sendUserFeedbackAboutTrainToServer(getApplicationContext(),new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), response ->{
            //Log.i(TAG,response + " server response");
            if(response){
                Toast.makeText(getApplicationContext(), "GPS service is destroy called from server", Toast.LENGTH_SHORT).show();
                this.stopSelf();
            }
            else {
                Toast.makeText(getApplicationContext(), "train location is updated : " + mLastLocation.getLatitude() + " " + mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            }
        });

         */
    }

    @Override
    public IBinder onBind(Intent arg0) {
        checkNetworkState = new CheckNetworkState(getApplicationContext());
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        checkNetworkState = new CheckNetworkState(getApplicationContext());
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        initializeLocationManager();

        try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            //   Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            //   Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            //   Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            //   Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        // Log.i(TAG, "onDestroy");
        super.onDestroy();

        Toast.makeText(getApplicationContext(), "Gps service destroy", Toast.LENGTH_SHORT).show();

        if (locationManager != null) {
            for (int i = 0; i < locationListeners.length; i++) {
                try {
                    locationManager.removeUpdates(locationListeners[i]);
                } catch (Exception ex) {
                }
            }
        }
    }

    private void initializeLocationManager() {
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private class LocationListener implements android.location.LocationListener {
        Location userCurrentLocation;

        public LocationListener(String provider) {
            userCurrentLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            userCurrentLocation.set(location);
            sendTravelingUserLocationToServer(this.userCurrentLocation);
        }

        @Override
        public void onProviderDisabled(String provider) {
            CustomSharedPref.getInstance(getApplicationContext()).setGpsHasDisabled(true);
            Toast.makeText(getApplicationContext(), "Gps disabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }
}
