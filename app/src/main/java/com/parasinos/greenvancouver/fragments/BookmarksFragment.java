package com.parasinos.greenvancouver.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.parasinos.greenvancouver.LoginActivity;
import com.parasinos.greenvancouver.MainActivity;
import com.parasinos.greenvancouver.R;

public class BookmarksFragment extends Fragment {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final FirebaseUser user = mAuth.getCurrentUser();
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        final TextView tvWarning = view.findViewById(R.id.bookmarks_warning);
        if (user != null) {
            Toast.makeText(getContext(), "LOGGED IN", Toast.LENGTH_SHORT).show();
            tvWarning.setVisibility(View.GONE);
        } else {
            tvWarning.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Not Logged In", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}