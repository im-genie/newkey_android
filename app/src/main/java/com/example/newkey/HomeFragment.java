package com.example.newkey;

import static com.google.android.material.internal.ViewUtils.dpToPx;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;
import android.content.Context;

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
                // 툴팁으로 사용할 레이아웃 인플레이트
                View tooltipView = inflater.inflate(R.layout.tooltip_layout, (ViewGroup) v.getRootView(), false);
                final PopupWindow tooltipPopup = new PopupWindow(tooltipView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                // 바깥 클릭을 감지하도록 설정
                tooltipPopup.setOutsideTouchable(true);
                tooltipPopup.setFocusable(true);

                // 버튼의 위치와 크기를 가져오기 위해 배열을 생성합니다.
                int[] location = new int[2];
                button_question.getLocationOnScreen(location);

                // dp 값을 픽셀로 변환합니다. getResources()와 DisplayMetrics를 사용합니다.
                Resources resources = v.getContext().getResources();
                int pxOffset = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, resources.getDisplayMetrics()));

                // 툴팁을 버튼 위로 위치시킵니다. 버튼의 Y 좌표에서 버튼의 높이와 변환된 픽셀 값을 빼줍니다.
                tooltipPopup.showAtLocation(v.getRootView(), Gravity.NO_GRAVITY, location[0], location[1] - button_question.getHeight() - pxOffset);


                // 툴팁이 사라졌을 때의 동작 설정
                tooltipPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        // 모든 오버레이를 제거하고 원래의 뷰로 복원
                        LinearLayout container = view.findViewById(R.id.container_layout);
                        for (int i = 0; i < container.getChildCount(); i++) {
                            View child = container.getChildAt(i);
                            if (child instanceof FrameLayout) {
                                FrameLayout frameLayout = (FrameLayout) child;
                                if (frameLayout.getChildCount() > 1) {
                                    View originalChild = frameLayout.getChildAt(0);
                                    frameLayout.removeViewAt(1); // 오버레이 제거

                                    // 필요한 경우, 여기서 추가적인 복원 로직을 수행할 수 있습니다.
                                }
                            }
                        }
                    }
                });

                LinearLayout container = view.findViewById(R.id.container_layout);
                for (int i = 0; i < container.getChildCount(); i++) {
                    View child = container.getChildAt(i);

                    // 특정 레이아웃을 제외하고 투명한 회색 뷰 추가
                    if (child.getId() != R.id.hot_keyword_layout) {
                        FrameLayout frameLayout = new FrameLayout(v.getContext());
                        ViewGroup.LayoutParams originalParams = child.getLayoutParams();
                        frameLayout.setLayoutParams(originalParams);

                        // 기존 뷰를 부모에서 제거하고 FrameLayout에 추가
                        ViewGroup parent = (ViewGroup) child.getParent();
                        int index = parent.indexOfChild(child);
                        parent.removeView(child);
                        frameLayout.addView(child, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                        // 투명한 회색 뷰(overlay)를 FrameLayout에 추가
                        View overlay = new View(v.getContext());
                        overlay.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        overlay.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.gray_600_90)); // 투명한 회색
                        frameLayout.addView(overlay);

                        // FrameLayout을 원래의 부모 뷰에 추가
                        parent.addView(frameLayout, index);
                    }
                }
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

        // HOT 키워드 - 키워드 클릭 시 카드뉴스로 이동
        TextView top_1 = view.findViewById(R.id.top_1);
        TextView top_2 = view.findViewById(R.id.top_2);
        TextView top_3 = view.findViewById(R.id.top_3);
        TextView top_4 = view.findViewById(R.id.top_4);
        TextView top_5 = view.findViewById(R.id.top_5);

        top_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TextView의 텍스트를 가져옵니다.
                String textToSend = top_1.getText().toString();

                // 새로운 액티비티를 시작하고 텍스트를 전달합니다.
                Intent intent = new Intent(getActivity(), KeywordActivity.class);
                intent.putExtra("keyword", textToSend);
                startActivity(intent);
            }
        });
        top_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TextView의 텍스트를 가져옵니다.
                String textToSend = top_2.getText().toString();

                // 새로운 액티비티를 시작하고 텍스트를 전달합니다.
                Intent intent = new Intent(getActivity(), KeywordActivity.class);
                intent.putExtra("keyword", textToSend);
                startActivity(intent);
            }
        });
        top_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TextView의 텍스트를 가져옵니다.
                String textToSend = top_3.getText().toString();

                // 새로운 액티비티를 시작하고 텍스트를 전달합니다.
                Intent intent = new Intent(getActivity(), KeywordActivity.class);
                intent.putExtra("keyword", textToSend);
                startActivity(intent);
            }
        });
        top_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TextView의 텍스트를 가져옵니다.
                String textToSend = top_4.getText().toString();

                // 새로운 액티비티를 시작하고 텍스트를 전달합니다.
                Intent intent = new Intent(getActivity(), KeywordActivity.class);
                intent.putExtra("keyword", textToSend);
                startActivity(intent);
            }
        });
        top_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TextView의 텍스트를 가져옵니다.
                String textToSend = top_5.getText().toString();

                // 새로운 액티비티를 시작하고 텍스트를 전달합니다.
                Intent intent = new Intent(getActivity(), KeywordActivity.class);
                intent.putExtra("keyword", textToSend);
                startActivity(intent);
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
                    home_scrollview.post(new Runnable() {
                        @Override
                        public void run() {
                            home_scrollview.smoothScrollTo(0, home_scrollview.getChildAt(0).getBottom());
                        }
                    });
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