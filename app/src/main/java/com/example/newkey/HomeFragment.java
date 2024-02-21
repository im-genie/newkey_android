package com.example.newkey;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;

    private RecommendationAdapter recommendationAdapter;
    private List<RecommendationItem> newsItems;

    private boolean isOpened = false;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 안내 내용 - 맞춤 설정 버튼
        LinearLayout layout_goto_mypage = view.findViewById(R.id.layout_goto_mypage);
        layout_goto_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MypageActivity.class);
                startActivity(intent);
            }
        });

        // 추천뉴스 - recyclerView 구현
        recyclerView = view.findViewById(R.id.recommendation_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        newsItems = new ArrayList<>(); // newsItems.add(new NewsItem("제목", "언론사", "시간", "이미지 URL"));
        newsItems.add(new RecommendationItem("중국 메인뉴스 등장한 AI 앵커…\"표정, 몸짓, 억양 성공적 구현\"\n", "더 차이나", "어제", "https://pds.joongang.co.kr/news/component/htmlphoto_mmdata/202402/14/a586f091-7a01-4a59-820a-7a065c761778.jpg"));
        newsItems.add(new RecommendationItem("박성재 후보자, 절세 효과 누리면서 아내는 탈세 의혹 [뉴스AS]\n", "한겨례", "6시간 전", "https://flexible.img.hani.co.kr/flexible/normal/800/536/imgdb/original/2024/0214/20240214501487.jpg"));
        newsItems.add(new RecommendationItem("'은퇴' 기보배 \"대한민국 양궁 선수로 산다는 건…\"\n", "연합뉴스 TV", "2시간 전", "https://yonhapnewstv-prod.s3.ap-northeast-2.amazonaws.com/article/AKR/20240214/AKR20240214141400641_01_i_P4.jpg"));

        recommendationAdapter = new RecommendationAdapter(newsItems);
        recyclerView.setAdapter(recommendationAdapter);

        // 추천뉴스 - 더보기 버튼
        LinearLayout layout_goto_recommendation = view.findViewById(R.id.layout_goto_recommendation);
        layout_goto_recommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecommendationActivity.class);
                startActivity(intent);
            }
        });

        // HOT 키워드 - 물음표
        ImageView button_question = view.findViewById(R.id.button_question);
        button_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // HOT 키워드 - 새로고침
        ImageView button_refresh = view.findViewById(R.id.button_refresh);
        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // *********** 새로고치는 event *********** //
            }
        });

        // 실시간 인기 키워드 순위 열기 접기
        FrameLayout keyword_6 = view.findViewById(R.id.keyword_6);
        FrameLayout keyword_7 = view.findViewById(R.id.keyword_7);
        FrameLayout keyword_8 = view.findViewById(R.id.keyword_8);
        FrameLayout keyword_9 = view.findViewById(R.id.keyword_9);
        FrameLayout keyword_10 = view.findViewById(R.id.keyword_10);

        TextView keyword_open_close_text = view.findViewById(R.id.keyword_open_close_text);
        ImageView keyword_open_close_image = view.findViewById(R.id.keyword_open_close_image);

        ScrollView home_scrollview = view.findViewById(R.id.home_scrollview);

        LinearLayout keyword_open_close = view.findViewById(R.id.keyword_open_close);
        keyword_open_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpened) { // FALSE -> 접혀있는 상태임. 열어야 함
                    // 키워드 open
                    keyword_6.setVisibility(View.VISIBLE);
                    keyword_7.setVisibility(View.VISIBLE);
                    keyword_8.setVisibility(View.VISIBLE);
                    keyword_9.setVisibility(View.VISIBLE);
                    keyword_10.setVisibility(View.VISIBLE);
                    // 열기/접기 세팅 변경
                    keyword_open_close_text.setText("접기");
                    keyword_open_close_image.setImageResource(R.drawable.button_up);
                    // 현재 상태 변경
                    isOpened = true;
                }
                else {
                    // 키워드 open
                    keyword_6.setVisibility(View.GONE);
                    keyword_7.setVisibility(View.GONE);
                    keyword_8.setVisibility(View.GONE);
                    keyword_9.setVisibility(View.GONE);
                    keyword_10.setVisibility(View.GONE);
                    // 열기/접기 세팅 변경
                    keyword_open_close_text.setText("펼치기");
                    keyword_open_close_image.setImageResource(R.drawable.button_down);
                    // 현재 상태 변경
                    isOpened = false;
                }
            }
        });

        return view;
    }
}