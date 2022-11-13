package com.example.team17;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactUsActivity extends AppCompatActivity {

    ExtendedFloatingActionButton send_message, email, phone;
    TextInputEditText message;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        ActionBar aBar;
        aBar = getSupportActionBar();
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#FFFFFF"));
        aBar.setBackgroundDrawable(cd);
        aBar.setTitle("Contact Us");

        message = findViewById(R.id.question_message);
        send_message = findViewById(R.id.send_message);
        email = findViewById(R.id.btn_email);
        phone = findViewById(R.id.btn_call);

        mAuth = FirebaseAuth.getInstance();
        send_message.setOnClickListener(view -> {

            String msg = message.getText().toString();
            database = FirebaseDatabase.getInstance();
            reference = database.getReference("Users");
            String email_userid = mAuth.getCurrentUser().getEmail();
            String userid = email_userid.replaceAll("@gmail.com", " ").replaceAll("@yahoo.com", " ");
            reference.child(userid).child("msg").setValue(msg);

            startActivity(new Intent(ContactUsActivity.this, MainActivity.class));
        });

        email.setOnClickListener(view -> {
            String email = "dmbhojani446@gmail.com";
            Intent emailIntent = new Intent(Intent.ACTION_VIEW);
            emailIntent.setData(Uri.parse("mailto:" + email));
            startActivity(emailIntent);
        });

        phone.setOnClickListener(view -> {
            String phone = "9998876425";
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phone));
            startActivity(callIntent);
        });
    }
}