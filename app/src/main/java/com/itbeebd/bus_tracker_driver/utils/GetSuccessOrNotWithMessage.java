package com.itbeebd.bus_tracker_driver.utils;

public interface GetSuccessOrNotWithMessage {
    void result(boolean success, boolean failed, String message);
}
