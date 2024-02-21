//package com.example.newkey;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.viewpager2.widget.ViewPager2;
//
//import com.google.android.material.tabs.TabLayout;
//import com.google.android.material.tabs.TabLayoutMediator;
//
//public class news1_activity extends AppCompatActivity {
//    private ViewPager2 viewPagerNews; // 일반 뉴스를 위한 ViewPager2
//    private ViewPager2 viewPagerHotNews; // HOT 뉴스를 위한 ViewPager2
//    private TabLayout tabLayout;
//
//    private TextView news1Information1;
//    private ImageView news1Information2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.news1);
//
//        viewPagerHotNews = findViewById(R.id.viewPagerHotNews);
//        viewPagerNews = findViewById(R.id.viewPagerNews);
//        tabLayout = findViewById(R.id.toolbar);
//
//        viewPagerNews.setAdapter(new news1_fragment_adapter(this));
//        viewPagerHotNews.setAdapter(new news1_hot_fragment_adapter(this));
//
//        news1Information1 = findViewById(R.id.news1_information1);
//        news1Information2 = findViewById(R.id.news1_information2);
//
//        news1Information1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(news1_activity.this, news2_activity.class);
//                startActivity(intent);
//            }
//        });
//
//        news1Information2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(news1_activity.this, news2_activity.class);
//                startActivity(intent);
//            }
//        });
//
//        new TabLayoutMediator(tabLayout, viewPagerNews, (tab, position) -> {
//            String[] tabTitles = {"정치", "경제", "사회", "생활", "세계", "IT", "오피니언", "스포츠"};
//            tab.setText(tabTitles[position]);
//        }).attach();
//
//        viewPagerHotNews.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                tabLayout.selectTab(tabLayout.getTabAt(position));
//            }
//        });
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int position = tab.getPosition();
//                viewPagerHotNews.setCurrentItem(position);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
//    }
//
//}
