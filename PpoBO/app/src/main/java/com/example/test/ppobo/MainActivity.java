package com.example.test.ppobo;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.radar.sdk.Radar;
import io.radar.sdk.RadarTrackingOptions;
import io.radar.sdk.model.RadarEvent;
import io.radar.sdk.model.RadarUser;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FloatingActionButton fab;
    public int requestCode = 1;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_maps,R.id.nav_alerts,R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Radar.initialize("publishableKey");
        Radar.setUserId("UserID");
        Radar.setDescription("Description");

        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, requestCode);
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION }, requestCode);
        Radar.trackOnce(new Radar.RadarCallback() {
            @Override
            public void onComplete(@NotNull Radar.RadarStatus radarStatus, @Nullable Location location, @Nullable RadarEvent[] radarEvents, @Nullable RadarUser radarUser) {

            }
        });
        Radar.startTracking();

        RadarTrackingOptions trackingOptions = new RadarTrackingOptions.Builder()
                .priority(Radar.RadarTrackingPriority.RESPONSIVENESS) // use EFFICIENCY instead to reduce location update frequency
                .offline(Radar.RadarTrackingOffline.REPLAY_STOPPED) // use REPLAY_OFF instead to disable offline replay
                .sync(Radar.RadarTrackingSync.POSSIBLE_STATE_CHANGES) // use ALL instead to sync all location updates
                .build();

        Radar.startTracking(trackingOptions);


    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onStart(){
        super.onStart();
        if (mAuth.getCurrentUser() == null){
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        } else if (mAuth.getCurrentUser() != null){
            db.collection("Users").document(mAuth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null){
                        if (documentSnapshot.getString("userType").equals("Care Receiver")){
                            Intent intent = new Intent(MainActivity.this,CareReceiverMainActivity.class);
                            startActivity(intent);
                        }
                    }

                }
            });
        }
    }

    public void showFloatingActionButton() {
        fab.show();
    }

    public void hideFloatingActionButton() {
        fab.hide();
    }
}
