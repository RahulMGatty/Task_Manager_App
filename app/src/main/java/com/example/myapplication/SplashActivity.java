package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Make sure this layout exists

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        ImageView logo = findViewById(R.id.imageView2);
        TextView appName = findViewById(R.id.textAppName);

        // Load animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        // Check if the user is logged in after splash screen
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null) {
                // ✅ User is signed in → Go to HomeActivity
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            } else {
                // 🚫 Not signed in → Go to LoginActivity
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }

            finish(); // close splash activity
        }, SPLASH_DELAY); // This is the splash duration (2 seconds)
    }
}
