package com.example.test.ppobo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CareReceiverMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.care_receiver_main_activity);

        /*findViewById(R.id.panic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // stuff
            }
        });*/

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
