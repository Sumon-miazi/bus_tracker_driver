package com.itbeebd.bus_tracker_driver;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.itbeebd.bus_tracker_driver.service.CheckNetworkState;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        CheckNetworkState checkNetworkState = new CheckNetworkState(SplashActivity.this);
        if (!checkNetworkState.haveNetworkConnection()) {
            showNoWifiInternet();
            TextView textView = findViewById(R.id.loadId);
            textView.setText("NO INTERNET");
            //animationView.setAnimation("wifi_animation.json");
            return;
        }

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showNoWifiInternet() {
        final Dialog dialog = new Dialog(SplashActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.no_internet);

        Button dialogButton = dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

}
