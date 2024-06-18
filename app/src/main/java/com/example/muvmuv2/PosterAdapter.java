package com.example.muvmuv2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {
    private List<Poster> posterList;

    public PosterAdapter(List<Poster> posterList) {
        this.posterList = posterList;
    }

    @NonNull
    @Override
    public PosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poster, parent, false);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PosterViewHolder holder, int position) {
        Poster poster = posterList.get(position);
        holder.bind(poster);
    }

    @Override
    public int getItemCount() {
        return posterList.size();
    }

    public static class PosterViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView titleTextView;
        private TextView directorTextView;
        private TextView yearTextView;

        public PosterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagePoster);
            titleTextView = itemView.findViewById(R.id.Title);
            directorTextView = itemView.findViewById(R.id.Direct);
            yearTextView = itemView.findViewById(R.id.year);
        }

        public void bind(Poster poster) {
            Glide.with(itemView.getContext()).load(poster.getImageUrl()).into(imageView);
            titleTextView.setText(poster.getTitle());
            directorTextView.setText(poster.getDirector());
            yearTextView.setText(poster.getYear());
        }
    }
}