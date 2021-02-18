package com.mobiloby.paylapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;

public class SpashScreenActivity extends AppCompatActivity {

    SharedPreferences preferences;
    Boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorWhiteLight));// set status background white


        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        isLoggedIn = preferences.getBoolean("isLoggedIn", false);

//        videoView = findViewById(R.id.videoView);

//        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.kare_splash);

//        videoView.setVideoURI(video);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();
                if(isLoggedIn) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish();
                }
            }
        }, 2000);   //2 seconds

//        videoView.setZOrderOnTop(true);
//        videoView.start();
    }
}