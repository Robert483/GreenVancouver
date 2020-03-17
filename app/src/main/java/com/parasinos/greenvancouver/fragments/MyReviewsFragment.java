package com.parasinos.greenvancouver.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parasinos.greenvancouver.R;
import com.parasinos.greenvancouver.adapters.MyReviewAdapter;
import com.parasinos.greenvancouver.models.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyReviewsFragment extends Fragment {
    private boolean isInitialized = false;
    private DatabaseReference reviewsRef;
    private MyReviewAdapter reviewAdapter;
    private TextView viewNoReviews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String path = String.join("/", "users", user.getUid(), "reviews");
            reviewsRef = FirebaseDatabase.getInstance().getReference(path);
        } else {
            String path = String.join("/", "users", "testuser5", "reviews");
            reviewsRef = FirebaseDatabase.getInstance().getReference(path);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_reviews, container, false);
        viewNoReviews = root.findViewById(R.id.txtv_noreviews);

        if (reviewsRef != null) {
            reviewsRef.addChildEventListener(new MyReviewEventListener());
            reviewsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Review> reviews = new ArrayList<>();
                    for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                        Review review = Objects.requireNonNull(reviewSnapshot.getValue(Review.class));
                        review.setKey(reviewSnapshot.getKey());
                        reviews.add(review);
                    }

                    reviewAdapter = new MyReviewAdapter(reviews);
                    if (reviews.size() == 0) {
                        viewNoReviews.setVisibility(View.VISIBLE);
                    }

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
        }

        return root;
    }

    private class MyReviewEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (isInitialized) {
                Review review = Objects.requireNonNull(dataSnapshot.getValue(Review.class));
                review.setKey(dataSnapshot.getKey());

                if (reviewAdapter.getItemCount() == 0) {
                    viewNoReviews.setVisibility(View.GONE);
                }

                reviewAdapter.add(review, s == null ? null : new Review(s));
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Review review = Objects.requireNonNull(dataSnapshot.getValue(Review.class));
            review.setKey(dataSnapshot.getKey());
            reviewAdapter.update(review);
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            reviewAdapter.remove(new Review(dataSnapshot.getKey()));

            if (reviewAdapter.getItemCount() == 0) {
                viewNoReviews.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            reviewAdapter.move(new Review(dataSnapshot.getKey()), s == null ? null : new Review(s));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            // Do nothing
        }
    }
}