package com.parasinos.greenvancouver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
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
    private DatabaseReference database;
    private FirebaseUser user;
    private String username;

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
        database = FirebaseDatabase.getInstance().getReference("users");

        Button loginBtn = headerView.findViewById(R.id.main_login);
        Button logoutBtn = headerView.findViewById(R.id.main_logout);
        final TextView tvUsername = headerView.findViewById(R.id.txtv_username);
        final TextView tvEmail = headerView.findViewById(R.id.txtv_email);

        if(currentUser == null){
            loginBtn.setVisibility(View.VISIBLE);
            logoutBtn.setVisibility(View.GONE);
            tvUsername.setText("Not logged in");
            tvEmail.setVisibility(View.GONE);

        }else{
            user = mAuth.getCurrentUser();

            Query nameQuery = database.orderByChild("name");
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        username = ds.child("basicInfo").child("name").getValue().toString();
                        tvUsername.setText(username);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            nameQuery.addListenerForSingleValueEvent(valueEventListener);
            loginBtn.setVisibility(View.GONE);
            logoutBtn.setVisibility(View.VISIBLE);
            tvEmail.setVisibility(View.VISIBLE);
            tvEmail.setText(user.getEmail());


        }
    }
}
