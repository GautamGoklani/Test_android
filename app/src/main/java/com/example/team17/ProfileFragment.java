package com.example.team17;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button logOut_btn;
    TextView profile_name;
    FirebaseAuth mAuth;
    Button edit_acc, about_us, contact_us;
    FirebaseDatabase database;
    DatabaseReference reference;
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ((MainActivity) getActivity()).setTitle("Profile");

        logOut_btn = view.findViewById(R.id.logOut_btn);
        edit_acc = view.findViewById(R.id.edit_account);
        about_us = view.findViewById(R.id.about_us);
        contact_us = view.findViewById(R.id.contact_us);
        profile_name = view.findViewById(R.id.profile_name);
        mAuth = FirebaseAuth.getInstance();

        retrieveData();

        logOut_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(getActivity(), "Logout SuccessFUlly !", Toast.LENGTH_SHORT).show();
                ProfileFragment.this.startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        edit_acc.setOnClickListener(view1 -> {
            startActivity(new Intent(getActivity(), EditAccountActivity.class));
        });

        about_us.setOnClickListener(view1 -> {
            startActivity(new Intent(getActivity(), AboutUsActivity.class));
        });
        contact_us.setOnClickListener(view1 -> {
            startActivity(new Intent(getActivity(), ContactUsActivity.class));
        });
        return view;
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
                if(snapshot.exists()){
                    name1=snapshot.child(userid).child("uname").getValue(String.class);
                }
                profile_name.setText(name1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}