package com.parasinos.greenvancouver.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parasinos.greenvancouver.R;
import com.parasinos.greenvancouver.adapters.ReviewAdapter;
import com.parasinos.greenvancouver.models.Review;
import com.willy.ratingbar.BaseRatingBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class ReviewsFragment extends Fragment {
    private static final String ARG_MAP_ID = "mapId";

    private float average;
    private boolean isInitialized = false;

    private DatabaseReference reviewsRef;
    private DatabaseReference myReviewRef;
    private ReviewAdapter reviewAdapter;

    private View viewNoReviews;
    private TextView txtvAverage;
    private TextView txtvTotal;
    private BaseRatingBar ratingBarAverage;

    private void updateOverallInfo() {
        txtvAverage.setText(String.format(Locale.getDefault(), "%.1f", average));
        txtvTotal.setText(String.format(Locale.getDefault(), "(%d)", reviewAdapter.getItemCount()));
        ratingBarAverage.setRating(average);
    }

    public static ReviewsFragment newInstance(String mapId) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MAP_ID, mapId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            return;
        }

        String mapId = getArguments().getString(ARG_MAP_ID);
        String path = String.join("/", "projects", mapId, "reviews");
        reviewsRef = FirebaseDatabase.getInstance().getReference(path);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            path = String.join("/", "users", uid, mapId);
            myReviewRef = FirebaseDatabase.getInstance().getReference(path);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reviews, container, false);

        View view = root.findViewById(myReviewRef == null ? R.id.txtv_nouser : R.id.linearl_yourreview);
        view.setVisibility(View.VISIBLE);

        viewNoReviews = root.findViewById(R.id.txtv_noreviews);
        txtvAverage = root.findViewById(R.id.txtv_average);
        txtvTotal = root.findViewById(R.id.txtv_total);
        ratingBarAverage = root.findViewById(R.id.ratingbar_average);

        reviewsRef.addChildEventListener(new ReviewEventListener());
        reviewsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Review> reviews = new ArrayList<>();
                for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                    Review review = Objects.requireNonNull(reviewSnapshot.getValue(Review.class));
                    review.setKey(reviewSnapshot.getKey());
                    reviews.add(review);
                    average += review.getRating();
                }

                reviewAdapter = new ReviewAdapter(reviews);
                if (reviews.size() > 0) {
                    average /= reviews.size();
                } else {
                    average = 0;
                    viewNoReviews.setVisibility(View.VISIBLE);
                }
                updateOverallInfo();

                RecyclerView recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.ryclerv_reviews);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(reviewAdapter);

                isInitialized = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Do nothing
            }
        });
        return root;
    }

    private class ReviewEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (isInitialized) {
                Review review = Objects.requireNonNull(dataSnapshot.getValue(Review.class));
                review.setKey(dataSnapshot.getKey());
                reviewAdapter.add(review);

                int size = reviewAdapter.getItemCount();
                average = (average * (size - 1) + review.getRating()) / size;
                updateOverallInfo();

                if (size - 1 == 0) {
                    viewNoReviews.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Review review = Objects.requireNonNull(dataSnapshot.getValue(Review.class));
            review.setKey(dataSnapshot.getKey());
            Review old = reviewAdapter.update(review);

            int size = reviewAdapter.getItemCount();
            average = (average * size - old.getRating() + review.getRating()) / size;
            updateOverallInfo();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            reviewAdapter.remove(dataSnapshot.getKey());

            int size = reviewAdapter.getItemCount();
            if (size > 0) {
                long newRating = Objects.requireNonNull((Long) dataSnapshot.child("rating").getValue());
                average = (average * (size + 1) - newRating) / size;
            } else {
                average = 0;
                viewNoReviews.setVisibility(View.VISIBLE);
            }

            updateOverallInfo();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            reviewAdapter.move(dataSnapshot.getKey(), s);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            // Do nothing
        }
    }
}
