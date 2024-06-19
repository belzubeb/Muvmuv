package com.example.muvmuv2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DetailFilmFragment extends Fragment {
    private TextView title, director, duration, genre, rate, synopsis, year;
    private ImageView pictDetail, poster;
    private View backButton;
    private boolean isAddedToWatchlist = false;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String filmPosterUrl;
    private String filmTitle;

    public DetailFilmFragment() {
        // Required empty public constructor
    }

    public static DetailFilmFragment newInstance(String param1) {
        DetailFilmFragment fragment = new DetailFilmFragment();
        Bundle args = new Bundle();
        args.putString("Title", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_film, container, false);

        // Initialize views
        title = view.findViewById(R.id.Title);
        director = view.findViewById(R.id.Direct);
        duration = view.findViewById(R.id.duration);
        genre = view.findViewById(R.id.genre);
        rate = view.findViewById(R.id.rate);
        synopsis = view.findViewById(R.id.sinopsis);
        year = view.findViewById(R.id.year);
        pictDetail = view.findViewById(R.id.pictdetail);
        poster = view.findViewById(R.id.poster);

        // Get film title from arguments
        filmTitle = getArguments().getString("Title");
        Log.d("DetailFilmFragment", "Received film title: " + filmTitle);

        // Initialize DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference("Film").child(filmTitle);

        // Load film details
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String filmTitle = dataSnapshot.child("Title").getValue(String.class);
                    String filmDirector = dataSnapshot.child("direct").getValue(String.class);
                    String filmDuration = dataSnapshot.child("duration").getValue(String.class);
                    String filmGenre = dataSnapshot.child("genre").getValue(String.class);
                    String filmRate = dataSnapshot.child("rate").getValue(String.class);
                    String filmSynopsis = dataSnapshot.child("synopsis").getValue(String.class);
                    String filmYear = dataSnapshot.child("year").getValue(String.class);
                    String filmPictDetail = dataSnapshot.child("pictDetail").getValue(String.class);
                    filmPosterUrl = dataSnapshot.child("poster").getValue(String.class);

                    // Set data to views
                    title.setText(filmTitle);
                    director.setText(filmDirector);
                    duration.setText(filmDuration);
                    genre.setText(filmGenre);
                    rate.setText(filmRate);
                    synopsis.setText(filmSynopsis);
                    year.setText(filmYear + " • DIRECTED BY");
                    loadImageFromUrl(filmPictDetail, pictDetail);
                    loadImageFromUrl(filmPosterUrl, poster);
                } else {
                    Log.d("DetailFilmFragment", "No data found at specified path");
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DetailFilmFragment", "Database error: " + databaseError.getMessage());
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

        Button reviewsButton = view.findViewById(R.id.reviews);
        reviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviews();
            }
        });


        // Set onClickListener for back button
        backButton = view.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                Fragment homeFragment = new HomeFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, homeFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // Set onClickListener for add button
        Button buttonAdd = view.findViewById(R.id.buttonadd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        return view;
    }

    private void showDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog);

        // Initialize views in the dialog
        TextView dialogTitle = dialog.findViewById(R.id.Title);
        TextView dialogYear = dialog.findViewById(R.id.year);
        ToggleButton watchedToggle = dialog.findViewById(R.id.watched);
        ToggleButton watchlistToggle = dialog.findViewById(R.id.addWatchlist);
        Button doneButton = dialog.findViewById(R.id.buttondone);
        ToggleButton loveButton = dialog.findViewById(R.id.btnLove);

        // Get film details
        DatabaseReference filmRef = FirebaseDatabase.getInstance().getReference("Film").child(filmTitle);
        filmRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String filmTitle = snapshot.child("Title").getValue(String.class);
                    String filmYear = snapshot.child("year").getValue(String.class);
                    dialogTitle.setText(filmTitle);
                    dialogYear.setText(filmYear);
                } else {
                    Log.d("DetailFilmFragment", "No data found at specified path");
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DetailFilmFragment", "Database error: " + error.getMessage());
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle watchlist toggle button
        watchlistToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    watchlistToggle.setBackgroundResource(R.drawable.addfill);
                    isAddedToWatchlist = true;
                    if (watchedToggle.isChecked()) {
                        watchedToggle.setChecked(false);
                        watchedToggle.setBackgroundResource(R.drawable.nofillwtached);
                    }
                } else {
                    watchlistToggle.setBackgroundResource(R.drawable.add);
                    isAddedToWatchlist = false;
                }
            }
        });

        // Handle done button
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddedToWatchlist && filmPosterUrl != null) {
                    // Call sendToWatchlist method with necessary film information
                    sendToWatchlist(filmPosterUrl, title.getText().toString(), director.getText().toString(), filmPosterUrl, year.getText().toString());

                    // Tambahkan navigasi kembali ke halaman DetailFilmFragment
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_container, DetailFilmFragment.newInstance(filmTitle));
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                dialog.dismiss();
            }
        });

        // Show dialog
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void sendToWatchlist(final String filmPosterUrl, final String filmTitle, final String filmDirector, final String filmImageUrl, final String filmYear) {
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("poster");

        // Check if the film is already in the watchlist
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isDuplicate = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Poster existingPoster = snapshot.getValue(Poster.class);
                    if (existingPoster != null && existingPoster.getTitle().equals(filmTitle)) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (isDuplicate) {
                    Toast.makeText(getContext(), "Film ini sudah ditambahkan", Toast.LENGTH_SHORT).show();
                } else {
                    long posterCount = dataSnapshot.getChildrenCount();
                    String newPosterKey = "poster" + (posterCount + 1);

                    HashMap<String, Object> posterData = new HashMap<>();
                    posterData.put("director", filmDirector);
                    posterData.put("imageUrl", filmImageUrl);
                    posterData.put("title", filmTitle);
                    posterData.put("year", filmYear);

                    userRef.child(newPosterKey).setValue(posterData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Film berhasil ditambahkan ke Watchlist", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("DetailFilmFragment", "Gagal menambahkan ke watchlist: " + e.getMessage());
                                    Toast.makeText(getContext(), "Gagal menambahkan ke Watchlist", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DetailFilmFragment", "DatabaseError: " + databaseError.getMessage());
            }
        });
    }

    private void loadImageFromUrl(String imageUrl, ImageView imageView) {
        Glide.with(this).load(imageUrl).into(imageView);
    }

    public void reviews() {
        Intent intent = new Intent(getContext(), Reviews.class);
        intent.putExtra("FilmTitle", filmTitle);
        startActivity(intent);
    }
}
