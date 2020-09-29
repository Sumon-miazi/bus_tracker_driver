package com.itbeebd.bus_tracker_driver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.itbeebd.bus_tracker_driver.utils.CustomSharedPref;

public class SignInActivity extends AppCompatActivity {

    private TextView email;
    private TextView password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.editText2);
        password = findViewById(R.id.editText);

        String savedEmail = CustomSharedPref.getInstance(this).getSavedEmail();
        if(!savedEmail.isEmpty()){
            email.setText(savedEmail);
        }

    }

    public void loginDriver(View view) {
        String emailTxt = email.getText().toString();
        String passwordTxt = password.getText().toString();

        if(emailTxt.isEmpty()){
            Toast.makeText(this, "Enter email first", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(passwordTxt.isEmpty()){
            Toast.makeText(this, "Enter password first", Toast.LENGTH_SHORT).show();
            return;
        }


    }
}