package com.example.newkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class SearchViewActivity extends AppCompatActivity {
    ArrayList<news1_item> newsList;
    RequestQueue queue;
    String query;
    SearchView search;
    ImageButton back;
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        newsList = new ArrayList<>();
        queue = Volley.newRequestQueue(getApplicationContext());
        query = getIntent().getStringExtra("search");

        preferences = getApplicationContext().getSharedPreferences(preference, Context.MODE_PRIVATE);
        email = preferences.getString("email", null);

        search = findViewById(R.id.view_search);

        // SearchView 내부의 EditText를 찾아서 hint text size 변경
        EditText searchEditText = search.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14); // hint text size를 설정

        search.setQuery(query, false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 입력이 완료되었을 때 수행할 동작
                Intent intent = new Intent(getApplicationContext(), SearchViewActivity.class);
                intent.putExtra("search", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        String url = "http://15.164.199.177:5000/search";
        final StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = new JSONArray();

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
                            newsList.add(newsData);
                        } else {
                            // 날짜가 없을 경우 기본값으로 처리 (예: "방금"으로 설정)
                            news1_item newsData = new news1_item(id, title, content, url, press, "", img, summary, key, reporter, mediaImg);
                            newsList.add(newsData);
                        }

                        // 이후에 newsList를 사용하여 원하는 처리를 진행
                        //Adapter
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        RecyclerView recyclerView=findViewById(R.id.recyclerView_SearchNews);
                        recyclerView.setLayoutManager(layoutManager);
                        news1_adapter adapter=new news1_adapter(newsList);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("searchError",error.toString());
            }
        }){
            //@Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", email);
                params.put("keyword", query);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                1000000,  // 기본 타임아웃 (기본값: 2500ms)
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        request.setShouldCache(false);
        queue.add(request);

        back=findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
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
