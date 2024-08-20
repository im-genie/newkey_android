package com.example.newkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AlrimSetting extends AppCompatActivity {
    RequestQueue queue;
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    Boolean isAlrim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alrim_setting);
        queue=Volley.newRequestQueue(getApplicationContext());
        preferences=getApplicationContext().getSharedPreferences(preference, Context.MODE_PRIVATE);
        isAlrim=preferences.getBoolean("alrim", true);

        //뒤로가기 버튼
        ImageButton button_back = findViewById(R.id.button_back);

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 스위치
        Switch switch1 = findViewById(R.id.switch1);

        if (isAlrim) {
            switch1.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.key_green_300)));
            switch1.setChecked(true);
        } else {
            switch1.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_600)));
            switch1.setChecked(false);
        }

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = preferences.edit();

                if (isChecked) {
                    switch1.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.key_green_300)));
                    editor.putBoolean("alrim",true);
                } else {
                    switch1.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_600)));
                    editor.putBoolean("alrim",false);
                }
                editor.apply();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}