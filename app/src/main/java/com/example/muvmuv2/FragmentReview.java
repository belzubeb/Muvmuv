package com.example.muvmuv2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentReview extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;

    private TextView usernameTextView, emailTextView, username2TextView, email2TextView;
    private ImageView fotoprofileImageView, fotoprofile2ImageView;;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentReview() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentReview.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentReview newInstance(String param1, String param2) {
        FragmentReview fragment = new FragmentReview();
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
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);

        // Initialize UI elements
        usernameTextView = rootView.findViewById(R.id.username);
        emailTextView = rootView.findViewById(R.id.email);
        fotoprofileImageView = rootView.findViewById(R.id.fotoprofile);

        username2TextView = rootView.findViewById(R.id.username2);
        email2TextView = rootView.findViewById(R.id.email2);
        fotoprofile2ImageView = rootView.findViewById(R.id.fotoprofile2);

        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Initialize Firebase Database
        if (currentUser != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());

            // Read data from Firebase Database
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("username").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String photoUrl = dataSnapshot.child("photoProfile").getValue(String.class);

                        // Set data to TextViews
                        usernameTextView.setText(username);
                        emailTextView.setText(email);

                        // Load image using Glide
                        if (photoUrl != null && !photoUrl.isEmpty()) {
                            Glide.with(requireContext())
                                    .load(photoUrl)
                                    .into(fotoprofileImageView);
                        }
                        String username2 = dataSnapshot.child("username").getValue(String.class);
                        String email2 = dataSnapshot.child("email").getValue(String.class);
                        String photoUrl2 = dataSnapshot.child("photoProfile").getValue(String.class);

                        // Set data to TextViews for second set
                        username2TextView.setText(username2);
                        email2TextView.setText(email2);

                        // Load image using Glide for second set
                        if (photoUrl2 != null && !photoUrl2.isEmpty()) {
                            Glide.with(requireContext())
                                    .load(photoUrl2)
                                    .into(fotoprofile2ImageView);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }

        return rootView;
    }
}
