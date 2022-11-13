package com.example.team17;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.Random;

public class Addrequest extends AppCompatActivity {

    FirebaseAuth mAuth;
    ExtendedFloatingActionButton add_request;
    TextInputEditText reg_title, reg_description, reg_features;
    TextInputLayout reg_title_label, reg_description_label, reg_features_label;
    AutoCompleteTextView spn_category;
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
        reg_title_label = findViewById(R.id.pro_title);
        reg_description_label = findViewById(R.id.description);
        reg_features_label = findViewById(R.id.features);
        add_request = findViewById(R.id.add);
        spn_category = findViewById(R.id.category);

        mAuth = FirebaseAuth.getInstance();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        AutoCompleteTextView textView = (AutoCompleteTextView) spn_category;
        textView.setAdapter(adapter);

        add_request.setOnClickListener(view -> {
            AddItem();
        });

    }

    private static final String[] COUNTRIES = new String[]{
            "Mobile Application Development", "Website Development", "System Design"
    };

    public void AddItem() {
        if (!validatetitle() | !validatedescription() | !validatefeatures()) {
            return;
        }
        String title = reg_title.getText().toString().trim();
        String description = reg_description.getText().toString().trim();
        String features = reg_features.getText().toString().trim();
        String category = spn_category.getText().toString().trim();
        String status = "Pending";

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Requests");
        AddRequestClass addRequestClass = new AddRequestClass(title, description, category, features, status);
        String email = mAuth.getCurrentUser().getEmail();
        String userid = email.replaceAll("@gmail.com", " ").replaceAll("@rku.ac.in"," ").replaceAll("@yahoo.com", " ");
        ;

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