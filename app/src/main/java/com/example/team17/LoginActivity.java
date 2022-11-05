package com.example.team17;

import static android.content.ContentValues.TAG;

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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    Button signIn_btn;
    ExtendedFloatingActionButton google_signin;
    TextInputEditText temail, tpass;
    TextInputLayout em_label, pas_label;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

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
        google_signin = findViewById(R.id.google_sign_in);

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

        google_signin.setOnClickListener(view -> {
            googlesignin();
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
                    retrieveData();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Wrong Id or Password", Toast.LENGTH_SHORT).show();
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
        finish();
    }

    private void retrieveData() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        String email_userid = mAuth.getCurrentUser().getEmail();
        String userid = email_userid.replaceAll("@gmail.com", " ").replaceAll("@yahoo.com", " ");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name1 = null;
                if (snapshot.exists()) {
                    name1 = snapshot.child(userid).child("uname").getValue(String.class);
                }
                Toast.makeText(LoginActivity.this, "Hello, " + name1, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void googlesignin() {

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    storeDataFirebase();
                                    retrieveDataFirebase();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void storeDataFirebase() {
        String uname = mAuth.getCurrentUser().getDisplayName();
        String em1 = mAuth.getCurrentUser().getEmail();
        String method = "google";
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        String msg = "hi";
        UserHelperClass userHelperClass = new UserHelperClass(uname, em1, msg, method);
        String userid = em1.replaceAll("@gmail.com", " ").replaceAll("@rku.ac.in", " ").replaceAll("@yahoo.com", " ");
        reference.child(userid).setValue(userHelperClass);
    }

    private void retrieveDataFirebase() {
        String name = mAuth.getCurrentUser().getDisplayName();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(LoginActivity.this, "Hello, " + name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}