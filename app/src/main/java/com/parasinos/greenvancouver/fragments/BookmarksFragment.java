package com.parasinos.greenvancouver.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parasinos.greenvancouver.Bookmark;
import com.parasinos.greenvancouver.LoginActivity;
import com.parasinos.greenvancouver.MainActivity;
import com.parasinos.greenvancouver.R;
import com.parasinos.greenvancouver.adapters.BookmarksAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookmarksFragment extends Fragment {
    ListView lvBookmarks;
    List<Bookmark> bookmarkList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseBookmarks = FirebaseDatabase.getInstance().getReference("users");
    FirebaseUser user = mAuth.getCurrentUser();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        final TextView tvWarning = view.findViewById(R.id.bookmarks_warning);
        bookmarkList = new ArrayList<Bookmark>();
        lvBookmarks = view.findViewById(R.id.bookmarks_lv);

        if (user != null) {
            tvWarning.setVisibility(View.GONE);

        } else {
            tvWarning.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (user != null) {

            databaseBookmarks.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    bookmarkList.clear();
                    DataSnapshot userBookmark = dataSnapshot.child(user.getUid()).child("bookmarks");

                    for (DataSnapshot bookmarksSnapshot : userBookmark.getChildren()) {
                        Log.d("USER", bookmarksSnapshot.getKey());
                        String name = bookmarksSnapshot.child("name").getValue().toString();
                        String address = bookmarksSnapshot.child("address").getValue().toString();
                        Bookmark bookmark = new Bookmark(address, name);
                        bookmarkList.add(bookmark);
                    }

                    if (!bookmarkList.isEmpty()) {
                        BookmarksAdapter adapter = new BookmarksAdapter(getActivity(), bookmarkList);
                        lvBookmarks.setAdapter(adapter);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }
}