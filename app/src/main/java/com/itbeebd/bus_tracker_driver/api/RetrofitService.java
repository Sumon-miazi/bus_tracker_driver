package com.itbeebd.bus_tracker_driver.api;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface RetrofitService {

    @POST(ApiUrls.SIGNIN)
    Call<ResponseBody> getSignIn(@Body Map<String, Object> body);

    @POST(ApiUrls.UPDATELOCATION)
    Call<ResponseBody> updateLocation(@Body Map<String, Object> body);

}
