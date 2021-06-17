package com.example.omw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        String password = preferences.getString("password", "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //first entry
                if (password.equals("")) {
                    Intent intent = new Intent(SplashActivity.this, ExplanationActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        }, 5000);

    }

}