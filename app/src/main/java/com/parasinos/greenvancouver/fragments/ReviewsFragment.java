package com.parasinos.greenvancouver.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.parasinos.greenvancouver.misc.CircleTransform;
import com.parasinos.greenvancouver.models.Review;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.BaseRatingBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class ReviewsFragment extends Fragment {
    private static final String ARG_MAP_ID = "mapId";
    private static final String ARG_PROJECT_NAME = "projectName";

    private Review myReview = new Review();
    private Mode currentMode = null;
    private String projectName;
    private float average;
    private boolean isInitialized = false;

    private DatabaseReference basicInfoRef;
    private DatabaseReference reviewRef;
    private DatabaseReference myReviewRef;
    private DatabaseReference overallRatingRef;
    private DatabaseReference reviewsRef;
    private ReviewAdapter reviewAdapter;

    private ImageView imgvProfilePicture;
    private TextView txtvName;
    private BaseRatingBar ratingBarRating;
    private TextView txtvContent;
    private EditText edttxtContent;
    private Button btnSend;
    private Button btnEdit;
    private Button btnSave;
    private ImageButton imgbtnCancel;

    private TextView viewNoReviews;
    private TextView txtvAverage;
    private TextView txtvTotal;
    private BaseRatingBar ratingBarAverage;

    public static ReviewsFragment newInstance(String mapId, String projectName) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MAP_ID, mapId);
        args.putString(ARG_PROJECT_NAME, projectName);
        fragment.setArguments(args);
        return fragment;
    }

    private void initializeMyReview() {
        if (basicInfoRef == null) {
            return;
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edttxtContent.getText().toString();
                int rating = (int) ratingBarRating.getRating();

                myReview.setContent(content);
                myReview.setRating(rating);
                reviewRef.setValue(myReview);

                Review review = new Review();
                review.setContent(content);
                review.setRating(rating);
                review.setProjectName(projectName);
                myReviewRef.setValue(review);

                changeMode(Mode.VIEW);

                Context context = Objects.requireNonNull(getContext());
                InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(context.getSystemService(Context.INPUT_METHOD_SERVICE));
                imm.hideSoftInputFromWindow(edttxtContent.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        };

        btnSend.setOnClickListener(listener);
        btnSave.setOnClickListener(listener);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMode(Mode.EDIT);
            }
        });

        imgbtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMode(Mode.VIEW);
            }
        });

        basicInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Review basicInfo = Objects.requireNonNull(dataSnapshot.getValue(Review.class));
                myReview.setName(basicInfo.getName());
                myReview.setProfilePicture(basicInfo.getProfilePicture());
                updateUserBasicInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Do nothing
            }
        });

        reviewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    changeMode(Mode.NEW);
                    return;
                }

                myReview = Objects.requireNonNull(dataSnapshot.getValue(Review.class));
                if (currentMode != Mode.EDIT) {
                    changeMode(Mode.VIEW);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Do nothing
            }
        });
    }

    private void initializeOtherReviews() {
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
                updateProjectOverallInfo();

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

    private void updateUserBasicInfo() {
        txtvName.setText(myReview.getName());
        if (!TextUtils.isEmpty(myReview.getProfilePicture())) {
            Picasso.get()
                    .load(myReview.getProfilePicture())
                    .resize(100, 0)
                    .noFade()
                    .transform(new CircleTransform())
                    .into(imgvProfilePicture);
        } else {
            imgvProfilePicture.setImageResource(R.drawable.app_im_profile_picture);
        }
    }

    private void updateProjectOverallInfo() {
        txtvAverage.setText(String.format(Locale.getDefault(), "%.1f", average));
        txtvTotal.setText(String.format(Locale.getDefault(), "(%d)", reviewAdapter.getItemCount()));
        ratingBarAverage.setRating(average);
        overallRatingRef.setValue(average);
    }

    private void changeMode(Mode mode) {
        currentMode = mode;
        switch (currentMode) {
            case NEW:
                btnSend.setVisibility(View.VISIBLE);
                btnEdit.setVisibility(View.GONE);
                btnSave.setVisibility(View.GONE);
                imgbtnCancel.setVisibility(View.GONE);
                txtvContent.setVisibility(View.GONE);
                edttxtContent.setVisibility(View.VISIBLE);

                ratingBarRating.setClickable(true);
                ratingBarRating.setScrollable(true);
                ratingBarRating.setClearRatingEnabled(true);
                ratingBarRating.setEmptyDrawableRes(R.drawable.reviews_ic_leafemptyenabled);

                edttxtContent.setText("");
                ratingBarRating.setRating(0);
                break;
            case VIEW:
                btnSend.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.GONE);
                imgbtnCancel.setVisibility(View.GONE);
                txtvContent.setVisibility(View.VISIBLE);
                edttxtContent.setVisibility(View.GONE);

                ratingBarRating.setClickable(false);
                ratingBarRating.setScrollable(false);
                ratingBarRating.setClearRatingEnabled(false);
                ratingBarRating.setEmptyDrawableRes(R.drawable.reviews_ic_leafempty);

                txtvContent.setText(myReview.getContent());
                ratingBarRating.setRating(myReview.getRating());
                break;
            case EDIT:
                btnSend.setVisibility(View.GONE);
                btnEdit.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                imgbtnCancel.setVisibility(View.VISIBLE);
                txtvContent.setVisibility(View.GONE);
                edttxtContent.setVisibility(View.VISIBLE);

                ratingBarRating.setClickable(true);
                ratingBarRating.setScrollable(true);
                ratingBarRating.setClearRatingEnabled(true);
                ratingBarRating.setEmptyDrawableRes(R.drawable.reviews_ic_leafemptyenabled);

                edttxtContent.setText(txtvContent.getText());
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = Objects.requireNonNull(getArguments());
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String mapId = args.getString(ARG_MAP_ID);
        reviewsRef = database.getReference(String.join("/", "projects", mapId, "reviews"));
        overallRatingRef = database.getReference(String.join("/", "projects", mapId, "overallRating"));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            projectName = args.getString(ARG_PROJECT_NAME);
            basicInfoRef = database.getReference(String.join("/", "users", uid, "basicInfo"));
            reviewRef = database.getReference(String.join("/", "projects", mapId, "reviews", uid));
            myReviewRef = database.getReference(String.join("/", "users", uid, "reviews", mapId));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reviews, container, false);

        root.findViewById(basicInfoRef == null ? R.id.txtv_nouser : R.id.linearl_yourreview)
                .setVisibility(View.VISIBLE);

        imgvProfilePicture = root.findViewById(R.id.imgv_profilepicture);
        txtvName = root.findViewById(R.id.txtv_username);
        ratingBarRating = root.findViewById(R.id.ratingbar_rating);
        txtvContent = root.findViewById(R.id.txtv_review);
        edttxtContent = root.findViewById(R.id.edttxt_review);
        btnSend = root.findViewById(R.id.btn_send);
        btnEdit = root.findViewById(R.id.btn_edit);
        btnSave = root.findViewById(R.id.btn_save);
        imgbtnCancel = root.findViewById(R.id.btn_cancel);

        viewNoReviews = root.findViewById(R.id.txtv_noreviews);
        txtvAverage = root.findViewById(R.id.txtv_average);
        txtvTotal = root.findViewById(R.id.txtv_total);
        ratingBarAverage = root.findViewById(R.id.ratingbar_average);

        initializeMyReview();
        initializeOtherReviews();
        return root;
    }

    private enum Mode {
        NEW,
        VIEW,
        EDIT
    }

    private class ReviewEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (isInitialized) {
                Review review = Objects.requireNonNull(dataSnapshot.getValue(Review.class));
                review.setKey(dataSnapshot.getKey());
                reviewAdapter.add(review, s == null ? null : new Review(s));

                int size = reviewAdapter.getItemCount();
                average = (average * (size - 1) + review.getRating()) / size;
                updateProjectOverallInfo();

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
            updateProjectOverallInfo();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            reviewAdapter.remove(new Review(dataSnapshot.getKey()));

            int size = reviewAdapter.getItemCount();
            if (size > 0) {
                long newRating = Objects.requireNonNull((Long) dataSnapshot.child("rating").getValue());
                average = (average * (size + 1) - newRating) / size;
            } else {
                average = 0;
                viewNoReviews.setVisibility(View.VISIBLE);
            }

            updateProjectOverallInfo();
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
