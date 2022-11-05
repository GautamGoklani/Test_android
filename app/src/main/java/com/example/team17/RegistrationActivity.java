package com.example.team17;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    Button reg_signUp_btn;
    TextInputEditText reg_temail, reg_tpass, reg_tname, reg_tphone, reg_tconpass;
    TextInputLayout reg_em_label, reg_pas_label, reg_tname_label, reg_tphone_label, reg_tconpass_label;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();
        //hooks
        reg_signUp_btn = findViewById(R.id.reg_btn);
        reg_temail = findViewById(R.id.reg_username1);
        reg_tpass = findViewById(R.id.reg_password1);
        reg_tname = findViewById(R.id.reg_name1);
        reg_tphone = findViewById(R.id.reg_phone);
        reg_tconpass = findViewById(R.id.reg_confirm_password);

        reg_em_label = findViewById(R.id.reg_uname_label);
        reg_pas_label = findViewById(R.id.reg_pass_label);
        reg_tname_label = findViewById(R.id.reg_name_label);
        reg_tphone_label = findViewById(R.id.reg_phone_label);
        reg_tconpass_label = findViewById(R.id.reg_confirm_pass_label);

        //white action bar
        ActionBar aBar;
        aBar = getSupportActionBar();
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#FFFFFF"));
        aBar.setBackgroundDrawable(cd);

        //making instance of Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        reg_signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUserNameData() | !validatePhoneData() | !validatePasswordData() | !validateNameData() | !validateConfirmPasswordData()) {
                    return;
                } else {
                    signIn();
                }
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            finish();
        }
    }

    private void signIn() {
        String em1 = reg_temail.getText().toString();
        String pass1 = reg_tpass.getText().toString();
        String uname = reg_tname.getText().toString();
        String phone = reg_tphone.getText().toString();

        mAuth.createUserWithEmailAndPassword(em1, pass1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("Users");
                    String msg="hi";
                    String method = "registration";
                    UserHelperClass userHelperClass = new UserHelperClass(uname, phone, em1, pass1, msg, method);
                    String userid=em1.replaceAll("@gmail.com"," ").replaceAll("@yahoo.com"," ");
                    reference.child(userid).setValue(userHelperClass);
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegistrationActivity.this, "Internet Connectivity Required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean validateUserNameData() {
        String val = reg_temail.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            reg_em_label.setError("Email cannot be empty");
            return false;
        } else if (val.matches(emailPattern)) {
            reg_em_label.setError(null);
            reg_em_label.setErrorEnabled(false);
            return true;
        } else {
            reg_em_label.setError("Not a valid email");
            return false;
        }
    }

    private boolean validatePasswordData() {
        String val = reg_tpass.getText().toString().trim();
        if (val.isEmpty()) {
            reg_pas_label.setError("Password cannot be empty");
            return false;
        } else {
            reg_pas_label.setError(null);
            reg_pas_label.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateNameData() {
        String val = reg_tname.getText().toString().trim();
        String namePattern = "^\\w+( +\\w+)*$";
        if (val.isEmpty()) {
            reg_tname_label.setError("Name cannot be empty");
            return false;
        } else if (val.matches(namePattern)) {
            reg_tname_label.setError(null);
            reg_tname_label.setErrorEnabled(false);
            return true;
        } else {
            reg_tname_label.setError("Not a valid name");
            return false;
        }
    }

    private boolean validatePhoneData() {
        String val = reg_tphone.getText().toString().trim();
        String phonePattern = "^[0-9]{10}$";
        if (val.isEmpty()) {
            reg_tphone_label.setError("Phone Number cannot be empty");
            return false;
        } else if (val.matches(phonePattern)) {
            reg_tphone_label.setError(null);
            reg_tphone_label.setErrorEnabled(false);
            return true;
        } else {
            reg_tphone_label.setError("Not a valid phone number");
            return false;
        }
    }

    private boolean validateConfirmPasswordData() {
        String val = reg_tconpass.getText().toString().trim();
        String val2 = reg_tpass.getText().toString().trim();
        if (val.isEmpty()) {
            reg_em_label.setError("Password Cannot be empty");
            return false;
        } else if (val.matches(val2)) {
            reg_tconpass_label.setError(null);
            reg_tconpass_label.setErrorEnabled(false);
            return true;
        } else {
            reg_tconpass_label.setError("Confirm Password doesn't match upper password");
            return false;
        }
    }

}