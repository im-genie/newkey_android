package com.example.newkey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class notification2 extends AppCompatActivity {

    private ArrayList<AlrimData> arrayList;
    private AlrimAdapter alrimAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification2);

        //뒤로가기 버튼
        ImageButton button_back = findViewById(R.id.button_back);

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), notification1.class);
                startActivity(intent);
            }
        });

        //전체삭제 버튼
        Button btn_delete = findViewById(R.id.btn_delete);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 알림 값 전체 삭제하는 코드 추가 */
            }
        });

        //리사이클러뷰
        recyclerView = (RecyclerView) findViewById(R.id.rv_noti);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        alrimAdapter = new AlrimAdapter(arrayList);
        recyclerView.setAdapter(alrimAdapter);


    }
}