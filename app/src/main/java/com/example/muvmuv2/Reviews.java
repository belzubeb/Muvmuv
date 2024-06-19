package com.example.muvmuv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Reviews extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;
    private String filmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        // Mendapatkan filmId dari Intent
        filmId = getIntent().getStringExtra("FilmTitle");

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.reviewRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inisialisasi Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Load daftar review
        loadReviews();

        Button btnAddReview = findViewById(R.id.addReview);
        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddReviewPopup();
            }
        });
    }

    // Method untuk menampilkan popup tambah review
    public void showAddReviewPopup() {
        Dialog addReviewDialog = new Dialog(this);
        addReviewDialog.setContentView(R.layout.custom_review);

        EditText etReviewContent = addReviewDialog.findViewById(R.id.etReviewContent);
        Button btnSubmitReview = addReviewDialog.findViewById(R.id.btnSubmitReview);

        // Setup tombol "Submit"
        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle logika saat tombol "Submit" ditekan
                String reviewContent = etReviewContent.getText().toString().trim();
                if (!reviewContent.isEmpty()) {
                    // Ambil userID saat ini
                    String userId = mAuth.getCurrentUser().getUid();

                    // Simpan review ke Firebase
                    saveReviewToFirebase(reviewContent, userId);

                    // Tutup dialog
                    addReviewDialog.dismiss();
                } else {
                    Toast.makeText(Reviews.this, "Please write your review", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Menampilkan popup
        addReviewDialog.show();
    }

    // Method untuk menyimpan review ke Firebase
    private void saveReviewToFirebase(String reviewContent, String userId) {
        // Referensi untuk menyimpan ulasan di node Film
        DatabaseReference filmReviewsRef = mDatabase.child("Film").child(filmId).child("reviews").push();
        // Referensi untuk menyimpan ulasan di node users
        DatabaseReference userReviewRef = mDatabase.child("users").child(userId).child("reviews").child(filmId).push();

        // Buat objek Review
        Review review = new Review(userId, reviewContent);

        // Simpan data ulasan ke kedua referensi
        filmReviewsRef.setValue(review)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Ketika sukses menyimpan di node Film, lanjutkan ke node users
                        userReviewRef.setValue(review)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Reviews.this, "Review added successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Reviews.this, "Failed to add review to user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Reviews.this, "Failed to add review to film: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }





    // Method untuk memuat daftar review dari Firebase
    private void loadReviews() {
        DatabaseReference reviewsRef = mDatabase.child("Film").child(filmId).child("reviews");
        reviewsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Review review = snapshot.getValue(Review.class);
                    if (review != null) {
                        reviewList.add(review);
                    }
                }
                // Set adapter untuk RecyclerView
                reviewAdapter = new ReviewAdapter(reviewList);
                recyclerView.setAdapter(reviewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Reviews.this, "Failed to load reviews: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    public void onBackPress(View view) {
        finish();
    }
}
