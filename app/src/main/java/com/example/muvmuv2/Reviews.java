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
import com.google.firebase.auth.FirebaseUser;
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

        // Dapatkan filmId dari Intent
        filmId = getIntent().getStringExtra("FilmTitle");

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.reviewRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inisialisasi Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Muat ulasan dari Firebase
        loadReviews();

        // Setup tombol "Tambah Ulasan"
        Button btnAddReview = findViewById(R.id.addReview);
        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddReviewPopup();
            }
        });
    }

    // Metode untuk menampilkan popup tambah ulasan
    public void showAddReviewPopup() {
        Dialog addReviewDialog = new Dialog(this);
        addReviewDialog.setContentView(R.layout.custom_review);

        EditText etReviewContent = addReviewDialog.findViewById(R.id.etReviewContent);
        Button btnSubmitReview = addReviewDialog.findViewById(R.id.btnSubmitReview);

        // Setup tombol "Submit"
        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewContent = etReviewContent.getText().toString().trim();
                if (!reviewContent.isEmpty()) {
                    String userId = mAuth.getCurrentUser().getUid();
                    DatabaseReference userRef = mDatabase.child("users").child(userId);

                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                User user = snapshot.getValue(User.class);
                                if (user != null) {
                                    // Periksa apakah filmId tidak null
                                    if (filmId != null) {
                                        saveReviewToFirebase(reviewContent, user);
                                        addReviewDialog.dismiss();
                                    } else {
                                        Toast.makeText(Reviews.this, "ID Film kosong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Reviews.this, "Gagal mengambil data pengguna", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Reviews.this, "Silakan tulis ulasan Anda", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addReviewDialog.show();
    }

    // Metode untuk menyimpan ulasan ke Firebase
    private void saveReviewToFirebase(String reviewContent, User user) {
        // Periksa apakah filmId null
        if (filmId == null) {
            Toast.makeText(Reviews.this, "ID Film kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference filmReviewsRef = mDatabase.child("Film").child(filmId).child("reviews").push();
        DatabaseReference userReviewRef = mDatabase.child("users").child(currentUser.getUid()).child("reviews").child(filmId).push();

        Review review = new Review(user.getUserId(), user.getUsername(), user.getEmail(), user.getProfilePhoto(), reviewContent, filmId);

        filmReviewsRef.setValue(review)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        userReviewRef.setValue(review)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Reviews.this, "Ulasan berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                        loadReviews();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Reviews.this, "Gagal menambahkan ulasan ke pengguna: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Reviews.this, "Gagal menambahkan ulasan ke film: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Metode untuk memuat ulasan dari Firebase
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
                Toast.makeText(Reviews.this, "Gagal memuat ulasan: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metode untuk menangani tombol kembali
    public void onBackPress(View view) {
        finish();
    }
}
