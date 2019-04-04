package com.ibm.mysampleapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen easySplashScreen = new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(2000)
                .withBackgroundColor(Color.parseColor("#FF9754CB"))
                .withLogo(R.drawable.etzion)
                .withHeaderText("ברוכים הבאים לרדיו שלנו")
                .withFooterText("CopyRight 2019")
                .withBeforeLogoText("רדיו עציון")
                .withAfterLogoText("Yuval,Nir and Assaf Dev,Ltd");

        easySplashScreen.getHeaderTextView().setTextColor(Color.WHITE);
        easySplashScreen.getFooterTextView().setTextColor(Color.WHITE);
        easySplashScreen.getBeforeLogoTextView().setTextColor(Color.WHITE);
        easySplashScreen.getAfterLogoTextView().setTextColor(Color.WHITE);

        View config = easySplashScreen.create();
        setContentView(config);
    }
}
