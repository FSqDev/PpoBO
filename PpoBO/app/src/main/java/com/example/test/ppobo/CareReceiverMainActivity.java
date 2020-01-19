package com.example.test.ppobo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;

public class CareReceiverMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.care_receiver_main_activity);

        ActivityCompat.requestPermissions(CareReceiverMainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        findViewById(R.id.panic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = getResources().getString(R.string.help_msg);

                SmsManager mySmsManager = SmsManager.getDefault();
                mySmsManager.sendTextMessage("6399150588", null, message, null, null);
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
}
