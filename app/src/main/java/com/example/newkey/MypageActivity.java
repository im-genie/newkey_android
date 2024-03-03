package com.example.newkey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MypageActivity extends Activity {

    private SharedPreferences preferences;
    public static final String preference = "newkey";
    Long userIdx;
    String email, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // TextView 초기화
        TextView textView = findViewById(R.id.profileId); // activity_main.xml 내의 TextView ID가 이와 일치해야 합니다.

        preferences=getSharedPreferences(preference, Context.MODE_PRIVATE);
        userIdx=preferences.getLong("userIdx", 0);
        email=preferences.getString("email", "");

        // TextView에 사용자 ID 표시
        textView.setText(email);

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
                Intent intent = new Intent(MypageActivity.this, PwFindActivity1.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, LogoActivity.class);
                startActivity(intent);

                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("userIdx");
                editor.remove("email");
                editor.apply();
            }
        });
    }
}