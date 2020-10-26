package com.itbeebd.bus_tracker_driver.api;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.itbeebd.bus_tracker_driver.utils.BooleanResponse;
import com.itbeebd.bus_tracker_driver.utils.CustomLocation;
import com.itbeebd.bus_tracker_driver.utils.CustomSharedPref;
import com.itbeebd.bus_tracker_driver.utils.GetResponse;

import org.json.JSONObject;

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

    public void signIn(final Context context, String email, String password, GetResponse getResponse) {

        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Signing in...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        System.out.println("signIn>>>>>>>>>>> called ");
        final RetrofitRequestBody retrofitRequestBody = new RetrofitRequestBody();
        Call<ResponseBody> responseBodyCall = service.getSignIn(retrofitRequestBody.signInRequirements(email, password));
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject = null;
                if (response.isSuccessful()) {
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        System.out.println("signIn>>>>>>>>>>> " + jsonObject.toString());
                        if(jsonObject.optString("status").equals("true")){

                            JSONObject userData = jsonObject.getJSONObject("data");

                            CustomSharedPref.getInstance(context).setUserName(userData.getString("name"));
                            CustomSharedPref.getInstance(context).setBusId(userData.getInt("bus_id"));
                            CustomSharedPref.getInstance(context).setBusName(userData.getString("bus_name"));

                            getResponse.data(true, jsonObject.optString("message"));
                        } else getResponse.data(false, jsonObject.optString("message"));

                    } catch (Exception ignore) {
                        System.out.println("signIn>>>>>>>>>>> catch " + ignore.getMessage());
                        getResponse.data(false, ignore.getMessage());
                    }
                } else {
                    System.out.println("signIn>>>>>>>>>>> response failed");
                    getResponse.data(false, response.message());
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("signIn>>>>>>>>>>> failed " + t.getMessage());
                getResponse.data(false, t.getMessage());
                mProgressDialog.dismiss();
            }
        });
    }

    public void getBusCurrentPositionByBusId(int bus_id, CustomLocation customLocation) {

        Call<ResponseBody> data = service.getBusCurrentPositionByBusId(new RetrofitRequestBody().getBusCurrentPositionByBusId(bus_id));
        data.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject = null;
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            jsonObject = new JSONObject(response.body().string());
                            System.out.println("getBusCurrentPositionByBusId>>>>>>>>>>> " + jsonObject.toString());
                            if (jsonObject.optString("status").equals("true")) {
                                JSONObject busLocation = jsonObject.getJSONObject("data");
                                customLocation.customLocation(busLocation.getDouble("lat"),
                                        busLocation.getDouble("long"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void sendUserFeedbackAboutBusToServer(int bus_id, LatLng latLng, BooleanResponse booleanResponse) {
        Call<ResponseBody> data = service.sendUserFeedbackAboutBusToServer(new RetrofitRequestBody().sendUserFeedbackAboutBusToServer(bus_id, latLng));
        data.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            // Toast.makeText(context, "gps service running", Toast.LENGTH_SHORT).show();
                            String jsonresponse = response.body().string();
                            JSONObject jsonObject = new JSONObject(jsonresponse);
                            if (jsonObject.optString("status").equals("true")) {
                                booleanResponse.getBoolean(true);
                            } else {
                                booleanResponse.getBoolean(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

}
