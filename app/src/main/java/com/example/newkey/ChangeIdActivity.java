package com.example.newkey;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ChangeIdActivity extends Activity {
    private EditText editUserId; // EditText를 직접 사용하기 위해 필드로 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_id); // 레이아웃 설정

        // 컴포넌트 초기화
        editUserId = findViewById(R.id.editUserId);
        ImageButton backFromProfile = findViewById(R.id.backFromProfile);
        Button completeButton = findViewById(R.id.completeButton);

        // 이미지 버튼 클릭 이벤트 처리: 화면을 닫고 이전 화면으로 돌아감
        backFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 액티비티를 종료
                finish();
            }
        });

        // 완료 버튼 클릭 이벤트 처리: 사용자 입력을 저장하고 MainActivity로 전달
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText에서 사용자 입력을 가져옴
                String userInput = editUserId.getText().toString().trim(); // 입력값 앞뒤 공백 제거

                // SharedPreferences를 사용하여 사용자 입력 저장
                SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("UserInput", userInput);
                editor.apply();

                // MainActivity로 화면 전환하면서 사용자 입력을 인텐트에 추가하여 전달
                Intent intent = new Intent(ChangeIdActivity.this, MypageActivity.class);
                intent.putExtra("UserInput", userInput);
                startActivity(intent);
            }
        });
    }
}
