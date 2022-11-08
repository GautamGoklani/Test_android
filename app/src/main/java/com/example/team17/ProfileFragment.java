package com.example.team17;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button logOut_btn;
    TextView profile_name, profile_email;
    FirebaseAuth mAuth;
    Button edit_acc, about_us, contact_us;
    FirebaseDatabase database;
    DatabaseReference reference;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    String method = null;
    CircleImageView image;
    Uri url;
    FirebaseStorage storage;
    StorageReference storageReference;

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
        profile_email = view.findViewById(R.id.profile_email);
        image = view.findViewById(R.id.profile_image);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        retrieveData();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        logOut_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseRequireInsteadOfGet")
            @Override
            public void onClick(View view) {
                if (method == "google") {
                    gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();

                    gsc = GoogleSignIn.getClient(ProfileFragment.this.getActivity(), gso);
                    gsc.signOut();

                    mAuth.signOut();
                    Toast.makeText(getActivity(), "Logout Successful !", Toast.LENGTH_SHORT).show();
                    ProfileFragment.this.startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                } else {
                    mAuth.signOut();
                    Toast.makeText(getActivity(), "Logout Successful !", Toast.LENGTH_SHORT).show();
                    ProfileFragment.this.startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
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

    private String retrieveData() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        String email_userid = mAuth.getCurrentUser().getEmail();
        String userid = email_userid.replaceAll("@gmail.com", " ").replaceAll("@rku.ac.in", " ").replaceAll("@yahoo.com", " ");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name1 = null;
                String email1 = null;
                String url = null;
                if (snapshot.exists()) {
                    name1 = snapshot.child(userid).child("uname").getValue(String.class);
                    method = snapshot.child(userid).child("method").getValue(String.class);
                    email1 = snapshot.child(userid).child("email").getValue(String.class);
                    url = snapshot.child(userid).child("url").getValue(String.class);
                }
                profile_name.setText(name1);
                profile_email.setText(email1);
                Picasso.get().load(url).into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return profile_name.getText().toString();
    }


    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), 22);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            url = data.getData();
            if (url != null) {
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                reference = database.getReference("Users");
                String name1 = retrieveData();
                reference = database.getReference("Users");
                String email_userid = mAuth.getCurrentUser().getEmail();
                String userid = email_userid.replaceAll("@gmail.com", " ").replaceAll("@rku.ac.in", " ").replaceAll("@yahoo.com", " ");
                reference.child(userid).child("url").setValue(url.toString());
                StorageReference ref = storageReference.child("images/" + name1);
                ref.putFile(url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                    }
                                })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                // Error, Image not uploaded
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(
                                    UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });
            }
        }
    }

}