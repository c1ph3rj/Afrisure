package com.swiftant.afrisure.create_account;


import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.swiftant.afrisure.R;
import com.swiftant.afrisure.login.email.LoginEmail;

import java.util.Objects;

public class CreateAccount extends AppCompatActivity {

    TextInputEditText nameField,mobileField,emailField,passwordField,confirmPasswordField;
    Button createButton;
    Context context;
    Activity activity;
    LinearLayout createAccountLayout,passwordChecker;
    TextView passwordStrength,charLengthTV
            ,lowerCaseTV,upperCaseTV
            ,numberTV,spCharTV,nameErr,passwordErr,emailErr,mobileErr,cnfPasswordErr;
    ImageView backCrtAct;
    TextInputLayout nameLayout,mobileLayout,emailLayout,passwordLayout,cnfPasswordLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                try{
                    startActivity(new Intent(context, LoginEmail.class));
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        try{
            context=this;
            activity=this;
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            init();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    void init() {
        try {
            nameField = findViewById(R.id.nameField);
            mobileField = findViewById(R.id.mobileField);
            emailField = findViewById(R.id.emailField);
            passwordField = findViewById(R.id.passwordField);
            confirmPasswordField = findViewById(R.id.confirmPasswordField);
            createButton = findViewById(R.id.createButton);
            createAccountLayout = findViewById(R.id.createAccountLayout);
            backCrtAct = findViewById(R.id.backCrtAct);

            passwordChecker = findViewById(R.id.passwordChecker);
            passwordStrength = findViewById(R.id.passwordStrength);
            charLengthTV = findViewById(R.id.charLengthTV);
            lowerCaseTV = findViewById(R.id.lowerCaseTV);
            upperCaseTV = findViewById(R.id.upperCaseTV);
            numberTV = findViewById(R.id.numberTV);
            spCharTV = findViewById(R.id.spCharTV);
            nameLayout = findViewById(R.id.nameLayout);
            mobileLayout = findViewById(R.id.mobileLayout);
            emailLayout = findViewById(R.id.emailLayout);
            passwordLayout = findViewById(R.id.passwordLayout);
            cnfPasswordLayout = findViewById(R.id.cnfPasswordLayout);
            cnfPasswordErr = findViewById(R.id.cnfPasswordErr);
            mobileErr = findViewById(R.id.mobileErr);
            emailErr = findViewById(R.id.emailErr);
            passwordErr = findViewById(R.id.passwordErr);
            nameErr = findViewById(R.id.nameErr);

            basicFunctions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void basicFunctions() {
        try{

            backCrtAct.setOnClickListener(l->{
                onBackPressed();
            });

            createButton.setOnClickListener(l->{
                try{
                    nameErr.setVisibility(View.GONE);
                    mobileErr.setVisibility(View.GONE);
                    emailErr.setVisibility(View.GONE);
                    passwordErr.setVisibility(View.GONE);
                    cnfPasswordErr.setVisibility(View.GONE);
                    if(Objects.requireNonNull(nameField.getText()).toString().trim().length()<3 || !nameField.getText().toString().matches(getString(R.string.name_regex))){
                        nameErr.setText("Please provide a valid name");
                        nameErr.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(Objects.requireNonNull(mobileField.getText()).toString().trim().length()!=10){
                        mobileErr.setText( "Please provide a valid mobile number");
                        mobileErr.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(Objects.requireNonNull(emailField.getText()).toString().trim().length()<4 || !emailField.getText().toString().trim().matches(getString(R.string.email_regex))){
                        emailErr.setText("Please provide a valid mail address");
                        emailErr.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(Objects.requireNonNull(passwordField.getText()).toString().trim().length()<8){
                        passwordErr.setText("Please provide a password that contains at least 7 characters");
                        passwordErr.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(!Objects.requireNonNull(confirmPasswordField.getText()).toString().trim().equals(passwordField.getText().toString().trim())){
                        cnfPasswordErr.setText("Password entered in both the field doesn't match.");
                        cnfPasswordErr.setVisibility(View.VISIBLE);
                        return;
                    }
                    Toast.makeText(context, "Account Created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context, LoginEmail.class));
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            });

            passwordField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    boolean lowerCase = s.toString().trim().matches(".*[a-z].*");
                    boolean upperCase = s.toString().trim().matches(".*[A-Z].*");
                    boolean number = s.toString().trim().matches(".*\\d.*");
                    boolean spChar = s.toString().trim().matches(".*[^a-zA-Z 0-9].*");
                    passwordChecker.setVisibility(View.VISIBLE);
                    if(lowerCase){
                        lowerCaseTV.setTextColor(getColor(R.color.teal_700));
                    }else{
                        lowerCaseTV.setTextColor(getColor(R.color.red));
                    }
                    if(upperCase){
                        upperCaseTV.setTextColor(getColor(R.color.teal_700));
                    }else{
                        upperCaseTV.setTextColor(getColor(R.color.red));
                    }
                    if(number){
                        numberTV.setTextColor(getColor(R.color.teal_700));
                    }else{
                        numberTV.setTextColor(getColor(R.color.red));
                    }
                    if(spChar){
                        spCharTV.setTextColor(getColor(R.color.teal_700));
                    }else{
                        spCharTV.setTextColor(getColor(R.color.red));
                    }
                    if(s.toString().trim().length()>6){
                        charLengthTV.setTextColor(getColor(R.color.teal_700));
                    }else if(s.toString().trim().length()<7){
                        charLengthTV.setTextColor(getColor(R.color.red));
                    }
                    if(s.toString().length()>6 && lowerCase && upperCase && number && spChar){
                        passwordStrength.setTextColor(getColor(R.color.teal_700));
                        passwordStrength.setText("Strong");
                    } else if (s.toString().length()>6 && lowerCase && upperCase) {
                        passwordStrength.setTextColor(getColor(R.color.yellow));
                        passwordStrength.setText("Medium");
                    }else{
                        passwordStrength.setTextColor(getColor(R.color.red));
                        passwordStrength.setText("Weak");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            passwordField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        passwordChecker.setVisibility(View.GONE);
                    }
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }

}