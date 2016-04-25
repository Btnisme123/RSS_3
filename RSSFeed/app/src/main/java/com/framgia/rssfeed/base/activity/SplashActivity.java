package com.framgia.rssfeed.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.framgia.rssfeed.MainActivity;
import com.framgia.rssfeed.R;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        waitUntilFinishInitializing();
    }

    public void waitUntilFinishInitializing() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switchToMainActivity();
            }
        }, SPLASH_DURATION);
    }

    private void switchToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
