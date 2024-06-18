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
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private View backButton;
    private boolean isAddedToWatchlist = false;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String filmPosterUrl;  // Add this field to store the poster URL

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_film, container, false);

        // Initialize the views
        title = view.findViewById(R.id.Title);
        director = view.findViewById(R.id.Direct);
        duration = view.findViewById(R.id.duration);
        genre = view.findViewById(R.id.genre);
        rate = view.findViewById(R.id.rate);
        synopsis = view.findViewById(R.id.sinopsis);
        year = view.findViewById(R.id.year);
        pictDetail = view.findViewById(R.id.pictdetail);
        poster = view.findViewById(R.id.poster);

        String filmTitle = getArguments().getString("Title");
        Log.d("DetailFilmFragment", "Received film title: " + filmTitle);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Film").child(filmTitle);
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
                    filmPosterUrl = dataSnapshot.child("poster").getValue(String.class);  // Save the poster URL
                    String filmTrailerUrl = dataSnapshot.child("urlTrailer").getValue(String.class);

                    // Set the data to the views
                    title.setText(filmTitle);
                    director.setText(filmDirector);
                    duration.setText(filmDuration);
                    genre.setText(filmGenre);
                    rate.setText(filmRate);
                    synopsis.setText(filmSynopsis);
                    year.setText(filmYear + " • DIRECTED BY");
                    loadImageFromUrl(filmPictDetail, pictDetail);
                    loadImageFromUrl(filmPosterUrl, poster);

                    Button trailerButton = view.findViewById(R.id.btntrailer);
                    trailerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openYoutubeTrailer(filmTrailerUrl);
                        }
                    });
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
                reviews(v);
            }
        });

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

        Button buttonAdd = view.findViewById(R.id.buttonadd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        return view;
    }

    private void openYoutubeTrailer(String filmTrailerUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(filmTrailerUrl));
        startActivity(intent);
    }

    private void loadImageFromUrl(String imageUrl, ImageView imageView) {
        Glide.with(this).load(imageUrl).into(imageView);
    }

    private void showDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog);

        ToggleButton watchedToggle = dialog.findViewById(R.id.watched);
        ToggleButton watchlistToggle = dialog.findViewById(R.id.addWatchlist);
        ToggleButton loveButton = dialog.findViewById(R.id.btnLove);

        watchedToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    watchedToggle.setBackgroundResource(R.drawable.wathced);
                    if (watchlistToggle.isChecked()) {
                        watchlistToggle.setChecked(false);
                        watchlistToggle.setBackgroundResource(R.drawable.add);
                    }
                } else {
                    watchedToggle.setBackgroundResource(R.drawable.nofillwtached);
                }
            }
        });
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
        Button doneButton = dialog.findViewById(R.id.buttondone);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddedToWatchlist && filmPosterUrl != null) {
                    // Panggil metode sendToWatchlist dengan informasi film yang diperlukan
                    sendToWatchlist(filmPosterUrl, title.getText().toString(), director.getText().toString(), filmPosterUrl, year.getText().toString());
                }
                dialog.dismiss();
            }
        });
        loveButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    loveButton.setBackgroundResource(R.drawable.love_fill);
                } else {
                    loveButton.setBackgroundResource(R.drawable.love);
                }
            }
        });

        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void sendToWatchlist(final String filmPosterUrl, final String filmTitle, final String filmDirector, final String filmImageUrl, final String filmYear) {
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("poster");

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
    public void reviews(View view) {
        Intent intent = new Intent(requireContext(), Reviews.class);
        startActivity(intent);
    }
}
