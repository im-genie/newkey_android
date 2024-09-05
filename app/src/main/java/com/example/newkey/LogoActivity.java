package com.example.newkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class LogoActivity extends AppCompatActivity {
    Button login;
    TextView register;
    SharedPreferences preferences;
    public static final String preference = "newkey";
    long userIdx = 0;

    private LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        preferences=getApplicationContext().getSharedPreferences(preference, Context.MODE_PRIVATE);
        userIdx=preferences.getLong("userIdx", 0);

        login.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.key_green_400));

        // 로그인 상태인 경우, 메인 액티비티로 이동
        if (userIdx != 0) {
            startActivity(new Intent(this, MainActivity.class));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConsentActivity.class);
                startActivity(intent);
            }
        });

        // 애니메이션
        animationView = findViewById(R.id.animationView);

        // res/raw 디렉터리에 있는 JSON 파일 로드
        animationView.setAnimation(R.raw.newkey_loading);

        // 스피트 조정
        animationView.setSpeed(0.65f);

        // 애니메이션 재생
        animationView.playAnimation();
    }

    private boolean backPressedOnce = false;
    private Toast backToast;

    @Override
    public void onBackPressed() {
        if (backPressedOnce) {
            if (backToast != null) {
                backToast.cancel();
            }
            super.onBackPressed();
            return;
        }

        this.backPressedOnce = true;
        backToast = Toast.makeText(this, "한 번 더 클릭 시, 어플이 종료됩니다.", Toast.LENGTH_SHORT);
        backToast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressedOnce = false;
            }
        }, 3000); // 3초
    }


}