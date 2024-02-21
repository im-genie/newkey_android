package com.example.newkey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class KeywordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword);

        // CardnewsFragment 띄우기
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        CardnewsFragment cardnewsFragment = new CardnewsFragment();
        fragmentTransaction.add(R.id.card_news_layout, cardnewsFragment);
        fragmentTransaction.commit();

        // 뒤로가기 - Main Activity로 이동
        ImageView cardnews_back = findViewById(R.id.cardnews_back);
        cardnews_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KeywordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}