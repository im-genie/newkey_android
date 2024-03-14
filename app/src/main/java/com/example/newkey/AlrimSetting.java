package com.example.newkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

public class AlrimSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alrim_setting);

        //뒤로가기 버튼
        ImageButton button_back = findViewById(R.id.button_back);

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), notification1.class);
                startActivity(intent);
            }
        });

        // 스위치
        Switch switch1 = findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switch1.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.key_green_300)));
                } else {
                    switch1.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_600)));
                }
            }
        });

    }
}