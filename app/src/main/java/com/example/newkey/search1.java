package com.example.newkey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class search1 extends AppCompatActivity {

    // 뒤로가기 버튼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search1);

        ImageButton button_back = findViewById(R.id.button_back);

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // 검색창
        EditText search = findViewById(R.id.et_search);
            /* 검색 기능 구현 코드 추가 */


        // 전체삭제 버튼
        Button btn_delete = findViewById(R.id.btn_delete);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 알림 값 전체 삭제하는 코드 추가 */
            }
        });
    }
}