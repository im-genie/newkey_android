package com.example.newkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogoActivity extends AppCompatActivity {
    private Button login,register;
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    long userIdx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        preferences=getApplicationContext().getSharedPreferences(preference, Context.MODE_PRIVATE);
        userIdx=preferences.getLong("userIdx", 0);

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
    }
}