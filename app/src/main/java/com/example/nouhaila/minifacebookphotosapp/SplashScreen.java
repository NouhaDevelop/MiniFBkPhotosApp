package com.example.nouhaila.minifacebookphotosapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    private ImageView img;
    private TextView txt;
    private ProgressBar progressBar;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        img = (ImageView) findViewById(R.id.img);
        txt = (TextView) findViewById(R.id.txt);
        progressBar = findViewById(R.id.start_progress);

        //Init Animation
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        txt.startAnimation(myanim);
        img.startAnimation(myanim);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(myanim);

        RunIntent();


    }

    public void RunIntent(){

        final Timer t = new Timer();
        final Intent i = new Intent(this, MainActivity.class);
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {

                counter++;
                progressBar.setProgress(counter);
                if (counter == 50) {
                    startActivity(i);
                    finish();
                }

            }
        };

        t.schedule(tt, 0, 50);
    }
}
