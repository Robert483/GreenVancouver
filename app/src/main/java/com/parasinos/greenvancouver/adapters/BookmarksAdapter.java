package com.parasinos.greenvancouver.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.parasinos.greenvancouver.Bookmark;
import com.parasinos.greenvancouver.R;

import java.util.List;

public class BookmarksAdapter extends ArrayAdapter<Bookmark> {
    private Activity context;
    private List<Bookmark> bookmarksList;

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

        Bookmark bookmark = bookmarksList.get(position);
        tvName.setText(bookmark.getName());
        tvAddress.setText(bookmark.getAddress());

        String name = bookmark.getName();
        Log.d("NAME", name);


        return listViewItem;
    }
}
