package com.example.myproject.presentationLayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myproject.R;
import com.google.firebase.auth.FirebaseAuth;

public class LauncherActivity extends AppCompatActivity  {
    private TextView appName, loadingText;
    private ProgressBar progressBar;
    int progress;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
//                mCheckCurrentUser();
                if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()) {
                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                    finish();
                } else {
                    intentMethod();
                }
            }
        });
        thread.start();

    }
    public void threadMethod(){
        for (progress = 1; progress<80; progress+=1)
        {

            try {
                Thread.sleep(45);
                progressBar.setProgress(progress);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    public void intentMethod(){
        Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

     void mCheckCurrentUser() {
        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()) {
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            finish();
        } else {
            intentMethod();
        }
    }
}
