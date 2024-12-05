package com.example.newkey;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class news1_society extends Fragment {

    private List<news1_item> itemList;
    private news1_adapter adapter;
    private int currentPage = 1;  // 현재 페이지
    private final int pageSize = 100;  // 한 페이지에 불러올 기사 수
    private boolean isLoading = false;  // 로딩 상태를 추적하여 중복 로드 방지
    private Handler handler = new Handler(); // 3초 지연을 위한 Handler

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news1_society, container, false);

        itemList = new ArrayList<>();

        // RecyclerView 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = view.findViewById(R.id.news1_society_recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);  // 성능 최적화 (리사이클러뷰의 크기가 고정되어 있다면 사용)
        adapter = new news1_adapter(itemList);
        recyclerView.setAdapter(adapter);

        // 첫 페이지 로드
        loadNews(currentPage);

        // 스크롤 리스너: 사용자가 스크롤 내릴 때 로딩 중일 경우 토스트 메시지
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && isLoading) {
                    // 사용자가 스크롤 내릴 때 로딩 중이면 토스트 메시지 표시
                    Toast.makeText(getContext(), "다음 페이지 로딩 중입니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 3초마다 다음 페이지 로드
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // currentPage가 8 이하일 때만 다음 페이지 로드
                if (!isLoading && currentPage < 2) {
                    currentPage++;
                    loadNews(currentPage);
                }
                // 3초마다 호출 (재귀적으로 호출)
                handler.postDelayed(this, 3000);  // 3초 딜레이
            }
        }, 3000);

        return view;
    }

    // 뉴스 데이터를 페이지 단위로 로드하는 메서드
    private void loadNews(int page) {
        isLoading = true;  // 로딩 중 플래그 설정
        String url = "http://15.164.199.177:5000/social?page=" + page + "&size=" + pageSize;

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("success!!", "success!! " + response.toString());
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

                    Date currentDate = new Date();

                    // 응답으로부터 데이터를 파싱하여 itemList에 추가
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String title = jsonObject.getString("title");
                        String content = jsonObject.getString("origin_content");
                        String press = jsonObject.getString("media");
                        String dateStr = jsonObject.getString("date");
                        String img = jsonObject.getString("img");
                        String summary = jsonObject.getString("summary");
                        String key = jsonObject.getString("key");
                        String reporter = jsonObject.getString("reporter");
                        String mediaImg = jsonObject.getString("media_img");
                        String url = jsonObject.getString("url");

                        Date articleDate;
                        if (!dateStr.isEmpty() && !dateStr.equals("null")) {
                            try {
                                // 밀리초 타임스탬프로 변환
                                long timestamp = Long.parseLong(dateStr);
                                articleDate = new Date(timestamp);
                            } catch (NumberFormatException e) {
                                // 만약 밀리초가 아니고 일반 날짜 문자열이라면 SimpleDateFormat으로 파싱
                                articleDate = sdf.parse(dateStr);
                            }

                            // 시간 차이 계산
                            long diffInMillis = currentDate.getTime() - articleDate.getTime();
                            String timeAgo = getTimeAgo(diffInMillis); // 차이를 "몇 시간 전" 형식으로 변환
                            news1_item newsData = new news1_item(id, title, content, url, press, timeAgo, img, summary, key, reporter, mediaImg);
                            itemList.add(newsData);
                        } else {
                            // 날짜가 없을 경우 기본값으로 처리 (예: "방금")
                            news1_item newsData = new news1_item(id, title, content, url, press, "", img, summary, key, reporter, mediaImg);
                            itemList.add(newsData);
                        }
                    }

                    adapter.notifyDataSetChanged();  // 어댑터에 데이터가 갱신되었음을 알림
                    isLoading = false;  // 로딩 완료
                } catch (Exception e) {
                    Log.d("myTest", e.toString());
                    e.printStackTrace();
                    isLoading = false;  // 에러 발생 시 로딩 상태 해제
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("fail!!", error.toString());
                isLoading = false;  // 에러 발생 시 로딩 상태 해제
            }
        });

        // 요청 재시도 및 타임아웃 설정
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,  // 기본 타임아웃
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // 기본 재시도 횟수
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        request.setShouldCache(true);
        //queue.add(request);
        MySingleton.getInstance(getContext()).addToRequestQueue(request);  // 싱글톤을 통한 요청 추가
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // 핸들러 콜백 제거: 더 이상 필요하지 않은 핸들러 작업 제거
        handler.removeCallbacksAndMessages(null);

        // MySingleton에서 관리하는 RequestQueue에서 모든 요청 취소
        if (MySingleton.getInstance(getContext()).getRequestQueue() != null) {
            MySingleton.getInstance(getContext()).getRequestQueue().cancelAll(this);  // 네트워크 요청 정리
        }
    }

    // 시간 차이를 "몇 분 전", "몇 시간 전", "며칠 전"으로 변환하는 메서드
    private String getTimeAgo(long diffInMillis) {
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
        if (diffInMinutes < 60) {
            return diffInMinutes + "분 전";
        } else {
            long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
            if (diffInHours < 24) {
                return diffInHours + "시간 전";
            } else {
                // 하루 이상 차이 나면 원래 날짜 반환
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd. a hh:mm", Locale.KOREA);
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                Date originalDate = new Date(System.currentTimeMillis() - diffInMillis);
                return sdf.format(originalDate);
            }
        }
    }
}