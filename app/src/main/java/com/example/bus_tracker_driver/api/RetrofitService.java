package com.example.bus_tracker_driver.api;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface RetrofitService {

    @POST(ApiUrls.SIGN_IN)
    Call<ResponseBody> getSignIn(@Body Map<String, Object> body);

    @POST(ApiUrls.BUS_LOCATION)
    Call<ResponseBody> getBusCurrentPositionByBusId(@Body Map<String, Object> body);

    @POST(ApiUrls.USER_FEEDBACK_ABOUT_BUS)
    Call<ResponseBody> sendUserFeedbackAboutBusToServer(@Body Map<String, Object> body);


}
