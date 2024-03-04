package com.example.newkey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class news1_activity extends AppCompatActivity {
    private ViewPager2 viewPagerNews; // 일반 뉴스를 위한 ViewPager2
    private ViewPager2 viewPagerHotNews; // HOT 뉴스를 위한 ViewPager2
    private TabLayout tabLayout;
    private RecyclerView recyclerView; // 가로 RecyclerView

    private TextView news1Information1;
    private ImageView news1Information2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news1);

        viewPagerHotNews = findViewById(R.id.viewPagerHotNews);
        viewPagerNews = findViewById(R.id.viewPagerNews);
        recyclerView = findViewById(R.id.news1_hot_recyclerview);
        tabLayout = findViewById(R.id.toolbar);

        viewPagerNews.setAdapter(new news1_fragment_adapter(this));
        viewPagerHotNews.setAdapter(new news1_hot_fragment_adapter(this));

        news1Information1 = findViewById(R.id.news1_information1);
        news1Information2 = findViewById(R.id.news1_information2);

        news1Information1.setOnClickListener(getMoreClickListener(tabLayout.getSelectedTabPosition()));
        news1Information2.setOnClickListener(getMoreClickListener(tabLayout.getSelectedTabPosition()));

        new TabLayoutMediator(tabLayout, viewPagerNews, (tab, position) -> {
            String[] tabTitles = {"정치", "경제", "사회", "생활", "세계", "IT", "오피니언", "스포츠"};
            tab.setText(tabTitles[position]);
        }).attach();

        viewPagerNews.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        viewPagerHotNews.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));

                viewPagerHotNews.setUserInputEnabled(false); //중요! HOT 뉴스 viewpager2 동작 못하게 하기

            }

        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPagerNews.setCurrentItem(position);
                viewPagerHotNews.setCurrentItem(position);
                news1Information1.setOnClickListener(getMoreClickListener(position));
                news1Information2.setOnClickListener(getMoreClickListener(position));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollHorizontally(1)) {
                    viewPagerNews.setCurrentItem(viewPagerNews.getCurrentItem() + 1);
                }
            }
        });


        ImageView button_home = findViewById(R.id.button_home);
        ImageView button_feed = findViewById(R.id.button_feed);
        ImageView button_person = findViewById(R.id.button_person);



        // 네비게이션 바: Home

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(news1_activity.this, MainActivity.class);
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
                Intent intent = new Intent(news1_activity.this, news1_activity.class);
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
                Intent intent = new Intent(news1_activity.this, MypageActivity.class);
                startActivity(intent);
                button_home.setImageResource(R.drawable.home);
                button_feed.setImageResource(R.drawable.feed);
                button_person.setImageResource(R.drawable.person_green);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        ImageView button_home = findViewById(R.id.button_home);
        ImageView button_feed = findViewById(R.id.button_feed);
        ImageView button_person = findViewById(R.id.button_person);

        button_home.setImageResource(R.drawable.home);
        button_feed.setImageResource(R.drawable.feed_green);
        button_person.setImageResource(R.drawable.person);
    }


    private View.OnClickListener getMoreClickListener(int position) {
        switch (position) {
            case 0: // 정치
                return v -> startActivity(new Intent(this, news2_politics.class));
            case 1: // 경제
                return v -> startActivity(new Intent(this, news2_economy.class));
            case 2: // 사회
                return v -> startActivity(new Intent(this, news2_society.class));
            case 3: // 생활
                return v -> startActivity(new Intent(this, news2_living.class));
            case 4: // 세계
                return v -> startActivity(new Intent(this, news2_world.class));
            case 5: // IT
                return v -> startActivity(new Intent(this, news2_it.class));
            case 6: // 오피니언
                return v -> startActivity(new Intent(this, news2_opinion.class));
            case 7: // 스포츠
                return v -> startActivity(new Intent(this, news2_sports.class));
            default:
                // 기본적으로 아무 동작도 하지 않는 리스너 반환
                return v -> {};
        }
    }
}