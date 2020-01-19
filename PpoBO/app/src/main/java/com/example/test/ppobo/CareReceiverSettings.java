package com.example.test.ppobo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CareReceiverSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_receiver_settings);

        findViewById(R.id.manage_caretakers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CareReceiverSettings.this,ManageCareTakers.class);
                startActivity(intent);
            }
        });
    }
}
