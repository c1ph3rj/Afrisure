package com.swiftant.afrisure.login.phone_number;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.swiftant.afrisure.MainActivity;
import com.swiftant.afrisure.R;
import com.swiftant.afrisure.common.Services;
import com.swiftant.afrisure.create_account.CreateAccount;

public class LoginPhoneNumber extends AppCompatActivity {

    EditText mobileField;
    Button sendOtpButton;
    LinearLayout loginLayout;
    TextView createAccount;
    Services services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone_number);

        services = new Services(this, null);
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void init() {
        try {
            mobileField = findViewById(R.id.mobileField);
            sendOtpButton = findViewById(R.id.sendOtpButton);
            loginLayout = findViewById(R.id.loginLayout);
            createAccount = findViewById(R.id.createAccount);
            basicFunctions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void basicFunctions() {
        try {
            createAccount.setOnClickListener(l -> {
                try {
                    //TODO REDIRECT CREATE ACCOUNT
                    startActivity(new Intent(this, CreateAccount.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            sendOtpButton.setOnClickListener(l -> {
                try {
                    if (mobileField.getText().toString().trim().length() == 10) {
                        //do the needful activity
                        MainActivity.loginCredential = mobileField.getText().toString().trim();
                        //calling the verify activity for instance
                        //TODO REDIRECT TO VERIFY OTP
//                        startActivity(new Intent(context, VerifyOTP.class));
                    } else {
                        Toast.makeText(this, "please enter the valid mobile number before proceeding", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    void sendOtpApi() {
//        try {
//            if (services.isNetworkConnected()) {
//                if (services.checkGpsStatus()) {
//
//                } else {
//                    services.redirectToGpsSettings();
//                }
//            } else {
//                services.checkNetworkVisibility();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}