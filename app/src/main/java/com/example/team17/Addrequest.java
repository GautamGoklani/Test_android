package com.example.team17;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.Random;

public class Addrequest extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextInputEditText reg_title, reg_description, reg_features;
    TextInputLayout reg_title_label, reg_description_label, reg_features_label;
    Spinner spn_category;
    Random rand = new Random();
    int req_count = rand.nextInt(100000);

    @SuppressLint({"ResourceAsColor", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrequest);
        Objects.requireNonNull(getSupportActionBar()).hide();

        reg_title = findViewById(R.id.reg_title);
        reg_description = findViewById(R.id.reg_description);
        reg_features = findViewById(R.id.reg_features);
        spn_category = findViewById(R.id.category);
        reg_title_label = findViewById(R.id.pro_title);
        reg_description_label = findViewById(R.id.description);
        reg_features_label = findViewById(R.id.features);

        mAuth = FirebaseAuth.getInstance();
    }

    public void AddItem(View view) {
        if (!validatetitle() | !validatedescription() | !validatefeatures()) {
            return;
        }
        String title = reg_title.getText().toString().trim();
        String description = reg_description.getText().toString().trim();
        String features = reg_features.getText().toString().trim();
        String category = spn_category.getSelectedItem().toString().trim();
        String status = "Pending";
        String source="https://github.com/DharamBhojani";

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Requests");
        AddRequestClass addRequestClass = new AddRequestClass(title, description, category, features, status, source);
        String email=mAuth.getCurrentUser().getEmail();
        String userid=email.replaceAll("@gmail.com"," ").replaceAll("@yahoo.com"," ");;

        reference.child(userid).child(title + "" + req_count).setValue(addRequestClass);
        startActivity(new Intent(Addrequest.this, MainActivity.class));
        finish();
    }

    private boolean validatefeatures() {
        String val = reg_features.getText().toString().trim();
        if (val.isEmpty()) {
            reg_features_label.setError("Features cannot be empty");
            return false;
        }
        return true;
    }

    private boolean validatedescription() {
        String val = reg_description.getText().toString().trim();
        if (val.isEmpty()) {
            reg_description_label.setError("Description cannot be empty");
            return false;
        }
        return true;
    }

    private boolean validatetitle() {
        String val = reg_title.getText().toString().trim();
        if (val.isEmpty()) {
            reg_title_label.setError("Title cannot be empty");
            return false;
        }
        return true;
    }
}