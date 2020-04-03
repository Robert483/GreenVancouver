package com.parasinos.greenvancouver.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parasinos.greenvancouver.Bookmark;
import com.parasinos.greenvancouver.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class BookmarksAdapter extends ArrayAdapter<Bookmark> {
    private Activity context;
    private List<Bookmark> bookmarksList;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference("projects");
    private String imgUrl = "";

    public BookmarksAdapter(FragmentActivity context, List<Bookmark> bookmarksList) {
        super(context, R.layout.bookmarks_list_layout, bookmarksList);
        this.context = context;
        this.bookmarksList = bookmarksList;
    }

    public BookmarksAdapter(Context context, int resource, List<Bookmark> objects,
                            Activity context1, List<Bookmark> bookmarksList) {
        super(context, resource, objects);
        this.context = context1;
        this.bookmarksList = bookmarksList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.bookmarks_list_layout, null, true);

        TextView tvName = listViewItem.findViewById(R.id.bookmarks_list_project);
        TextView tvAddress = listViewItem.findViewById(R.id.bookmarks_list_project_address);
        final ImageView ivPhoto = listViewItem.findViewById(R.id.bookmarks_list_image);

        Bookmark bookmark = bookmarksList.get(position);

        // get image
        final String mapId = bookmark.getMapid();

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {;

                for (DataSnapshot map : dataSnapshot.getChildren()) {
                    if (map.getKey().equals(mapId)) {
                        imgUrl = map.child("images").child("0").getValue().toString();
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        new DownloadImageTask(ivPhoto)
                                .execute(imgUrl);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        tvName.setText(bookmark.getName());
        tvAddress.setText(bookmark.getAddress());

        return listViewItem;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}
