package com.swiftant.afrisure.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.swiftant.afrisure.MainActivity;
import com.swiftant.afrisure.R;
import com.swiftant.afrisure.common.Services;
import com.swiftant.afrisure.login.phone_number.LoginPhoneNumber;

public class VerifyOTP extends AppCompatActivity {

    TextView txtOtpTimer, txtResendOtp, mobileNumberTextField, edit_mail;
    Button btnVerifyOtp;
    Context context;
    EditText otpEditText1, otpEditText2, otpEditText3, otpEditText4, otpEditText5, otpEditText6;
    LinearLayout OTPSeconds, resendLayout, verifyOTPLayout, noInternetLayout;
    ImageView backLoginOTP;
    Services services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        services = new Services(this, null);
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    try {
                        if (services.isNetworkConnected()) {
                            if (MainActivity.loginCredential.contains("@")) {
                                //TODO REDIRECT TO LOGIN EMAIL FLOW
//                    startActivity(new Intent(context, LoginEmail.class));
                                finish();
                            } else {
                                startActivity(new Intent(context, LoginPhoneNumber.class));
                                finish();
                            }
                        } else {
                            finishAffinity();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void init() {
        try {
            mobileNumberTextField = findViewById(R.id.mobileNumberTextField);
            btnVerifyOtp = findViewById(R.id.BtnVerifyotp);
            txtOtpTimer = findViewById(R.id.txtotptimer);
            OTPSeconds = findViewById(R.id.OTPseconds);
            resendLayout = findViewById(R.id.resendotlay);
            backLoginOTP = findViewById(R.id.backLoginOTP);
            edit_mail = findViewById(R.id.edit_mail);
            verifyOTPLayout = findViewById(R.id.verifyOTPLayout);
            noInternetLayout = findViewById(R.id.noInternetLayout);
            txtResendOtp = findViewById(R.id.txtresendotp);
            otpEditText1 = findViewById(R.id.otp_edit_text_1);
            otpEditText2 = findViewById(R.id.otp_edit_text_2);
            otpEditText3 = findViewById(R.id.otp_edit_text_3);
            otpEditText4 = findViewById(R.id.otp_edit_text_4);
            otpEditText5 = findViewById(R.id.otp_edit_text_5);
            otpEditText6 = findViewById(R.id.otp_edit_text_6);

            basicFunctions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void basicFunctions() {
        try {
            backLoginOTP.setOnClickListener(l -> getOnBackPressedDispatcher().onBackPressed());
            edit_mail.setOnClickListener(l -> getOnBackPressedDispatcher().onBackPressed());
            String textFieldData = "";
            if (MainActivity.loginCredential.contains("@")) {
                textFieldData = MainActivity.loginCredential;
            } else {
                textFieldData = getString(R.string.code) + MainActivity.loginCredential;
            }
            mobileNumberTextField.setText(textFieldData);


            //GenericTextWatcher here works only for moving to next EditText when a number is entered
            //first parameter is the current EditText and second parameter is next EditText
            otpEditText1.addTextChangedListener(new GenericTextWatcher(otpEditText1, otpEditText2));
            otpEditText2.addTextChangedListener(new GenericTextWatcher(otpEditText2, otpEditText3));
            otpEditText3.addTextChangedListener(new GenericTextWatcher(otpEditText3, otpEditText4));
            otpEditText4.addTextChangedListener(new GenericTextWatcher(otpEditText4, otpEditText5));
            otpEditText5.addTextChangedListener(new GenericTextWatcher(otpEditText5, otpEditText6));
            otpEditText6.addTextChangedListener(new GenericTextWatcher(otpEditText6, null));

            //GenericKeyEvent here works for deleting the element and to switch back to previous EditText
            //first parameter is the current EditText and second parameter is previous EditText
            otpEditText1.setOnKeyListener(new GenericKeyEvent(otpEditText1, null));
            otpEditText2.setOnKeyListener(new GenericKeyEvent(otpEditText2, otpEditText1));
            otpEditText3.setOnKeyListener(new GenericKeyEvent(otpEditText3, otpEditText2));
            otpEditText4.setOnKeyListener(new GenericKeyEvent(otpEditText4, otpEditText3));
            otpEditText5.setOnKeyListener(new GenericKeyEvent(otpEditText5, otpEditText4));
            otpEditText6.setOnKeyListener(new GenericKeyEvent(otpEditText6, otpEditText5));


            getTimerText();

            txtResendOtp.setOnClickListener(l -> {
                try {
                    resendLayout.setVisibility(View.GONE);
                    OTPSeconds.setVisibility(View.VISIBLE);
                    getTimerText();
                    resendOtp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            btnVerifyOtp.setOnClickListener(l -> {
                try {
                    if (otpEditText1.length() == 1 && otpEditText2.length() == 1 && otpEditText3.length() == 1 && otpEditText4.length() == 1 && otpEditText5.length() == 1 && otpEditText6.length() == 1) {
                        String enteredOTP = otpEditText1.getText().toString().trim() + otpEditText2.getText().toString().trim() +
                                otpEditText3.getText().toString().trim() + otpEditText4.getText().toString().trim() +
                                otpEditText5.getText().toString().trim() + otpEditText6.getText().toString().trim();
                        //do the needful action
                        Toast.makeText(context, "entered otp is: " + enteredOTP, Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(context, ChatScreen.class));
                    } else {
                        Toast.makeText(context, "Please enter the OTP before proceeding", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class GenericKeyEvent implements View.OnKeyListener {
        private final EditText currentView;
        private final EditText previousView;

        public GenericKeyEvent(EditText currentView, EditText previousView) {
            this.currentView = currentView;
            this.previousView = previousView;
        }

        @Override
        public boolean onKey(View view, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL
                    && currentView.getId() != R.id.otp_edit_text_1 && currentView.getText().toString().isEmpty()) {
                // If current is empty then previous EditText's number will also be deleted
                previousView.setText(null);
                previousView.requestFocus();
                return true;
            }
            return false;
        }
    }

    public static class GenericTextWatcher implements TextWatcher {
        private final View currentView;
        private final View nextView;

        public GenericTextWatcher(View currentView, View nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            int currentViewId = currentView.getId();
            if (currentViewId == R.id.otp_edit_text_1 ||
                    currentViewId == R.id.otp_edit_text_2 ||
                    currentViewId == R.id.otp_edit_text_3 ||
                    currentViewId == R.id.otp_edit_text_4 ||
                    currentViewId == R.id.otp_edit_text_5) {
                if (text.length() == 1) {
                    nextView.requestFocus();
                }
                // You can add conditions for EditText6 and others as needed
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // TODO: Your code here
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // TODO: Your code here
        }
    }


    private void resendOtp() {
        try {
            if (services.isNetworkConnected()) {
                if (services.checkGpsStatus()) {

                } else {
                    services.redirectToGpsSettings();
                }
            } else {
                services.checkNetworkVisibility();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    void verifyOTP() {
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

    void getTimerText() {
        try {
            new CountDownTimer(59999, 1000) {
                @Override
                public void onTick(long l) {
                    String seconds = String.valueOf(l / 1000);
                    String timer;
                    if (seconds.length() == 1) {
                        timer = getString(R.string.timer2) + seconds;
                    } else {
                        timer = getString(R.string.timer) + seconds;
                    }
                    txtOtpTimer.setText(timer);
                }

                @Override
                public void onFinish() {
                    OTPSeconds.setVisibility(View.GONE);
                    resendLayout.setVisibility(View.VISIBLE);
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}