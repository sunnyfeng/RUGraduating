package com.sunnyfeng.rugraduating;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddClassesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }
}