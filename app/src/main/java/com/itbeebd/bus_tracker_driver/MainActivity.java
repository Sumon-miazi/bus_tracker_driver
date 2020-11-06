package com.itbeebd.bus_tracker_driver;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.itbeebd.bus_tracker_driver.service.GpsService;
import com.itbeebd.bus_tracker_driver.utils.CustomSharedPref;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Button startBtn;
    private Button stopBtn;
    private long time;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.startBusBtnId);
        stopBtn = findViewById(R.id.stopBusBtnId);

        db = FirebaseFirestore.getInstance();

        TextView driverName = findViewById(R.id.driverNameId);
        driverName.setText(CustomSharedPref.getInstance(this).getUserName());

        showStartBtn(!isMyServiceRunning(GpsService.class));

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLocationPermission();
    }

    public void startTheGpsService(View view) {
        startService(new Intent(this, GpsService.class));
        showStartBtn(false);
        setBusStartStatement();
    }

    public void stopTheGpsService(View view) {
        stopService(new Intent(this, GpsService.class));
        showStartBtn(true);
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location permission")
                        .setMessage("This app required gps location otherwise this app will not work properly. Enable location to use this app.")
                        .setPositiveButton("ok", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        // locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (time + 2000 > System.currentTimeMillis()) {
            finish();
        } else {
            time = System.currentTimeMillis();
            Toast.makeText(this, "press again to exit", Toast.LENGTH_SHORT).show();
        }

    }

    private void showStartBtn(boolean flag) {
        if (flag) {
            startBtn.setVisibility(View.VISIBLE);
            stopBtn.setVisibility(View.GONE);
        } else {
            startBtn.setVisibility(View.GONE);
            stopBtn.setVisibility(View.VISIBLE);
        }
    }

    private void setBusStartStatement() {
        //  android.text.format.DateFormat.format("EEEE", date);

        Calendar calendar = Calendar.getInstance();
        String[] days = new String[]{"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
        String day = days[calendar.get(Calendar.DAY_OF_WEEK) - 1];

        Map<String, Object> record = new HashMap<>();
        record.put("title", CustomSharedPref.getInstance(this).getBusName() + " has started");
        record.put("body", "Dear student, your bus is now on the way.");
        record.put("topic", CustomSharedPref.getInstance(this).getBusName().replaceAll(" ", "_").toLowerCase() + "_" + day.toLowerCase());

        System.out.println(">>>>>>>>>>. " + record.toString());

// Add a new document with a generated ID
        db.collection("notification")
                .add(record)
                .addOnCompleteListener(runnable -> {
                });
    }
}