package com.example.muvmuv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private TextView usernameTextView, emailTextView, bioTextView;
    private ImageView headerProfileImageView;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView txtreview = rootView.findViewById(R.id.txtreview);
        TextView txtplaylist = rootView.findViewById(R.id.txtplaylist);
        TextView txtwatchlist = rootView.findViewById(R.id.txtwatclist);
        TextView txtdiary = rootView.findViewById(R.id.txtdiary);
        Button btnSetting = rootView.findViewById(R.id.btnSetting);
        LinearLayout folls = rootView.findViewById(R.id.folls);

        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());

        usernameTextView = rootView.findViewById(R.id.username);
        emailTextView = rootView.findViewById(R.id.email);
        bioTextView = rootView.findViewById(R.id.bio);
        headerProfileImageView = rootView.findViewById(R.id.headerprofile);

        // Read data from Firebase Database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String bio = dataSnapshot.child("bio").getValue(String.class);
                    String photoUrl = dataSnapshot.child("photo").getValue(String.class);
                    String photoProfileUrl = dataSnapshot.child("photoProfile").getValue(String.class);

                    // Set data to TextViews
                    usernameTextView.setText(username);
                    emailTextView.setText(email);
                    bioTextView.setText(bio);

                    // Load image using Glide
                    if (photoProfileUrl != null && !photoProfileUrl.isEmpty()) {
                        Glide.with(ProfileFragment.this)
                                .load(photoProfileUrl)
                                .into(headerProfileImageView);
                    } else if (photoUrl != null && !photoUrl.isEmpty()) {
                        Glide.with(ProfileFragment.this)
                                .load(photoUrl)
                                .into(headerProfileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        txtreview.setOnClickListener(this);
        txtplaylist.setOnClickListener(this);
        txtwatchlist.setOnClickListener(this);
        txtdiary.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        folls.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtreview) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.FragmentProfile, new FragmentReview())
                    .commit();
        } else if (v.getId() == R.id.txtplaylist) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.FragmentProfile, new PlayistFragment())
                    .commit();
        } else if (v.getId() == R.id.txtwatclist) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.FragmentProfile, new WatchlistFragment())
                    .commit();
        } else if (v.getId() == R.id.txtdiary) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.FragmentProfile, new DiaryFragment())
                    .commit();
        } else if (v.getId() == R.id.btnSetting) {
            Intent intent = new Intent(getContext(), Settings.class);
            startActivity(intent);
        } else if (v.getId() == R.id.folls) {
            Intent intent = new Intent(getContext(), PageFollow.class);
            startActivity(intent);
        }
    }
}
