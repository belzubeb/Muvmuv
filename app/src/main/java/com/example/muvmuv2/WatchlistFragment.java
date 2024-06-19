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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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

        // Menambahkan ItemTouchHelper untuk menghapus item saat digesek ke samping
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isAdded()) {
                    posterList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        HashMap<String, Object> posterData = (HashMap<String, Object>) snapshot.getValue();
                        if (posterData != null) {
                            Poster poster = new Poster();
                            poster.setTitle((String) posterData.get("title"));
                            poster.setDirector((String) posterData.get("director"));
                            poster.setYear((String) posterData.get("year"));
                            poster.setImageUrl((String) posterData.get("imageUrl"));
                            poster.setKey(key); // Jangan lupa tambahkan key pada poster
                            posterList.add(poster);
                        }
                    }
                    posterAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (isAdded()) {
                    Log.e("WatchlistFragment", "DatabaseError: " + databaseError.getMessage());
                }
            }
        });
    }

    // Menghapus item ketika diswipe
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteItem(viewHolder.getAdapterPosition());
        }
    };

    private void deleteItem(int position) {
        String key = posterList.get(position).getKey();
        if (key == null) {
            Log.e("WatchlistFragment", "Key is null for item at position: " + position);
            return;
        }

        DatabaseReference itemRef = databaseReference.child(key);
        itemRef.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Item berhasil dihapus dari Firebase, lanjutkan menghapus dari RecyclerView
                            posterList.remove(position);
                            posterAdapter.notifyItemRemoved(position);

                            // Hapus item dari Firebase Database
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                                    .child("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("poster")
                                    .child(key);

                            ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (isAdded()) {
                                            Toast.makeText(getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        if (isAdded()) {
                                            Toast.makeText(getContext(), "Failed to delete item", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        } else {
                            Log.e("WatchlistFragment", "Failed to delete item: " + task.getException());
                            if (isAdded()) {
                                Toast.makeText(getContext(), "Failed to delete item", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

}
