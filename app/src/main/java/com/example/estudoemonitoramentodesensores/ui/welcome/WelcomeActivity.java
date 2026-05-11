package com.example.estudoemonitoramentodesensores.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.estudoemonitoramentodesensores.R;
import com.example.estudoemonitoramentodesensores.ui.sensor.list.SensorListActivity;

public class WelcomeActivity extends AppCompatActivity {

    private static final int DELAY_MS = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        LinearLayout content = findViewById(R.id.contentLayout);
        ImageView logo = findViewById(R.id.logo);

        // Fade in content
        content.setAlpha(0f);
        content.animate().alpha(1f).setDuration(1000);

        // Rotation animation for the sensor icon
//        RotateAnimation rotate = new RotateAnimation(
//                0, 360,
//                Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f
//        );
//        rotate.setDuration(2000);
//        rotate.setRepeatCount(Animation.INFINITE);
//        logo.startAnimation(rotate);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(this, SensorListActivity.class));
            finish();
        }, DELAY_MS);
    }
}