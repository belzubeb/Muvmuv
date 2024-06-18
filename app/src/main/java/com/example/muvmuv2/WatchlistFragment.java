package com.example.muvmuv2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class WatchlistFragment extends Fragment {

    private RecyclerView recyclerView;
    private PosterAdapter posterAdapter;
    private ArrayList<Poster> posterList;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);

        recyclerView = view.findViewById(R.id.recyclerWatchlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        posterList = new ArrayList<>();
        posterAdapter = new PosterAdapter(posterList);
        recyclerView.setAdapter(posterAdapter);

        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("poster");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posterList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> posterData = (HashMap<String, Object>) snapshot.getValue();
                    if (posterData != null) {
                        Poster poster = new Poster();
                        poster.setTitle((String) posterData.get("title"));
                        poster.setDirector((String) posterData.get("director"));
                        poster.setYear((String) posterData.get("year"));
                        poster.setImageUrl((String) posterData.get("imageUrl"));
                        posterList.add(poster);
                    }
                }
                posterAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("WatchlistFragment", "DatabaseError: " + databaseError.getMessage());
            }
        });

        // Menambahkan ItemTouchHelper untuk menghapus item saat digesek ke samping
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        // Menghapus item saat digesek ke samping
                        int position = viewHolder.getAdapterPosition();
                        deleteItem(position);
                    }
                });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void deleteItem(int position) {
        // Hapus item dari database dan daftar
        String posterKey = posterList.get(position).getKey();
        if (posterKey != null) {
            databaseReference.child(posterKey).removeValue();
            posterList.remove(position);
            posterAdapter.notifyItemRemoved(position);
        } else {
            Log.e("WatchlistFragment", "Poster key is null");
        }
    }

}
