package com.example.team17;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Addrequest extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrequest);
        ActionBar aBar;
        aBar = getSupportActionBar();
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#FFFFFF"));
        Objects.requireNonNull(aBar).setTitle("Add Request");
        Objects.requireNonNull(aBar).setBackgroundDrawable(cd);

        mAuth = FirebaseAuth.getInstance();

    }
}