package com.parasinos.greenvancouver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.parasinos.greenvancouver.adapters.ProjectInfoAdapter;

public class ProjectInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);

        ProjectInfoAdapter adapter = new ProjectInfoAdapter(getSupportFragmentManager(), getLifecycle());

        final ViewPager2 viewPager = findViewById(R.id.vpager_info);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);

        TabLayout tabLayout = findViewById(R.id.tabl_info);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Nothing
            }
        });
    }
}
