package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_uas.MainActivity;
import com.example.project_uas.R;

public class splash_screen extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent mainIntent = new Intent(splash_screen.this, MainActivity.class);
                splash_screen.this.startActivity(mainIntent);
                splash_screen.this.finish();

            }
        },SPLASH_DISPLAY_LENGTH);
    }
}