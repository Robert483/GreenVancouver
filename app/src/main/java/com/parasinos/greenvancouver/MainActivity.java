package com.parasinos.greenvancouver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_header);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawerlo_container);
        NavigationView navigationView = findViewById(R.id.navview_menu);

        View headerView = navigationView.getHeaderView(0);

        mAuth = FirebaseAuth.getInstance();

        Button loginBtn = headerView.findViewById(R.id.main_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });


        Button logoutBtn = headerView.findViewById(R.id.main_logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), "LOGGED OUT", Toast.LENGTH_SHORT).show();
                onStart();
            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.item_map, R.id.item_bookmarks, R.id.item_myreviews,
                R.id.item_settings, R.id.item_share, R.id.item_send)
                .setDrawerLayout(drawer)
                .build();
        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragcontainerv_host);
        if (navHost == null) {
            return;
        }

        NavController navController = navHost.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragcontainerv_host);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        NavigationView navigationView = findViewById(R.id.navview_menu);

        View headerView = navigationView.getHeaderView(0);

        mAuth = FirebaseAuth.getInstance();

        Button loginBtn = headerView.findViewById(R.id.main_login);
        Button logoutBtn = headerView.findViewById(R.id.main_logout);

        if(currentUser == null){
            loginBtn.setVisibility(View.VISIBLE);
            logoutBtn.setVisibility(View.GONE);
        }else{
            loginBtn.setVisibility(View.GONE);
            logoutBtn.setVisibility(View.VISIBLE);
        }
    }
}
