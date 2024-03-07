package com.example.newkey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChooseTopicsActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> myDataset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_topics);

        LinearLayout backFromTopic = findViewById(R.id.back_from_topic);


        recyclerView = findViewById(R.id.categoriesRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        myDataset = new ArrayList<>();
        // 예제 데이터를 리스트에 추가합니다.
        myDataset.add("아이템 1");
        myDataset.add("아이템 2");

        adapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(adapter);
        backFromTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity로 돌아가기
                Intent intent = new Intent(ChooseTopicsActivity.this, MypageActivity.class);
                // MainActivity를 새 태스크로 시작하고, 이전에 있던 액티비티들을 모두 제거
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // 현재 액티비티 종료

            }
        });
    }
}