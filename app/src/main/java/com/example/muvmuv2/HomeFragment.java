package com.example.muvmuv2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.utils.ViewTimeCycle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView textUsername;
    private ImageView fotoProfile;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabase;

    private String mParam1;
    private String mParam2;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        textUsername = rootView.findViewById(R.id.username);
        fotoProfile = rootView.findViewById(R.id.fotoprofile);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            // Mengambil instance Firebase Database
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());

            // Menambahkan listener untuk mengambil data pengguna
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Jika data pengguna ada, set teks username sesuai dengan nama pengguna yang tersimpan
                        String username = snapshot.child("username").getValue(String.class);
                        textUsername.setText(username);

                        String photoUrl = snapshot.child("photoProfile").getValue(String.class);
                        if (photoUrl != null && !photoUrl.isEmpty()) {
                            // Gunakan Picasso untuk memuat foto profil ke ImageView
                            Picasso.get().load(photoUrl).into(fotoProfile);
                        }
                    } else {
                        // Jika tidak ada data pengguna, set teks ke "Login Gagal"
                        textUsername.setText("Login Gagal");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("HomeFragment", "Failed to read user data.", error.toException());
                }
            });
        } else {
            textUsername.setText("Login Gagal");
        }

        ImageView imageView = rootView.findViewById(R.id.FightClub);
        ImageView imageView2 = rootView.findViewById(R.id.Wonka);
        ImageView imageView3 = rootView.findViewById(R.id.RoadHouse);
        ImageView imageView6 = rootView.findViewById(R.id.godzilla);
        ImageView imageView4 = rootView.findViewById(R.id.Anatomy);
        ImageView imageView5 = rootView.findViewById(R.id.Aquaman);
        ImageView imageView7 = rootView.findViewById(R.id.bob);
        ImageView imageView1 = rootView.findViewById(R.id.Argylle);
        ImageView imageView8 = rootView.findViewById(R.id.Damsel);
        ImageView imageView9 = rootView.findViewById(R.id.Dune);
        ImageView imageView10 = rootView.findViewById(R.id.Fighter);
        ImageView imageView11 = rootView.findViewById(R.id.ghost);
        ImageView imageView12 = rootView.findViewById(R.id.imaginary);
        ImageView imageView13 = rootView.findViewById(R.id.Kungfu);
        ImageView imageView14 = rootView.findViewById(R.id.MadameWeb);
        ImageView imageView15 = rootView.findViewById(R.id.Ordinary);
        ImageView imageView16 = rootView.findViewById(R.id.poorthings);
        ImageView imageView17 = rootView.findViewById(R.id.Shirley);
        ImageView imageView18 = rootView.findViewById(R.id.Spiderman);
        ImageView imageView19 = rootView.findViewById(R.id.TheMarvels);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Fight Club";
                Log.d("HomeFragment", "Clicked on Fight Club");
                openDetailFilmFragment(filmTitle);
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Wonka";
                Log.d("HomeFragment", "Clicked on Wonka");
                openDetailFilmFragment(filmTitle);
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Road House";
                Log.d("HomeFragment", "Clicked on Road House");
                openDetailFilmFragment(filmTitle);
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Anatomy of a Fall";
                Log.d("HomeFragment", "Clicked on Anatomy of a Fall");
                openDetailFilmFragment(filmTitle);
            }
        });
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Aquaman and the Lost Kigdom";
                Log.d("HomeFragment", "Clicked on Aquaman and the Lost Kigdom");
                openDetailFilmFragment(filmTitle);
            }
        });
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Godzilla x Kong: The New Empire";
                Log.d("HomeFragment", "Clicked on Godzilla x Kong: The New Empire");
                openDetailFilmFragment(filmTitle);
            }
        });
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Argylle";
                Log.d("HomeFragment", "Clicked on Argylle");
                openDetailFilmFragment(filmTitle);
            }
        });
        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Bob Marley: One Love";
                Log.d("HomeFragment", "Clicked on Bob Marley: One Love");
                openDetailFilmFragment(filmTitle);
            }
        });
        imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Damsel";
                Log.d("HomeFragment", "Clicked on Damsel");
                openDetailFilmFragment(filmTitle);
            }
        });
        imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Dune";
                Log.d("HomeFragment", "Clicked on Dune");
                openDetailFilmFragment(filmTitle);
            }
        });
        imageView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Fighter";
                Log.d("HomeFragment", "Clicked on Fighter");
                openDetailFilmFragment(filmTitle);
            }
        });
        imageView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Ghostbusters: Frozen Empire";
                Log.d("HomeFragment", "Clicked on Ghostbusters: Frozen Empire");
                openDetailFilmFragment(filmTitle);
            }
        });
        imageView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Imaginary";
                Log.d("HomeFragment", "Clicked on Imaginary");
                openDetailFilmFragment(filmTitle);
            }
        });
        imageView13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Kung Fu Panda 4";
                Log.d("HomeFragment", "Clicked on Kung Fu Panda 4");
                openDetailFilmFragment(filmTitle);
            }
        });
        imageView14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Madame Web";
                Log.d("HomeFragment", "Clicked on Madame Web");
                openDetailFilmFragment(filmTitle);
            }
        });
        imageView15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Ordinary Angels";
                Log.d("HomeFragment", "Clicked on Ordinary Angels");
                openDetailFilmFragment(filmTitle);
            }
        });
        imageView16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Poor Things";
                Log.d("HomeFragment", "Clicked on Poor Things");
                openDetailFilmFragment(filmTitle);
            }
        });
        imageView17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Shirley";
                Log.d("HomeFragment", "Clicked on Shirley");
                openDetailFilmFragment(filmTitle);
            }
        });
        imageView18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "Spiderman";
                Log.d("HomeFragment", "Clicked on Spiderman");
                openDetailFilmFragment(filmTitle);
            }
        });
        imageView19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = "The Marvels";
                Log.d("HomeFragment", "Clicked on The Marvels");
                openDetailFilmFragment(filmTitle);
            }
        });
        return rootView;
    }

    private void openDetailFilmFragment(String filmTitle) {
        DetailFilmFragment detailFragment = DetailFilmFragment.newInstance(filmTitle);
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
