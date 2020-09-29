package com.itbeebd.bus_tracker_driver.api;

import android.content.Context;

import com.itbeebd.bus_tracker_driver.utils.CustomSharedPref;
import com.itbeebd.bus_tracker_driver.utils.GetResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCalls {

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ApiUrls.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private RetrofitService service = retrofit.create(RetrofitService.class);

    public void signIn(final Context context, String email , String password, final GetResponse getResponse){
        System.out.println("signIn>>>>>>>>>>> called ");
        final RetrofitRequestBody retrofitRequestBody = new RetrofitRequestBody();
        Call<ResponseBody> responseBodyCall = service.getSignIn(retrofitRequestBody.signInRequirements(email, password));
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject = null;
                if(response.isSuccessful()){
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        System.out.println("signIn>>>>>>>>>>> " + jsonObject.toString());
                        if(jsonObject.optString("status").equals("true")){

                            JSONObject userData = jsonObject.getJSONObject("data");

                            CustomSharedPref.getInstance(context).setUserName(userData.getString("name"));
                            CustomSharedPref.getInstance(context).setBusId(userData.getInt("bus_id"));

                            getResponse.data(true, jsonObject.optString("message"));
                        }
                        else getResponse.data(false,jsonObject.optString("message"));

                    } catch (Exception ignore) {
                       // System.out.println("addUserEnrollment>>>>>>>>>>> catch " + ignore.getMessage());
                        getResponse.data(false, ignore.getMessage());
                    }
                }
                else getResponse.data(false, response.message());
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("signIn>>>>>>>>>>> failed " + t.getMessage());
                getResponse.data(false, t.getMessage());
            }
        });
    }

}
