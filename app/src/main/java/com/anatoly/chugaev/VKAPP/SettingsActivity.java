package com.anatoly.chugaev.VKAPP;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button start = findViewById(R.id.start_service);
        Button stop = findViewById(R.id.stop_service);
        ProgressBar progressBar = findViewById(R.id.prog_bar);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MyService.class);
                startService(intent);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(SettingsActivity.this, MyService.class));
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
