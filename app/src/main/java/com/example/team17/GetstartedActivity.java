package com.example.team17;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Objects;

public class GetstartedActivity extends AppCompatActivity {


    ExtendedFloatingActionButton btn_getstarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getstarted);

        btn_getstarted=findViewById(R.id.btn_getstarted);
        Objects.requireNonNull(getSupportActionBar()).hide();

        btn_getstarted.setOnClickListener(view -> {
            startActivity(new Intent(GetstartedActivity.this,LoginActivity.class));
            finish();
        });
    }
}