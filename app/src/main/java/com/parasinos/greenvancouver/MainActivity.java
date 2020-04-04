package com.parasinos.greenvancouver;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parasinos.greenvancouver.misc.CircleTransform;
import com.parasinos.greenvancouver.models.User;
import com.squareup.picasso.Picasso;

import java.util.Objects;

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
    SharedPreferences sharedPreferences;
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private FirebaseUser user;

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

        sharedPreferences = getSharedPreferences("pref", 0);
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
        user = mAuth.getCurrentUser();
        NavigationView navigationView = findViewById(R.id.navview_menu);

        View headerView = navigationView.getHeaderView(0);

        mAuth = FirebaseAuth.getInstance();

        Button loginBtn = headerView.findViewById(R.id.main_login);
        Button logoutBtn = headerView.findViewById(R.id.main_logout);
        final TextView tvUsername = headerView.findViewById(R.id.txtv_username);
        final TextView tvEmail = headerView.findViewById(R.id.txtv_email);
        final ImageView profilePic = headerView.findViewById(R.id.imgv_profilepicture);

        if (user == null) {
            loginBtn.setVisibility(View.VISIBLE);
            logoutBtn.setVisibility(View.GONE);
            tvUsername.setText("");
            tvEmail.setVisibility(View.GONE);
            profilePic.setVisibility(View.INVISIBLE);
        } else {
            String path = String.join("/", "users", user.getUid(), "basicInfo");
            database = FirebaseDatabase.getInstance().getReference(path);

            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = Objects.requireNonNull(dataSnapshot.getValue(User.class));
                    tvUsername.setText(user.getName());
                    if (TextUtils.isEmpty(user.getProfilePicture())) {
                        return;
                    }

                    Picasso.get()
                            .load(user.getProfilePicture())
                            .resize(500, 0)
                            .transform(new CircleTransform())
                            .into(profilePic);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Do nothing
                }
            });
            loginBtn.setVisibility(View.GONE);
            logoutBtn.setVisibility(View.VISIBLE);
            tvEmail.setVisibility(View.VISIBLE);
            tvEmail.setText(user.getEmail());
        }

        ImageButton editPic = headerView.findViewById(R.id.edit_pc_btn);

        editPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create an alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Edit your profile picture");
                // set the custom layout
                final View customLayout = View.inflate(MainActivity.this, R.layout.dialog_profile_editor, null);
                builder.setView(customLayout);
                // create and show the alert dialog
                final AlertDialog dialog = builder.create();
                Button changeBtn = customLayout.findViewById(R.id.profile_change);
                Button cancelBtn = customLayout.findViewById(R.id.profile_cancel);

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                changeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user = mAuth.getCurrentUser();
                        EditText url = customLayout.findViewById(R.id.profile_url);
                        if (!TextUtils.isEmpty(url.getText()) || user != null) {
                            User user = new User(tvUsername.getText().toString(), url.getText().toString());
                            database.setValue(user);
                        } else {
                            Toast.makeText(MainActivity.this, "URL is empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
    }
}
