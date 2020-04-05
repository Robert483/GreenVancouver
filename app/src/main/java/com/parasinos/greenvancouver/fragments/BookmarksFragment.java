package com.parasinos.greenvancouver.fragments;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parasinos.greenvancouver.R;
import com.parasinos.greenvancouver.adapters.BookmarksAdapter;
import com.parasinos.greenvancouver.models.Bookmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class BookmarksFragment extends Fragment {
    private ListView lvBookmarks;
    private List<Bookmark> bookmarkList;
    private ArrayList<Bookmark> toDelete;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseBookmarks;
    private FirebaseUser user = mAuth.getCurrentUser();
    private BookmarksAdapter adapter;
    private ActionMode actionMode = null;

    private TextView txtvMessage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        txtvMessage = view.findViewById(R.id.txtv_message);
        bookmarkList = new ArrayList<>();
        lvBookmarks = view.findViewById(R.id.bookmarks_lv);
        lvBookmarks.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvBookmarks.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    toDelete.add(adapter.getItem(position));
                } else {
                    toDelete.remove(adapter.getItem(position));
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.cab_bookmarks, menu);
                actionMode = mode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.action_delete) {
                    for (Bookmark i : toDelete) {
                        adapter.remove(i);
                        databaseBookmarks.child(i.getMapid()).removeValue();
                    }
                    adapter.notifyDataSetChanged();
                    mode.finish();
                    return true;
                }

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
            }
        });

        if (user != null) {
            txtvMessage.setText(R.string.bookmarks_nobookmarks);
            String path = String.join("/", "users", user.getUid(), "bookmarks");
            databaseBookmarks = FirebaseDatabase.getInstance().getReference(path);
        } else {
            txtvMessage.setText(R.string.bookmarks_warning);
            txtvMessage.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        toDelete = new ArrayList<>();
        if (user != null) {
            databaseBookmarks.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    bookmarkList.clear();
                    for (DataSnapshot bookmarkSnapshot : dataSnapshot.getChildren()) {
                        if (!Objects.requireNonNull(bookmarkSnapshot.getKey()).equals("filler")) {
                            Bookmark bookmark = bookmarkSnapshot.getValue(Bookmark.class);
                            bookmarkList.add(bookmark);
                        }
                    }

                    if (!bookmarkList.isEmpty()) {
                        adapter = new BookmarksAdapter(getActivity(), bookmarkList);
                        lvBookmarks.setAdapter(adapter);
                        txtvMessage.setVisibility(View.GONE);
                    } else {
                        txtvMessage.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Do nothing
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Destroy action mode
        if (actionMode != null)
            actionMode.finish();
    }
}
