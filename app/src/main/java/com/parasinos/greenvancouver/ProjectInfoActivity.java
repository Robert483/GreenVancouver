package com.parasinos.greenvancouver;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.parasinos.greenvancouver.adapters.ProjectInfoAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class ProjectInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);

        String mapID = getIntent().getStringExtra("mapId");
        String projectName = getIntent().getStringExtra("projectName");
        ProjectInfoAdapter adapter = new ProjectInfoAdapter(getSupportFragmentManager(), getLifecycle(), mapID, projectName);

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
