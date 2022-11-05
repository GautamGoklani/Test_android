package com.example.team17;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    //variables
    ImageView img1, img2;
    FirebaseAuth mAuth;
    //Animation
    Animation sideAnim, bottomAnim, rightAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Objects.requireNonNull(getSupportActionBar()).hide();
        //hooks
        img1 = findViewById(R.id.shapeableImageView);
        img2 = findViewById(R.id.shapeableImageView2);

        mAuth = FirebaseAuth.getInstance();
        //Animation hooks
        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        rightAnim = AnimationUtils.loadAnimation(this, R.anim.align);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        //set animations to elements
        img1.setAnimation(sideAnim);
        img2.setAnimation(rightAnim);

        new Handler().postDelayed(() -> {
            String prevStarted = "yes";
            SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
            if (!sharedpreferences.getBoolean(prevStarted, false)) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(prevStarted, Boolean.TRUE);
                editor.apply();
                Intent intent = new Intent(SplashActivity.this, GetstartedActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, 2000);
    }
}