package com.example.newkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WebViewActivity extends AppCompatActivity {

    ImageView web_view_back;
    ImageView bookMark;

    RequestQueue queue;
    String email;
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    Set<String> storedNewsIds = new HashSet<>();
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        // WebView 설정
        WebView webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Intent로 전달받은 URL 로드
        String url = getIntent().getStringExtra("url");
        if (url != null) {
            webView.loadUrl(url);
        }

        web_view_back = findViewById(R.id.web_view_back);
        web_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // WebView에서 뒤로 갈 수 있으면 뒤로가기, 없으면 Activity 종료
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });

        bookMark = findViewById(R.id.news3_scrap);

        queue= Volley.newRequestQueue(getApplicationContext());

        id = getIntent().getStringExtra("id");

        preferences=getApplicationContext().getSharedPreferences(preference, Context.MODE_PRIVATE);


        email=preferences.getString("email", null);

        // 저장 뉴스 불러오기
        String storeUrl="http://15.164.199.177:5000/storedNews";

        final StringRequest storeRequest=new StringRequest(Request.Method.POST, storeUrl, new Response.Listener<String>() {
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

                // 저장 뉴스 목록에 해당 뉴스 id 있으면 북마크 표시
                if (storedNewsIds.contains(id)) {
                    bookMark.setTag(true);
                    bookMark.setImageResource(R.drawable.bookmark_checked);
                } else {
                    bookMark.setTag(false);
                    bookMark.setImageResource(R.drawable.bookmark_unchecked);
                }
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
        storeRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000000,  // 기본 타임아웃 (기본값: 2500ms)
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        storeRequest.setShouldCache(false);
        queue.add(storeRequest);

        bookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = bookMark.getTag() != null && (boolean) bookMark.getTag();
                if (!isChecked) {
                    bookMark.setImageResource(R.drawable.bookmark_checked);
                    bookMark.setTag(true);

                    // 사용자 저장 기사
                    String store_url = "http://15.164.199.177:5000/store";

                    final StringRequest request=new StringRequest(Request.Method.POST, store_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //클릭 시 기사 자세히 보여주기
                            Log.d("res",response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error);
                        }
                    }){
                        //@Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("user_id", email);
                            params.put("stored_news", id);

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
                } else {
                    bookMark.setImageResource(R.drawable.bookmark_unchecked);
                    bookMark.setTag(false);

                    // 사용자 저장 취소 기사
                    String unstore_url = "http://15.164.199.177:5000/unstore";

                    final StringRequest request=new StringRequest(Request.Method.POST, unstore_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //클릭 시 기사 자세히 보여주기
                            Log.d("res",response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error);
                        }
                    }){
                        //@Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("user_id", email);
                            params.put("stored_news", id);

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
            }
        });
    }

    @Override
    public void onBackPressed() {
        WebView webView = findViewById(R.id.webView);
        // WebView에서 뒤로 갈 수 있으면 뒤로가기, 없으면 Activity 종료
        if (webView.canGoBack()) {
            webView.goBack(); // WebView 내 뒤로가기
        } else {
            super.onBackPressed(); // Activity 종료
        }
    }
}