package com.swiftant.afrisure.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.swiftant.afrisure.R;
import com.swiftant.afrisure.line_of_business.LineOfBusinessScreen;

public class Dashboard extends AppCompatActivity {

    private static final int ID_HOME = 1;
    private static final int ID_EXPLORE = 2;
    private static final int ID_MESSAGE = 3;
    private static final int ID_NOTIFICATION = 4;
    private static final int ID_ACCOUNT = 5;
//    private CurvedBottomNavigation bottomNavigation;
    private long lastClickTime = 0;
    CardView mainOptionsBtn;

    private int SELECTED_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        init();
    }

    void init() {
        try {
            mainOptionsBtn = findViewById(R.id.mainOptionsBtn);
            mainOptionsBtn.setOnClickListener(onClickMainOptions ->
                    startActivity(new Intent(this, LineOfBusinessScreen.class)));
//            startActivity(new Intent(this, AudioRecorder.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}