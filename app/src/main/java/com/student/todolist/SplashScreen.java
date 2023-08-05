package com.student.todolist;

import static com.student.todolist.R.layout.activity_splash_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    TextView appName;
    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        appName= findViewById(R.id.appName);
        lottieAnimationView = findViewById(R.id.lottieAnimation);

        appName.animate().translationY(1400).setDuration(2700).setStartDelay(0);
        lottieAnimationView.animate().setDuration(2000).setStartDelay(2900);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
// This method will be executed once the timer is over
                Intent iLogin = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(iLogin);
                finish();
            }
        },3000);

    }
}