package com.example.newkey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecommendationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecommendationBigsizeAdapter recommendationAdapter;
    private List<RecommendationItemBigsize> newsItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        // 추천 list 구성
        recyclerView = findViewById(R.id.recommendation_recyclerview2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        newsItems = new ArrayList<>();
        // newsItems.add(new NewsItem("제목", "언론사", "시간", "이미지 URL", "언론사 URL"));
        newsItems = new ArrayList<>();
        newsItems.add(new RecommendationItemBigsize("중국 메인뉴스 등장한 AI 앵커…\"표정, 몸짓, 억양 성공적 구현\"\n", "더 차이나", "어제", "https://pds.joongang.co.kr/news/component/htmlphoto_mmdata/202402/14/a586f091-7a01-4a59-820a-7a065c761778.jpg", "https://img.joongang.co.kr/pubimg/northkorea/bn_china_gray.png"));
        newsItems.add(new RecommendationItemBigsize("박성재 후보자, 절세 효과 누리면서 아내는 탈세 의혹 [뉴스AS]\n", "한겨례", "6시간 전", "https://flexible.img.hani.co.kr/flexible/normal/800/536/imgdb/original/2024/0214/20240214501487.jpg", "https://pds.saramin.co.kr/company/logo/202101/06/qmi8ti_f8zg-1kkvbkh_logo.jpg"));
        newsItems.add(new RecommendationItemBigsize("'은퇴' 기보배 \"대한민국 양궁 선수로 산다는 건…\"\n", "연합뉴스 TV", "2시간 전", "https://yonhapnewstv-prod.s3.ap-northeast-2.amazonaws.com/article/AKR/20240214/AKR20240214141400641_01_i_P4.jpg", "https://r.yna.co.kr/global/home/v01/img/yonhapnews_logo_600x600_kr01.jpg"));
        newsItems.add(new RecommendationItemBigsize("'은퇴' 기보배 \"대한민국 양궁 선수로 산다는 건…\"\n", "연합뉴스 TV", "2시간 전", "https://yonhapnewstv-prod.s3.ap-northeast-2.amazonaws.com/article/AKR/20240214/AKR20240214141400641_01_i_P4.jpg", "https://r.yna.co.kr/global/home/v01/img/yonhapnews_logo_600x600_kr01.jpg"));
        newsItems.add(new RecommendationItemBigsize("'은퇴' 기보배 \"대한민국 양궁 선수로 산다는 건…\"\n", "연합뉴스 TV", "2시간 전", "https://yonhapnewstv-prod.s3.ap-northeast-2.amazonaws.com/article/AKR/20240214/AKR20240214141400641_01_i_P4.jpg", "https://r.yna.co.kr/global/home/v01/img/yonhapnews_logo_600x600_kr01.jpg"));
        newsItems.add(new RecommendationItemBigsize("'은퇴' 기보배 \"대한민국 양궁 선수로 산다는 건…\"\n", "연합뉴스 TV", "2시간 전", "https://yonhapnewstv-prod.s3.ap-northeast-2.amazonaws.com/article/AKR/20240214/AKR20240214141400641_01_i_P4.jpg", "https://r.yna.co.kr/global/home/v01/img/yonhapnews_logo_600x600_kr01.jpg"));
        newsItems.add(new RecommendationItemBigsize("'은퇴' 기보배 \"대한민국 양궁 선수로 산다는 건…\"\n", "연합뉴스 TV", "2시간 전", "https://yonhapnewstv-prod.s3.ap-northeast-2.amazonaws.com/article/AKR/20240214/AKR20240214141400641_01_i_P4.jpg", "https://r.yna.co.kr/global/home/v01/img/yonhapnews_logo_600x600_kr01.jpg"));

        recommendationAdapter = new RecommendationBigsizeAdapter(newsItems);
        recyclerView.setAdapter(recommendationAdapter);

        // 뒤로가기 - Main Activity로 이동
        ImageView back_recommendation_to_main = findViewById(R.id.back_recommendation_to_main);
        back_recommendation_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecommendationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
