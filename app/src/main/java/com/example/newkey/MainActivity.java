package com.example.newkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 검색 클릭이벤트
        ImageView main_activity_linearlayout1_imageview2 = findViewById(R.id.main_activity_linearlayout1_imageview2);
        main_activity_linearlayout1_imageview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, search1.class);
                startActivity(intent);
            }
        });

        // 알림 클릭이벤트
        ImageView main_activity_linearlayout1_imageview3 = findViewById(R.id.main_activity_linearlayout1_imageview3);
        main_activity_linearlayout1_imageview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, notification1.class);
                startActivity(intent);
            }
        });

        ImageView main_activity_linearlayout1_imageview1=findViewById(R.id.main_activity_linearlayout1_imageview1);

        main_activity_linearlayout1_imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView button_home = findViewById(R.id.button_home);
        ImageView button_feed = findViewById(R.id.button_feed);
        ImageView button_person = findViewById(R.id.button_person);

        // 네비게이션 바: Home

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                button_home.setImageResource(R.drawable.home_green);
                button_feed.setImageResource(R.drawable.feed);
                button_person.setImageResource(R.drawable.person);
            }
        });

        // 네비게이션 바: news
        button_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, news1_activity.class);
                startActivity(intent);
                button_home.setImageResource(R.drawable.home);
                button_feed.setImageResource(R.drawable.feed_green);
                button_person.setImageResource(R.drawable.person);
            }
        });
        // 네비게이션 바: mypage
        button_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MypageActivity.class);
                startActivity(intent);
                button_home.setImageResource(R.drawable.home);
                button_feed.setImageResource(R.drawable.feed);
                button_person.setImageResource(R.drawable.person_green);
            }
        });

        // HomeFragment 띄우기
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        HomeFragment myFragment = new HomeFragment();
        fragmentTransaction.add(R.id.main_activity_framelayout1_linearlayout1, myFragment);
        fragmentTransaction.commit();


    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageView button_home = findViewById(R.id.button_home);
        ImageView button_feed = findViewById(R.id.button_feed);
        ImageView button_person = findViewById(R.id.button_person);

        button_home.setImageResource(R.drawable.home_green);
        button_feed.setImageResource(R.drawable.feed);
        button_person.setImageResource(R.drawable.person);
    }
}