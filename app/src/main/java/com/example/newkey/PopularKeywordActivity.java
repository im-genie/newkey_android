package com.example.newkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopularKeywordActivity extends AppCompatActivity {
    ImageButton back;
    TextView popularKeyword;
    RequestQueue queue;
    private List<news1_item> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_keyword);

        newsList = new ArrayList<>();
        queue = Volley.newRequestQueue(getApplicationContext());
        String keyword = getIntent().getStringExtra("popular");
        popularKeyword=findViewById(R.id.popularKeyword);
        popularKeyword.setText(keyword);

        String url = "http://15.164.199.177:5000/popularKeywordNews";
        final StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = new JSONArray();
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
                        String date = jsonObject.getString("date");
                        String img = jsonObject.getString("img");
                        String summary = jsonObject.getString("summary");
                        String key = jsonObject.getString("key");
                        String reporter = jsonObject.getString("reporter");
                        String mediaImg = jsonObject.getString("media_img");

                        // NewsData 클래스를 사용하여 데이터를 저장하고 리스트에 추가
                        news1_item newsData = new news1_item(id,title,content,press,date,img,summary,key,reporter,mediaImg);
                        newsList.add(newsData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                RecyclerView recyclerView=findViewById(R.id.popularRecyclerView);
                recyclerView.setLayoutManager(layoutManager);
                news1_adapter adapter=new news1_adapter(newsList);
                recyclerView.setAdapter(adapter);
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
                params.put("keyword", keyword);
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

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}