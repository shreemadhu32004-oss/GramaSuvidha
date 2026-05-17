package com.gramasuvidha.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.gramasuvidha.app.R;
import com.gramasuvidha.app.repository.AuthRepository;
import com.gramasuvidha.app.ui.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView tvAppName = findViewById(R.id.tv_app_name);
        TextView tvTagline = findViewById(R.id.tv_tagline);

        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(1200);
        fadeIn.setFillAfter(true);
        tvAppName.startAnimation(fadeIn);

        AlphaAnimation fadeIn2 = new AlphaAnimation(0f, 1f);
        fadeIn2.setDuration(1200);
        fadeIn2.setStartOffset(400);
        fadeIn2.setFillAfter(true);
        tvTagline.startAnimation(fadeIn2);

        AuthRepository authRepo = new AuthRepository(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (authRepo.isLoggedIn()) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        }, 2500);
    }
}
