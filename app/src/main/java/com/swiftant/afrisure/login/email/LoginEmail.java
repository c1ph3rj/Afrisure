package com.swiftant.afrisure.login.email;

import static com.swiftant.afrisure.common.Services.generateOkHttpClient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.swiftant.afrisure.MainActivity;
import com.swiftant.afrisure.R;
import com.swiftant.afrisure.common.DatabaseHelper;
import com.swiftant.afrisure.common.Services;
import com.swiftant.afrisure.create_account.CreateAccount;
import com.swiftant.afrisure.dashboard.Dashboard;

import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginEmail extends AppCompatActivity {


    TextInputEditText emailField,passwordField;
    TextInputLayout passwordLayout,emailLayout;
    Button loginButton;
    Context context;
    Activity activity;
    LinearLayout loginLayout,noInternetLayout;
    TextView forgotPass,createAccount,emailError,passwordError;

    Services services;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);
        try {
            context = this;
            activity = this;
            services = new Services(context, null);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void init(){
        try{
            emailField=findViewById(R.id.emailField);
            passwordField=findViewById(R.id.passwordField);
            forgotPass=findViewById(R.id.forgotPass);
            createAccount=findViewById(R.id.createAccount);
            loginButton=findViewById(R.id.loginButton);
            loginLayout=findViewById(R.id.loginLayout);
            passwordLayout=findViewById(R.id.passwordLayout);
            emailLayout=findViewById(R.id.emailLayout);
            passwordError=findViewById(R.id.passwordError);
            emailError=findViewById(R.id.emailError);

            basicFunctions();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void basicFunctions() {
        try{

            createAccount.setOnClickListener(l->{
                try{
                    //TODO REDIRECT TO CREATE ACCOUNT
                    startActivity(new Intent(context, CreateAccount.class));
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            });


            loginButton.setOnClickListener(l->{
                try{
                    emailField.setBackground(AppCompatResources.getDrawable(context,R.drawable.card_bordered_background));
                    passwordField.setBackground(AppCompatResources.getDrawable(context,R.drawable.card_bordered_background));
                    emailError.setVisibility(View.GONE);
                    passwordError.setVisibility(View.GONE);
                    if(emailField.getText().toString().length() == 0 ){
                        emailField.setBackground(AppCompatResources.getDrawable(context,R.drawable.card_theme_bordered_background));
                        emailError.setText("Please enter your email address");
                        emailError.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(!emailField.getText().toString().trim().matches(getString(R.string.email_regex))){
                        emailField.setBackground(AppCompatResources.getDrawable(context,R.drawable.card_theme_bordered_background));
                        emailError.setText("Please enter valid email address");
                        emailError.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(passwordField.getText().toString().trim().length()<7){
                        passwordField.setBackground(AppCompatResources.getDrawable(context,R.drawable.card_theme_bordered_background));
                        passwordError.setText("Please enter your password");
                        passwordError.setVisibility(View.VISIBLE);
                        return;
                    }
                    loginAPI();
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void loginAPI() {
        try {
            if (services.isNetworkConnected()) {
                if (services.checkGpsStatus()) {

                    //creating the thread to run operations in background thread.
                    Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        services.showDialog();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                            String postUrl = getString(R.string.base_url)+"api/digital/core/Account/Login";
                            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            JsonObject Details = new JsonObject();
                            try{
                                Details.addProperty("emailID", Objects.requireNonNull(emailField.getText()).toString().trim());
                                Details.addProperty("password", Objects.requireNonNull(passwordField.getText()).toString().trim());
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            String insertString = Details.toString();
                            RequestBody body = RequestBody.create(insertString,JSON);
                            Request request = new Request.Builder()
                                    .url(postUrl)
                                    //.header("clientInfo", MainActivity.InsertMobileParameters())
                                    .header("orgAppID","6130")
                                    .post(body)
                                    .build();
                            OkHttpClient client = generateOkHttpClient().build();
                            Response staticResponse = null;
                            try {
                                staticResponse = client.newCall(request).execute();
                                String staticRes = staticResponse.body().string();
                                System.out.println(staticRes);

                                if(staticResponse.code()==200) {
                                    final JSONObject staticJsonObj = new JSONObject(staticRes);

                                    if (staticJsonObj.getInt("rcode") == 200) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    services.dismissDialog();
                                                    //storing the token value for future usage.
                                                    String sToken = staticJsonObj.getJSONObject("rObj").getString("token");
                                                    String refreshToken = staticJsonObj.getJSONObject("rObj").getString("refreshToken");

                                                    DatabaseHelper mydb = services.mydb;
                                                    if (mydb.getTokenDetails().getCount() != 0) {
                                                        mydb.deleteTokenData();
                                                    }

                                                    boolean isInserted = mydb.insertToken(sToken);
                                                    if (isInserted) {
                                                        MainActivity.loginCredential=emailField.getText().toString().trim();
                                                        //TODO REDIRECT TO HOME SCREEN (DASHBOARD)
                                                        startActivity(new Intent(LoginEmail.this, Dashboard.class));
                                                        finish();
                                                    } else {
                                                        Toast.makeText(context, "Token is not stored...", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    } else if (staticJsonObj.getInt("rcode") == 502) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    services.dismissDialog();
                                                    JSONObject errorMessage=staticJsonObj.getJSONArray("rmsg").getJSONObject(0);
                                                    String errorText;
                                                    errorText=getString(R.string.cmn_something_wrong_msg);
                                                    /*try{
                                                        errorText=MainActivity.error_messages.
                                                                getJSONArray("login").
                                                                getJSONObject(0).getString(errorMessage.getString("errorCode"));
                                                    }catch (Exception e){
                                                        errorText=getString(R.string.cmn_something_wrong_msg);
                                                    }*/
                                                    services.alertTheUser("", errorText)
                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    dialogInterface.dismiss();
                                                                }
                                                            }).setCancelable(false)
                                                            .show();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    

                                                }
                                            }
                                        });
                                    }else {
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                try {
                                                    services.dismissDialog();
                                                    services.alertTheUser("", getString(R.string.cmn_something_wrong_msg))
                                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    dialogInterface.dismiss();
                                                                }
                                                            }).show();

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    

                                                }
                                            }
                                        });

                                    }
                                }
                                else{
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            try {
                                                services.dismissDialog();
                                                services.alertTheUser("",getString(R.string.cmn_something_wrong_msg))
                                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.dismiss();
                                                            }
                                                        }).show();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                

                                            }
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                services.dismissDialog();

                            }
                        }
                    });
                    t.start();


                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setMessage(getString(R.string.gps_not_enabled));
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //this will navigate user to the device location settings screen
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alert = dialog.create();
                    alert.show();
                }
            } else {
                Toast.makeText(this, getString(R.string.nonetwork), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            

        }
    }

}