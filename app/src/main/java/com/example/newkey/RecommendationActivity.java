package com.example.newkey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class RecommendationActivity extends AppCompatActivity {

    private List<news1_item> recommendList;
    RequestQueue recommendQueue, bookQueue;
    private SharedPreferences preferences;
    public static final String preference = "newkey";

    ImageView back_recommendation_to_main;
    Set<String> storedNewsIds = new HashSet<>();
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        preferences = getApplicationContext().getSharedPreferences(preference, Context.MODE_PRIVATE);
        email = preferences.getString("email", null);

        recommendList = new ArrayList<>();
        recommendQueue = Volley.newRequestQueue(getApplicationContext());
        bookQueue = Volley.newRequestQueue(getApplicationContext());

        // 저장 & 추천 뉴스 불러오기
        fetchStoredNews();

        // 뒤로가기 - Main Activity로 이동
        ImageView back_recommendation_to_main = findViewById(R.id.back_recommendation_to_main);

        back_recommendation_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    // 추천 뉴스 불러오기
    private void recommend() {
        String recommendUrl = "http://15.164.199.177:5000/recommend";
        //추천뉴스
        final StringRequest recommendRequest = new StringRequest(Request.Method.POST, recommendUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res", response);
                JSONArray jsonArray = null;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

                Date currentDate = new Date();

                try {
                    jsonArray = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
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

                        if (!dateStr.isEmpty() && !dateStr.equals("null")) {
                            Date articleDate = sdf.parse(dateStr); // 서버에서 받은 날짜 문자열을 Date 객체로 변환
                            long diffInMillis = currentDate.getTime() - articleDate.getTime(); // 시간 차이 계산
                            String timeAgo = getTimeAgo(diffInMillis); // 차이를 "몇 시간 전" 형식으로 변환
                            news1_item newsData = new news1_item(id, title, content, url, press, timeAgo, img, summary, key, reporter, mediaImg);
                            recommendList.add(newsData);
                        } else {
                            // 날짜가 없을 경우 기본값으로 처리 (예: "방금"으로 설정)
                            news1_item newsData = new news1_item(id, title, content, url, press, "", img, summary, key, reporter, mediaImg);
                            recommendList.add(newsData);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }

                // 이후에 newsList를 사용하여 원하는 처리를 진행
                //Adapter
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                RecyclerView recyclerView = findViewById(R.id.recommendation_recyclerview);
                recyclerView.setLayoutManager(layoutManager);
                RecommendationBigsizeAdapter adapter = new RecommendationBigsizeAdapter(getApplicationContext(), recommendList, storedNewsIds);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("추천 오류", error.toString());
            }
        }) {
            //@Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", email); // 로그인 아이디로 바꾸기
                return params;
            }
        };

        recommendRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000000, // 기본 타임아웃 시간을 조정
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 재시도 횟수
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT // 백오프 멀티플라이어
        ));

        recommendRequest.setShouldCache(false); // 캐시 사용 여부
        recommendQueue.add(recommendRequest); // 올바른 변수명으로 수정
    }

    // 저장 뉴스 불러오기
    private void fetchStoredNews() {
        String url="http://15.164.199.177:5000/storedNews";

        final StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = new JSONArray(response);
                } catch (JSONException e) {
                    Log.d("JSONParseError", e.toString());
                    e.printStackTrace();
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        storedNewsIds.add(jsonObject.getString("id"));
                    } catch (JSONException e) {
                        Log.d("res!!",response);
                        e.printStackTrace();
                    }
                }
                // 추천 뉴스 불러오기
                recommend();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("storeViewError",error.toString());
            }
        }){
            //@Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", email); // 로그인 아이디로 바꾸기
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                1000000,  // 기본 타임아웃 (기본값: 2500ms)
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        request.setShouldCache(false);
        bookQueue.add(request);
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
