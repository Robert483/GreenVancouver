package com.parasinos.greenvancouver.adapters;

import com.parasinos.greenvancouver.fragments.ReviewsFragment;
import com.parasinos.greenvancouver.fragments.VolunteerFragment;

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
        switch (position) {
            case 0:
                return VolunteerFragment.newInstance("test", "test");
            case 1:
                return ReviewsFragment.newInstance("test", "test");
        }
        return VolunteerFragment.newInstance("test", "test");
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
