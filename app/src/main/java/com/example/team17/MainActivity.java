package com.example.team17;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar aBar;
        aBar = getSupportActionBar();
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#FFFFFF"));
        Objects.requireNonNull(aBar).setBackgroundDrawable(cd);

        mAuth = FirebaseAuth.getInstance();

        FragmentManager ft = getSupportFragmentManager();
        ft.beginTransaction()
                .replace(R.id.fragment, HomeFragment.class, null)
                .setReorderingAllowed(true)
                .commit();

        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    FragmentManager ft1 = getSupportFragmentManager();
                    ft1.beginTransaction()
                            .replace(R.id.fragment, HomeFragment.class, null)
                            .setReorderingAllowed(true)
                            .commit();
                    return true;
                case R.id.project:
                    FragmentManager ft2 = getSupportFragmentManager();
                    ft2.beginTransaction()
                            .replace(R.id.fragment, ProjectFragment.class, null)
                            .setReorderingAllowed(true)
                            .commit();
                    return true;
                case R.id.requests:
                    FragmentManager ft3 = getSupportFragmentManager();
                    ft3.beginTransaction()
                            .replace(R.id.fragment, RequestFragment.class, null)
                            .setReorderingAllowed(true)
                            .commit();
                    return true;
                case R.id.profile:
                    FragmentManager ft4 = getSupportFragmentManager();
                    ft4.beginTransaction()
                            .replace(R.id.fragment, ProfileFragment.class, null)
                            .setReorderingAllowed(true)
                            .commit();
                    return true;
            }
            return false;
        });
    }

    public void addreq(View view) {
        Intent i=new Intent(MainActivity.this,Addrequest.class);
        startActivity(i);
    }
}