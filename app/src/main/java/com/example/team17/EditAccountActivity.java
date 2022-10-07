package com.example.team17;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class EditAccountActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextInputEditText edit_name,edit_phone,edit_email;
    FirebaseDatabase database;
    DatabaseReference reference;
    Button save;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        ActionBar aBar;
        aBar = getSupportActionBar();
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#FFFFFF"));
        Objects.requireNonNull(aBar).setBackgroundDrawable(cd);

        mAuth = FirebaseAuth.getInstance();
        save = findViewById(R.id.save_btn);
        edit_name = findViewById(R.id.edit_name1);
        edit_email = findViewById(R.id.edit_email);
        edit_phone = findViewById(R.id.edit_phone);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit();
                startActivity(new Intent(EditAccountActivity.this,MainActivity.class));
            }
        });
    }

    private void edit() {
        String name = edit_name.getText().toString();
        String email = edit_email.getText().toString();
        String phone = edit_phone.getText().toString();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        String email_userid=mAuth.getCurrentUser().getEmail();
        String userid = email_userid.replaceAll("@gmail.com", " ").replaceAll("@yahoo.com", " ");

        if (!name.isEmpty()) {
            reference.child(userid).child("uname").setValue(name);
        }
        if (!email.isEmpty()) {
            reference.child(userid).child("email").setValue(email);
        }
        if(!phone.isEmpty()){
            reference.child(userid).child("phone").setValue(phone);
        }
    }
}