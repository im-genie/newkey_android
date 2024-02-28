package com.example.newkey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        RecyclerView recyclerView = findViewById(R.id.myNewsRecyclerView); // XML 파일 내의 RecyclerView ID로 변경하세요
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // LinearLayoutManager 사용

        // 뉴스 제목 목록 생성 (예제 데이터)
        List<String> newsTitles = new ArrayList<>();
        newsTitles.add("뉴스 제목 1");
        newsTitles.add("뉴스 제목 2");
        newsTitles.add("뉴스 제목 3");
        // ... 여기에 더 많은 뉴스 제목을 추가할 수 있습니다.

        // 어댑터 생성 및 설정
        NewsAdapter adapter = new NewsAdapter(newsTitles);
        recyclerView.setAdapter(adapter);

        LinearLayout backFromScrap = findViewById(R.id.back_from_history);

        backFromScrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity로 돌아가기
                Intent intent = new Intent(ViewHistoryActivity.this, MypageActivity.class);
                // MainActivity를 새 태스크로 시작하고, 이전에 있던 액티비티들을 모두 제거
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // 현재 액티비티 종료
            }
        });
    }
}
