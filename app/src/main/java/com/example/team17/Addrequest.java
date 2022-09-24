package com.example.team17;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Addrequest extends AppCompatActivity {

    FirebaseAuth mAuth;
    static int req_count=0;
    FirebaseDatabase database;
    DatabaseReference reference;
    Button add_btn;
    public TextInputEditText reg_title, reg_description, reg_features;
    public TextInputLayout reg_title_label, reg_description_label, reg_features_label;
    Spinner spn_category;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrequest);
        Objects.requireNonNull(getSupportActionBar()).hide();

//        reg_title = findViewById(R.id.pro_title);
//        reg_description = findViewById(R.id.description);
//        reg_features = findViewById(R.id.features);
//        spn_category = findViewById(R.id.category);
//        reg_title_label = findViewById(R.id.reg_title);
//        reg_description_label = findViewById(R.id.reg_description);
//        reg_features_label = findViewById(R.id.reg_features);

        add_btn = findViewById(R.id.add_btn);

        mAuth = FirebaseAuth.getInstance();

        add_btn.setOnClickListener(view -> {
            if (!validatetitle() | validatedescription() | validatefeatures()) {
                return;
            } else {
                String title = reg_title.getText().toString().trim();
                String description = reg_description.getText().toString().trim();
                String features = reg_features.getText().toString().trim();
                String category = spn_category.getSelectedItem().toString().trim();
                req_count++;

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("Requests");
                AddRequestClass addRequestClass = new AddRequestClass(title, description, category, features);
                String userid = mAuth.getUid();
                if (userid != null) {
                    reference.child(userid).child(String.valueOf(req_count)).setValue(addRequestClass);
                    startActivity(new Intent(Addrequest.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

    private boolean validatefeatures() {
        String val = reg_features.getText().toString().trim();
        if (val.isEmpty()) {
            reg_features_label.setError("Features cannot be empty");
            return false;
        } else {
            return true;
        }
    }

    private boolean validatedescription() {
        String val = reg_description.getText().toString().trim();
        if (val.isEmpty()) {
            reg_description_label.setError("Description cannot be empty");
            return false;
        } else {
            return true;
        }
    }

    private boolean validatetitle() {
        String val = reg_title.getText().toString().trim();
        if (val.isEmpty()) {
            reg_title_label.setError("Title cannot be empty");
            return false;
        } else {
            return true;
        }
    }
}