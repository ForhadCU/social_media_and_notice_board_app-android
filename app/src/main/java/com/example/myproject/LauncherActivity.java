package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LauncherActivity extends AppCompatActivity {
    private TextView appName, loadingText;
    private ProgressBar progressBar;
    int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide Notification Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_launcher);
        //hide title Bar
//        Objects.requireNonNull(getSupportActionBar()).hide();


        progressBar = findViewById(R.id.progressBarId);
        appName = findViewById(R.id.notiflyId);
        loadingText = findViewById(R.id.loadingId);

        //set Animation on AppName
        Animation textAnim = AnimationUtils.loadAnimation(this, R.anim.text_anim);
        appName.startAnimation(textAnim);
        //set Animation on loading
        Animation loadAnim = AnimationUtils.loadAnimation(this, R.anim.loading_anim);
        loadingText.startAnimation(loadAnim);

        //progressBar Thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                threadMethod();
                intentMethod();
            }
        });
        thread.start();

    }
    public void threadMethod(){
        for (progress = 1; progress<100; progress+=1)
        {

            try {
                Thread.sleep(50);
                progressBar.setProgress(progress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    public void intentMethod(){
        Intent intent = new Intent(LauncherActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
