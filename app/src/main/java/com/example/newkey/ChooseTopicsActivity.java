package com.example.newkey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseTopicsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_topics);

        LinearLayout backFromScrap = findViewById(R.id.back_from_history);

        backFromScrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity로 돌아가기
                Intent intent = new Intent(ChooseTopicsActivity.this, MyActivity.class);
                // MainActivity를 새 태스크로 시작하고, 이전에 있던 액티비티들을 모두 제거
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // 현재 액티비티 종료

            }
        });
    }
}