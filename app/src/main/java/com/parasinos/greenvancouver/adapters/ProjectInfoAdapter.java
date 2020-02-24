package com.parasinos.greenvancouver.adapters;

import android.os.Bundle;

import com.parasinos.greenvancouver.ReviewsFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ProjectInfoAdapter extends FragmentStateAdapter {

    public ProjectInfoAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ReviewsFragment.newInstance("test", "test");
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
