package com.example.newkey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.transition.TransitionManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class news3_activity extends AppCompatActivity {

    private ImageView news3back, news3SummaryArrow;
    private FrameLayout summaryButton;
    private CardView summaryCardView;
    TextView Title,Content,Date,Reporter,Publisher,who,what,when,how,why,where;
    ImageView Img,bookMark;
    RequestQueue queue;
    String email;
    Set<String> storedNewsIds = new HashSet<>();
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    String fiveWOneHUrl="http://15.164.199.177:5000/5w1h";
    private static final Executor executor = Executors.newFixedThreadPool(4);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news3);

        news3back = findViewById(R.id.news3_back);
        news3SummaryArrow = findViewById(R.id.news3_summary_arrow);
        summaryButton = findViewById(R.id.new3_summary);
        summaryCardView = findViewById(R.id.summary_cardview);
        bookMark = findViewById(R.id.news3_scrap);

        what=findViewById(R.id.what);
        who=findViewById(R.id.who);
        when=findViewById(R.id.when);
        how=findViewById(R.id.how);
        why=findViewById(R.id.why);
        where=findViewById(R.id.where);

        Title=findViewById(R.id.title);
        Content=findViewById(R.id.content);
        Date=findViewById(R.id.date);
        Reporter=findViewById(R.id.reporter);
        Publisher=findViewById(R.id.publisher);
        Img=findViewById(R.id.newsImg);
        queue=Volley.newRequestQueue(getApplicationContext());

        Content.setLineSpacing(0,1.66f);

        String id = getIntent().getStringExtra("id");
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String date = getIntent().getStringExtra("date");
        String publisher = getIntent().getStringExtra("publisher");
        String summary = getIntent().getStringExtra("summary");
        String imgUrl = getIntent().getStringExtra("img");
        String mediaImgUrl = getIntent().getStringExtra("media_img");
        String reporter = getIntent().getStringExtra("reporter");
        String key = getIntent().getStringExtra("key");
        String url = getIntent().getStringExtra("url");

        Title.setText(title);
        Content.setText(url); // 기사 url 받아옴
        Date.setText(date);
        Reporter.setText(reporter+" 기자");
        Publisher.setText(publisher);

        if(imgUrl.equals("none")){
            imgUrl=mediaImgUrl;
        }

        preferences=getApplicationContext().getSharedPreferences(preference, Context.MODE_PRIVATE);
        email=preferences.getString("email", null);


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

        final ImageRequest imageRequest = new ImageRequest(imgUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Log.d("test","image get");
                try {
                    Img.setImageBitmap(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },
                500, 500, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러 처리
                System.out.println(error);
            }
        });

        imageRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000000,  // 기본 타임아웃 (기본값: 2500ms)
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        imageRequest.setShouldCache(false);
        queue.add(imageRequest);

        news3back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TransitionManager.beginDelayedTransition((ViewGroup) summaryCardView.getParent());

        summaryCardView.setVisibility(View.GONE);
        news3SummaryArrow.setImageResource(R.drawable.news3_up);
        summaryButton.setBackgroundResource(R.drawable.news3_radius2);
        summaryButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_500)));
        Toast.makeText(getApplicationContext(), "요약이 준비되지 않았어요", Toast.LENGTH_SHORT).show();

        final StringRequest fiveWOneHRequest = new StringRequest(Request.Method.POST, fiveWOneHUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res",response);

                try {
                    // JSON 형식의 response를 파싱
                    JSONObject jsonResponse = new JSONObject(response);

                    // 각 5W1H 요소에 맞는 데이터를 가져와 TextView에 설정
                    who.setText(jsonResponse.getString("누가"));
                    when.setText(jsonResponse.getString("언제"));
                    where.setText(jsonResponse.getString("어디서"));
                    how.setText(jsonResponse.getString("어떻게"));
                    why.setText(jsonResponse.getString("왜"));
                    what.setText(jsonResponse.getString("무엇을"));

                    summaryCardView.setVisibility(View.VISIBLE);
                    summaryButton.setBackgroundResource(R.drawable.news3_radius2);
                    summaryButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.key_green_400)));
                    news3SummaryArrow.setImageResource(R.drawable.news3_down);
                    Toast.makeText(getApplicationContext(), "해당 기사를 요약했어요", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "응답 파싱 중 오류 발생", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("육하원칙 오류",error.toString());
            }
        }){
            //@Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id", id); // 로그인 아이디로 바꾸기
                params.put("summary", summary);
                params.put("key", key);

                return params;
            }
        };
        fiveWOneHRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000000,  // 기본 타임아웃 (기본값: 2500ms)
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        fiveWOneHRequest.setShouldCache(false);
        queue.add(fiveWOneHRequest);

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

        // 육하원칙 요약 보여주기
        summaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition((ViewGroup) summaryCardView.getParent());

                if (summaryCardView.getVisibility() == View.VISIBLE) {
                    summaryCardView.setVisibility(View.GONE);
                    summaryButton.setBackgroundResource(R.drawable.news3_radius2);
                    summaryButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_500)));
                    news3SummaryArrow.setImageResource(R.drawable.news3_up);

                    TextView news3SummaryText = findViewById(R.id.news3_summary_text);
                    news3SummaryText.setTextColor(getResources().getColor(R.color.white));
                } else {
                    summaryCardView.setVisibility(View.VISIBLE);
                    summaryButton.setBackgroundResource(R.drawable.news3_radius2);
                    summaryButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.key_green_400)));
                    news3SummaryArrow.setImageResource(R.drawable.news3_down);

                    TextView news3SummaryText = findViewById(R.id.news3_summary_text);
                    news3SummaryText.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}