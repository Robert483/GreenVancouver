package com.parasinos.greenvancouver;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViewById(R.id.imgv_city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        });

        findViewById(R.id.imgv_car).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, ProjectInfoActivity.class));
            }
        });

        findViewById(R.id.constrl_container).post(new Runnable() {
            @Override
            public void run() {
                ImageView car = findViewById(R.id.imgv_car);
                ProgressBar loading = findViewById(R.id.probar_loading);

                int distance = loading.getWidth() - car.getWidth();
                ObjectAnimator carAnimation = ObjectAnimator.ofFloat(car, "translationX", distance);
                carAnimation.setDuration(1500);
                carAnimation.setStartDelay(500);

                ObjectAnimator loadingAnimation = ObjectAnimator.ofInt(loading, "progress", 100);
                loadingAnimation.setDuration(1500);
                loadingAnimation.setStartDelay(500);
                loadingAnimation.addListener(new LoadingListener());

                carAnimation.start();
                loadingAnimation.start();
            }
        });
    }

    private class LoadingListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {
            // Do nothing
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            Toast.makeText(getApplicationContext(), "TODO: go to main activity.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            // Do nothing
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            // Do nothing
        }
    }
}
