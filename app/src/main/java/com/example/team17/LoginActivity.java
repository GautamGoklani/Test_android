package com.example.team17;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button signIn_btn;
    TextInputEditText temail, tpass;
    TextInputLayout em_label, pas_label;
    FirebaseAuth mAuth;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        //hooks

        signIn_btn = findViewById(R.id.login_btn);
        temail = findViewById(R.id.username1);
        tpass = findViewById(R.id.password1);
        em_label = findViewById(R.id.uname_label);
        pas_label = findViewById(R.id.pass_label);

        ActionBar aBar;
        aBar = getSupportActionBar();
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#FFFFFF"));
        aBar.setBackgroundDrawable(cd);
        mAuth = FirebaseAuth.getInstance();

        signIn_btn.setOnClickListener(view -> {
            if (!validateUserNameData() | !validatePasswordData()) {
                return;
            } else {
                signIn();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private void signIn() {
        String em1 = temail.getText().toString();
        String pass1 = tpass.getText().toString();
        mAuth.signInWithEmailAndPassword(em1, pass1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Hello, " + em1, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateUserNameData() {
        String val = temail.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            em_label.setError("Email cannot be empty");
            return false;
        } else if (val.matches(emailPattern)) {
            em_label.setError(null);
            em_label.setErrorEnabled(false);
            return true;
        } else {
            em_label.setError("Not a valid email");
            return false;
        }
    }

    private boolean validatePasswordData() {
        String val = tpass.getText().toString().trim();
        if (val.isEmpty()) {
            pas_label.setError("Password cannot be empty");
            return false;
        } else {
            pas_label.setError(null);
            pas_label.setErrorEnabled(false);
            return true;
        }
    }

    public void signup(View view) {
        Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(i);
    }
}