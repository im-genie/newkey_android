package com.example.newkey;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MypageActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // TextView 초기화
        TextView textView = findViewById(R.id.profileId); // activity_main.xml 내의 TextView ID가 이와 일치해야 합니다.

        // SharedPreferences에서 사용자 ID 읽기
        SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String userId = preferences.getString("UserInput", "Default ID");

        // TextView에 사용자 ID 표시
        textView.setText(userId);

        // 인텐트에서 사용자 ID를 받는 경우 (옵셔널: 인텐트를 사용하는 경우에만 필요)
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("UserInput")) {
            String userIdFromIntent = intent.getStringExtra("UserInput");
            textView.setText(userIdFromIntent); // 인텐트로부터 받은 값을 사용하여 TextView를 업데이트
        }


        ImageView changeProfile = findViewById(R.id.changeProfile);
        ImageView changeProfileId = findViewById(R.id.changeProfileId);
        Button viewHistory = findViewById(R.id.btn_history);
        Button viewScrap = findViewById(R.id.btn_scrap);
        Button chooseTopics = findViewById(R.id.btn_topic);
        TextView changePassword = findViewById(R.id.changePassword);
        TextView logout = findViewById(R.id.logout);

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        changeProfileId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, ChangeIdActivity.class);
                startActivity(intent);
            }
        });

        viewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, ViewHistoryActivity.class);
                startActivity(intent);
            }
        });

        viewScrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, ViewScrapActivity.class);
                startActivity(intent);
            }
        });

        chooseTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, ChooseTopicsActivity.class);
                startActivity(intent);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, LogoutActivity.class);
                startActivity(intent);
            }
        });
    }
}