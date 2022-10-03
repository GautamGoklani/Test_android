package com.example.team17;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class editAccount extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FirebaseAuth mAuth;
    TextInputEditText edit_name,edit_phone,edit_email;
    private String mParam1;
    private String mParam2;
    FirebaseDatabase database;
    DatabaseReference reference;
    Button save,cancel;

    public editAccount() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment editAccount.
     */
    // TODO: Rename and change types and number of parameters
    public static editAccount newInstance(String param1, String param2) {
        editAccount fragment = new editAccount();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_edit_account, container, false);
        ((MainActivity) getActivity()).setTitle("About Us");

        mAuth=FirebaseAuth.getInstance();
        save=view.findViewById(R.id.save_btn);
        cancel=view.findViewById(R.id.cancel_btn);
        edit_name=view.findViewById(R.id.edit_name1);
        edit_email=view.findViewById(R.id.edit_email);
        edit_phone=view.findViewById(R.id.edit_phone);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, new ProfileFragment())
                        .commit();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, new ProfileFragment())
                        .commit();
            }
        });
        return view;
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