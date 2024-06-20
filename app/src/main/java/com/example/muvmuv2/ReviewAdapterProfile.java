package com.example.muvmuv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapterProfile extends RecyclerView.Adapter<ReviewAdapterProfile.ViewHolder> {
    private Context context;
    private List<ReviewProfile> itemList;

    public ReviewAdapterProfile(Context context, List<ReviewProfile> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReviewProfile item = itemList.get(position);

        holder.username.setText(item.getUsername());
        holder.email.setText(item.getEmail());
        holder.title.setText(item.getFilmTitle());
        holder.isiReview.setText(item.getContent());

        Glide.with(context)
                .load(item.getPhotoProfile())
                .placeholder(R.drawable.fotoprofile)
                .circleCrop()
                .into(holder.fotoProfile);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, email, title, rate, isiReview;
        CircleImageView fotoProfile;

        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.email);
            title = itemView.findViewById(R.id.title);
            rate = itemView.findViewById(R.id.rate);
            isiReview = itemView.findViewById(R.id.isiReview);
            fotoProfile = itemView.findViewById(R.id.fotoprofile);
        }
    }
}
