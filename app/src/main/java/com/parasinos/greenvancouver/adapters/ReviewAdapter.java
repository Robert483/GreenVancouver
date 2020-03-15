package com.parasinos.greenvancouver.adapters;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parasinos.greenvancouver.R;
import com.parasinos.greenvancouver.misc.CircleTransform;
import com.parasinos.greenvancouver.models.Review;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.BaseRatingBar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviews;

    public ReviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_review_details, null);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.name.setText(review.getName());
        holder.content.setText(review.getContent());
        holder.rating.setRating(review.getRating());

        if (!TextUtils.isEmpty(review.getProfilePicture())) {
            Picasso.get()
                    .load(review.getProfilePicture())
                    .resize(100, 0)
                    .noFade()
                    .transform(new CircleTransform())
                    .into(holder.profilePicture);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void add(Review review) {
        int pos = reviews.size();
        reviews.add(review);
        notifyItemInserted(pos);
    }

    public Review update(Review review) {
        int pos = reviews.indexOf(review);
        Review old = reviews.get(pos);
        reviews.set(pos, review);
        notifyItemChanged(pos);
        return old;
    }

    public void remove(String key) {
        Review review = new Review(key);
        int pos = reviews.indexOf(review);
        reviews.remove(review);
        notifyItemRemoved(pos);
    }

    public void move(String key, String previousKey) {
        int oldPos = reviews.indexOf(new Review(key));
        int newPos = previousKey == null ? 0 : reviews.indexOf(new Review(previousKey)) + 1;

        int cur = Math.min(oldPos, newPos);
        int end = Math.max(oldPos, newPos);
        Review review = reviews.get(cur);
        while (cur < end) {
            reviews.set(cur, reviews.get(cur + 1));
            cur++;
        }
        reviews.set(end, review);
        notifyItemMoved(oldPos, newPos);
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView content;
        private ImageView profilePicture;
        private BaseRatingBar rating;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtv_username);
            content = itemView.findViewById(R.id.txtv_review);
            profilePicture = itemView.findViewById(R.id.imgv_profilepicture);
            rating = itemView.findViewById(R.id.ratingbar_rating);
        }
    }
}
