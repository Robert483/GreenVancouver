package com.parasinos.greenvancouver.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parasinos.greenvancouver.R;
import com.parasinos.greenvancouver.models.Review;
import com.willy.ratingbar.BaseRatingBar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyReviewAdapter extends FirebaseListAdapter<Review, MyReviewAdapter.MyReviewViewHolder> {
    public MyReviewAdapter(List<Review> reviews) {
        super(reviews);
    }

    @NonNull
    @Override
    public MyReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_my_review, null);
        return new MyReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReviewViewHolder holder, int position) {
        Review review = get(position);
        holder.projectName.setText(review.getProjectName());
        holder.content.setText(review.getContent());
        holder.rating.setRating(review.getRating());
    }

    static class MyReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView projectName;
        private TextView content;
        private BaseRatingBar rating;

        MyReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            projectName = itemView.findViewById(R.id.txtv_project);
            content = itemView.findViewById(R.id.txtv_review);
            rating = itemView.findViewById(R.id.ratingbar_rating);
        }
    }
}
