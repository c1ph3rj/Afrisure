package com.swiftant.afrisure.landing;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.swiftant.afrisure.R;
import com.swiftant.afrisure.common.Services;
import com.swiftant.afrisure.landing.*;
import com.swiftant.afrisure.common.Services.*;

import com.google.android.material.tabs.TabLayout;
import com.swiftant.afrisure.landing.adapter.ImageAdapter;
import com.swiftant.afrisure.landing.model.ImageModel;
import com.swiftant.afrisure.login.email.LoginEmail;

import java.util.ArrayList;

public class LandingPage extends AppCompatActivity {

    Context context;
    Activity activity;
    ViewPager imageViewPager;
    TextView skipButtonLP, lpPrevious, lpNext, lpTitle, lpSubTxt;
    TabLayout tabDottedLine;
    ArrayList<ImageModel> imageList;
    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        context = this;
        activity = this;
        Services services = new Services(this, null);

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                services.alertTheUser("", "Are you sure, you want to exit the application?")
                        .setCancelable(false)
                        .setPositiveButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                        .setNegativeButton("Yes", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            finishAffinity();
                        }).show();
            }
        });
        try {
            getSupportActionBar().hide();
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void init() {
        try {
            imageViewPager = findViewById(R.id.imageViewPager);
            skipButtonLP = findViewById(R.id.skipButtonLP);
            lpPrevious = findViewById(R.id.lpPrevious);
            lpNext = findViewById(R.id.lpNext);
            lpTitle = findViewById(R.id.lpTitle);
            lpSubTxt = findViewById(R.id.lpSubTxt);
            tabDottedLine = findViewById(R.id.tabDottedLine);
            basicFunctions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void basicFunctions() {
        try {
            imageList = new ArrayList<>();
            imageList.add(new ImageModel(R.drawable.dummy_1));
            imageList.add(new ImageModel(R.drawable.dummy_2));
            imageList.add(new ImageModel(R.drawable.dummy_3));
            imageList.add(new ImageModel(R.drawable.logo_black));
            adapter = new ImageAdapter(context, imageList);
            imageViewPager.setAdapter(adapter);
            tabDottedLine.setupWithViewPager(imageViewPager, true);

            // Set a page change listener to handle enabling/disabling of previous button/textview
            imageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    // Not needed for this implementation
                }

                @Override
                public void onPageSelected(int position) {
                    // Enable/disable previous TextView based on current position
                    lpPrevious.setEnabled(position > 0);
                    lpPrevious.setTextColor(position > 0 ? getColor(R.color.themeColor) : getColor(R.color.lightGrey));
                    switch (position) {
                        case 0: {
                            lpTitle.setText("Secure Your Drive");
                            lpSubTxt.setText("Experience Peace of Mind on Every Journey");
                            break;
                        }
                        case 1: {
                            lpTitle.setText("Effortless Protection");
                            lpSubTxt.setText("Seamless Quotes, Swift Protection");
                            break;
                        }
                        case 2: {
                            lpTitle.setText("24/7 Support, Always Covered");
                            lpSubTxt.setText("Reliable Support for Every Bump in the Road");
                            break;
                        }
                        case 3: {
                            lpTitle.setText("Afrisure");
                            lpSubTxt.setText("Tailored Policies, Hassle-Free Claims");
                            break;
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    // Not needed for this implementation
                }
            });

            // Set click listeners for previous and next TextViews
            lpPrevious.setOnClickListener(l -> {
                int currentPosition = imageViewPager.getCurrentItem();
                if (currentPosition > 0) {
                    imageViewPager.setCurrentItem(currentPosition - 1);
                }
            });

            lpNext.setOnClickListener(l -> {
                int currentPosition = imageViewPager.getCurrentItem();
                int totalCount = adapter.getCount();
                if (currentPosition < totalCount - 1) {
                    imageViewPager.setCurrentItem(currentPosition + 1);
                } else {
                    startActivity(new Intent(context, LoginEmail.class));
                }
            });

            skipButtonLP.setOnClickListener(l -> {
                startActivity(new Intent(context, LoginEmail.class));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}