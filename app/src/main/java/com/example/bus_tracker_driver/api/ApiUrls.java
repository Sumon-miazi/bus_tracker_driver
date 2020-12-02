package com.example.bus_tracker_driver.api;

public class ApiUrls {

    // public static final String IP_ADDRESS = "http://127.0.0.1";
    public static final String IP_ADDRESS = "http://192.168.43.77";
    // public static final String IP_ADDRESS = "http://localhost";

    public static final String BASE_URL = IP_ADDRESS + "/bus_management_admin/public/api/";

    public static final String SIGN_IN = "driverSignIn";
    public static final String BUS_LOCATION = "getBusCurrentPositionByBusId";
    public static final String USER_FEEDBACK_ABOUT_BUS = "userFeedbackAboutBusToServer";

}
