package com.example.test.ppobo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import users.User;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CareReceiverMainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    User self;
    private String addressLocation;
    private GeoPoint geoPoint;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.care_receiver_main_activity);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ActivityCompat.requestPermissions(CareReceiverMainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db.collection("Users").document(mAuth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    self = new User(documentSnapshot.getString("name"),documentSnapshot.getString("phoneNum"),documentSnapshot.getString("email"),documentSnapshot.getString("userType"));
                }
            }
        });


        findViewById(R.id.panic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Users").document(mAuth.getCurrentUser().getEmail()).collection("people").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String message = getResources().getString(R.string.help_msg);

                                        SmsManager mySmsManager = SmsManager.getDefault();
                                        mySmsManager.sendTextMessage(document.getString("phoneNum"), null, message, null, null);

                                        getLocation();

                                        HashMap<String,String> data = new HashMap<>();
                                        data.put("name",self.getName());
                                        data.put("phoneNum",self.getPhoneNum());
                                        data.put("location",addressLocation);

                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                                        db.collection("Users").document(document.getString("email")).collection("alerts").document(simpleDateFormat.format(new Date())).set(data);

                                        Intent changeScreen = new Intent(CareReceiverMainActivity.this, PostHelpRedirect.class);
                                        startActivity(changeScreen);
                                    }
                                }
                            }
                        });
            }
        });

        findViewById(R.id.settings).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(CareReceiverMainActivity.this, CareReceiverSettings.class);
                startActivity(intent);
                return false;
            }
        });
    }

    private void getLocation(){
        if (ContextCompat.checkSelfPermission(CareReceiverMainActivity.this , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(CareReceiverMainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            createLocationRequest();
            // Gets GPS Coordinates
            mFusedLocationClient.getLastLocation().addOnSuccessListener(CareReceiverMainActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                        Geocoder geoCoder = new Geocoder(CareReceiverMainActivity.this, Locale.getDefault()); //it is Geocoder
                        String errorMessage = "";
                        List<Address> addresses = null;

                        try {
                            addresses = geoCoder.getFromLocation(
                                    location.getLatitude(),
                                    location.getLongitude(),
                                    // In this sample, get just a single address.
                                    1);
                        } catch (IOException ioException) {
                            // Catch network or other I/O problems.
                        } catch (IllegalArgumentException illegalArgumentException) {

                        }

                        // Handle case where no address was found.
                        if (addresses == null || addresses.size() == 0) {
                            if (errorMessage.isEmpty()) {
                                addressLocation = "No location found";
                            }
                        } else {
                            for (int i = 0; addresses.get(0).getAddressLine(i) != null; i++){
                                addressLocation = addressLocation + addresses.get(0).getAddressLine(i) + ", ";
                            }
                            addressLocation = addressLocation + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea();//+", "+addresses.get(0).getCountryName();
                        }

                    }

                }
            });
        }
    }

    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(CareReceiverMainActivity.this,
                                9002);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }
}
