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

public class ReviewAdapter extends FirebaseListAdapter<Review, ReviewAdapter.ReviewViewHolder> {
    public ReviewAdapter(List<Review> reviews) {
        super(reviews);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_review, null);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = get(position);
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
        } else {
            holder.profilePicture.setImageResource(R.drawable.app_im_profile_picture);
        }
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
