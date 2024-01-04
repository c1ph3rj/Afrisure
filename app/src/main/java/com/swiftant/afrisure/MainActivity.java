package com.swiftant.afrisure;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;

import com.swiftant.afrisure.common.Services;
import com.swiftant.afrisure.dashboard.Dashboard;
import com.swiftant.afrisure.landing.LandingPage;
import com.swiftant.afrisure.login.email.LoginEmail;
import com.swiftant.afrisure.login.phone_number.LoginPhoneNumber;

public class MainActivity extends AppCompatActivity {
    public static String loginCredential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Services services = new Services(this, null);
        if(services.mydb.getTokenDetails().getCount() != 0) {
            startActivity(new Intent(this, Dashboard.class));
        } else {
            startActivity(new Intent(this, LandingPage.class));
        }

    }
}