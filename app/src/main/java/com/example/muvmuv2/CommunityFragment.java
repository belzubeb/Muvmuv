package com.example.muvmuv2;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private TextView txtActivity, txtCommunity, usernameTextView;
    private CircleImageView fotoProfileImageView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabase;

    public CommunityFragment() {
        // Required empty public constructor
    }

    public static CommunityFragment newInstance(String param1, String param2) {
        CommunityFragment fragment = new CommunityFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_community, container, false);

        txtActivity = rootView.findViewById(R.id.txtActivity);
        txtCommunity = rootView.findViewById(R.id.txtCommunity);
        usernameTextView = rootView.findViewById(R.id.username);
        fotoProfileImageView = rootView.findViewById(R.id.fotoprofile);

        txtActivity.setOnClickListener(this);
        txtCommunity.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("username").getValue(String.class);
                        if (username != null && !username.isEmpty()) {
                            usernameTextView.setText("Hello, " + username);
                        }

                        String photoUrl = dataSnapshot.child("photoProfile").getValue(String.class);
                        if (photoUrl != null && !photoUrl.isEmpty()) {
                            Picasso.get().load(photoUrl).into(fotoProfileImageView);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle error
                }
            });
        }

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ActivityFragmentChild())
                .commit();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtActivity) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ActivityFragmentChild())
                    .commit();
        } else if (v.getId() == R.id.txtCommunity) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CommunityFragmentChild())
                    .commit();
        }
    }
}
