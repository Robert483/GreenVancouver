package com.parasinos.greenvancouver.adapters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parasinos.greenvancouver.R;
import com.parasinos.greenvancouver.models.Bookmark;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public class BookmarksAdapter extends ArrayAdapter<Bookmark> {
    private Activity context;
    private List<Bookmark> bookmarksList;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    public BookmarksAdapter(FragmentActivity context, List<Bookmark> bookmarksList) {
        super(context, R.layout.item_bookmark, bookmarksList);
        this.context = context;
        this.bookmarksList = bookmarksList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listViewItem;
        if (convertView == null) {
            listViewItem = View.inflate(context, R.layout.item_bookmark, null);
        } else {
            listViewItem = convertView;
        }

        TextView tvName = listViewItem.findViewById(R.id.bookmarks_list_project);
        TextView tvAddress = listViewItem.findViewById(R.id.bookmarks_list_project_address);
        final ImageView ivPhoto = listViewItem.findViewById(R.id.bookmarks_list_image);

        final Bookmark bookmark = bookmarksList.get(position);
        tvName.setText(bookmark.getName());
        tvAddress.setText(bookmark.getAddress());

        if (bookmark.getImage() != null) {
            ivPhoto.setImageDrawable(bookmark.getImage());
            return listViewItem;
        }

        // get image
        final String path = String.join("/", "projects", bookmark.getMapid(), "images", "0");

        database.getReference(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imgUrl = (String) dataSnapshot.getValue();
                if (TextUtils.isEmpty(imgUrl)) {
                    return;
                }

                Picasso.get()
                        .load(imgUrl)
                        .resize(300, 0)
                        .noFade()
                        .into(ivPhoto, new Callback() {
                            @Override
                            public void onSuccess() {
                                bookmark.setImage(ivPhoto.getDrawable());
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Do nothing
            }
        });
        ivPhoto.setImageResource(R.drawable.details_im_placeholder);

        return listViewItem;
    }
}
