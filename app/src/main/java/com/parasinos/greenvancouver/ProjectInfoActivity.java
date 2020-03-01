package com.parasinos.greenvancouver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            private final String[] TITLES = getResources().getStringArray(R.array.projectinfo_tabs);

            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(TITLES[position]);
                viewPager.setCurrentItem(position);
            }
        }).attach();
    }
}
