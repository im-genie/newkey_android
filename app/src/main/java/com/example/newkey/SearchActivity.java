package com.example.newkey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    RecentSearchAdapter adapter;
    private List<String> recentSearchList;
    Button allClear;
    ImageButton back;
    SearchView search;
    RequestQueue queue;
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        preferences = getApplicationContext().getSharedPreferences(preference, Context.MODE_PRIVATE);
        email = preferences.getString("email", null);
        queue = Volley.newRequestQueue(getApplicationContext());

        // 최근 검색어를 표시할 RecyclerView 초기화
        RecyclerView recyclerViewRecentSearch = findViewById(R.id.recyclerView_SearchNews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewRecentSearch.setLayoutManager(layoutManager);

        // 최근 검색어 데이터를 가져오거나 초기화
        recentSearchList = new ArrayList<>();
        adapter = new RecentSearchAdapter(recentSearchList);
        recyclerViewRecentSearch.setAdapter(adapter);

        // 검색기록 가져오기
        addKeywordData();

        allClear=findViewById(R.id.btn_delete);
        allClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllItems();
            }
        });

        search=findViewById(R.id.view_search);
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

        back=findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // 모든 검색어 삭제 메소드
    public void clearAllItems() {
        recentSearchList.clear();
        //adapter.notifyDataSetChanged();

        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView=findViewById(R.id.recyclerView_SearchNews);
        recyclerView.setLayoutManager(layoutManager);
        RecentSearchAdapter adapter=new RecentSearchAdapter(recentSearchList);
        recyclerView.setAdapter(adapter);

        String url = "http://15.164.199.177:5000/searchAllDelete";
        final StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("searchAllDelete",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("searchAllDeleteError",error.toString());
            }
        }){
            //@Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", email);
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
    }

    // 최근 검색어 테스트 데이터 추가
    public void addKeywordData() {
        // recentSearchList 객체가 null인지 확인하고 null이면 새로운 ArrayList로 초기화합니다.
        if (recentSearchList == null) {
            recentSearchList = new ArrayList<>();
        }

        // 테스트 검색어를 recentSearchList에 추가합니다.
        //recentSearchList.add("테스트 검색어 1");
        //recentSearchList.add("테스트 검색어 2");
        //recentSearchList.add("테스트 검색어 3");

        String url = "http://15.164.199.177:5000/searchView";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        Log.d("searchView",response);
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            recentSearchList.add(jsonArray.getString(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                    RecyclerView recyclerView=findViewById(R.id.recyclerView_SearchNews);
                    recyclerView.setLayoutManager(layoutManager);
                    RecentSearchAdapter adapter=new RecentSearchAdapter(recentSearchList);
                    recyclerView.setAdapter(adapter);

                    // 어댑터에 데이터가 변경되었음을 알리고 RecyclerView를 업데이트합니다.
                    adapter.notifyDataSetChanged();
                },
                error -> {
                    // 오류 처리
                    Log.e("Volley", "Error: " + error.getMessage());
                }) {
            @Override
            protected Map<String, String> getParams() {
                // POST 요청에 포함될 파라미터 설정
                Map<String, String> params = new HashMap<>();
                params.put("user_id", email);
                return params;
            }
        };

        // 요청 큐에 추가
        request.setRetryPolicy(new DefaultRetryPolicy(
                1000000,  // 기본 타임아웃 (기본값: 2500ms)
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        request.setShouldCache(false);
        queue.add(request);
    }
}