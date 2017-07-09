package com.editsoft.ansh.mychat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.editsoft.ansh.mychat.R;
import com.editsoft.ansh.mychat.utility.PreferenceHelper;

public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_TIME_OUT = 2000;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (PreferenceHelper.getIsLogin()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, SignUpActivity.class));
                }
            }
        };
        mHandler.postDelayed(mRunnable, SPLASH_TIME_OUT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mHandler.removeCallbacks(mRunnable);
    }
}
