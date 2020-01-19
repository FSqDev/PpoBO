package com.example.test.ppobo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


public class PostHelpRedirect extends AppCompatActivity {

    Button emergency;
    Button emergencyPrep;
    Button poisonControl;
    Button dementiaAdvice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_help_redirect);

        emergency = findViewById(R.id.emergency);
        emergencyPrep = findViewById(R.id.emergency_management);
        poisonControl = findViewById(R.id.poison);
        dementiaAdvice = findViewById(R.id.dementia_advice);
    }

    public void onDialPoison(View v){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:18003321414"));
        startActivity(intent);

    }

    public void onDialEmergency(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:911"));
        startActivity(intent);

    }

    public void onDialEmergencyManagement(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:18666182362"));
        startActivity(intent);

    }

    public void onDialDementiaAdvice(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:811"));
        startActivity(intent);


    }

}