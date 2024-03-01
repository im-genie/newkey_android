package com.example.newkey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

public class KeywordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword);

        // CardnewsFragment 띄우기
        ViewPager2 viewPager = findViewById(R.id.card_news_layout);
        MyFragmentAdapter adapter = new MyFragmentAdapter(this);
        viewPager.setAdapter(adapter);

        // keyword 이름 설정
        String receivedText = getIntent().getStringExtra("keyword");
        TextView textView = findViewById(R.id.top_keyword);
        textView.setText(receivedText);

        // 뒤로가기 - Main Activity로 이동
        ImageView cardnews_back = findViewById(R.id.cardnews_back);
        cardnews_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KeywordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // 네비게이션 바
        ImageView button_home = findViewById(R.id.button_home_keyword);
        ImageView button_feed = findViewById(R.id.button_feed_keyword);
        ImageView button_person = findViewById(R.id.button_person_keyword);

        // 네비게이션 바: Home
        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KeywordActivity.this, MainActivity.class);
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
                Intent intent = new Intent(KeywordActivity.this, NewsActivity.class);
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
                Intent intent = new Intent(KeywordActivity.this, MypageActivity.class);
                startActivity(intent);
                button_home.setImageResource(R.drawable.home);
                button_feed.setImageResource(R.drawable.feed);
                button_person.setImageResource(R.drawable.person_green);
            }
        });
    }
}