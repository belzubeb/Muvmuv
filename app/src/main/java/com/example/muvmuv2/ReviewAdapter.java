package com.example.muvmuv2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private static final int TYPE_ITEM_ONE = 1;
    private static final int TYPE_ITEM_TWO = 2;


    private List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profilePhotoImageView;
        private TextView usernameTextView;
        private TextView emailTextView;
        private TextView titleTextView;
        private TextView rateTextView;
        private TextView isiReviewTextView;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePhotoImageView = itemView.findViewById(R.id.fotoprofile);
            usernameTextView = itemView.findViewById(R.id.username);
            emailTextView = itemView.findViewById(R.id.email);
            titleTextView = itemView.findViewById(R.id.title);
            rateTextView = itemView.findViewById(R.id.rate);
            isiReviewTextView = itemView.findViewById(R.id.isiReview);
        }

        public void bind(Review review) {
            usernameTextView.setText(review.getUsername());
            emailTextView.setText(review.getEmail());
            titleTextView.setText(review.getFilmTitle());

            String rateString = String.format("%.1f", review.getRate());
            rateTextView.setText(rateString);

            isiReviewTextView.setText(review.getContent());

            Glide.with(itemView.getContext())
                    .load(review.getProfilePhoto())
                    .into(profilePhotoImageView);
        }
    }
}
