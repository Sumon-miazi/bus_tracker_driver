package com.itbeebd.bus_tracker_driver.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.itbeebd.bus_tracker_driver.R;

public class CustomSharedPref {
    private static CustomSharedPref customSharedPref;
    private Context context;
    private SharedPreferences sharedPreferences;

    private CustomSharedPref(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(this.context.getResources().getString(R.string.sharedPrefName), Context.MODE_PRIVATE);
    }  //private constructor.

    public static CustomSharedPref getInstance(Context context) {
        if (customSharedPref == null) {
            customSharedPref = new CustomSharedPref(context);
        }
        return customSharedPref;
    }

    public void setInternetConnectionHasDisabled(boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("InternetConnectionHasDisabled", value);
        editor.apply();
    }

    public boolean wasInternetConnectionDisabled() {
        return sharedPreferences.getBoolean("InternetConnectionHasDisabled", false);
    }

    public void setGpsHasDisabled(boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("GpsHasDisabled", value);
        editor.apply();
    }

    public boolean wasGpsDisabled() {
        return sharedPreferences.getBoolean("GpsHasDisabled", false);
    }

    public void setEmail(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("savedEmail", value);
        editor.apply();
    }

    public String getSavedEmail() {
        return sharedPreferences.getString("savedEmail", "");
    }

    public void setUserName(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("driverName", value);
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString("driverName", "");
    }

    public void setBusId(int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("BusId", value);
        editor.apply();
    }

    public int getBusId() {
        return sharedPreferences.getInt("BusId", 0);
    }
}
