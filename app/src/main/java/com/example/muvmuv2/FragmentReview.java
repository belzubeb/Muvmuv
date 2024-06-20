package com.example.muvmuv2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentReview extends Fragment {

    private RecyclerView recyclerView;
    private ReviewAdapterProfile adapter;
    private List<ReviewProfile> reviewList;

    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        recyclerView = view.findViewById(R.id.review_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        reviewList = new ArrayList<>();

        if (currentUser != null) {
            DatabaseReference userReviewsRef = databaseReference.child("users").child(currentUser.getUid()).child("reviews");
            userReviewsRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot != null) {
                        for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot itemSnapshot : reviewSnapshot.getChildren()) {
                                ReviewProfile review = itemSnapshot.getValue(ReviewProfile.class);
                                if (review != null) {
                                    reviewList.add(review);
                                }
                            }
                        }
                        // Set up RecyclerView adapter with reviewList
                        adapter = new ReviewAdapterProfile(getContext(), reviewList);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    // Handle error
                }
            });
        }

        return view;
    }
}