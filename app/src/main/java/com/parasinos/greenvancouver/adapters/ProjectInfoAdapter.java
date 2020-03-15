package com.parasinos.greenvancouver.adapters;

import com.parasinos.greenvancouver.fragments.ReviewsFragment;
import com.parasinos.greenvancouver.fragments.VolunteerFragment;

import java.security.InvalidParameterException;

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
            case 1:
                return ReviewsFragment.newInstance("testmapId1");
            case 2:
                return VolunteerFragment.newInstance();
            default:
                throw new InvalidParameterException("Position is not valid");
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
