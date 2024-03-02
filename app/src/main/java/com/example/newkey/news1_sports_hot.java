package com.example.newkey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;

public class news1_sports_hot extends Fragment {
    private List<news1_item> itemList;
    RequestQueue queue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news1_sports_hot, container, false);

        itemList = new ArrayList<>();
        queue= Volley.newRequestQueue(view.getContext());
        String url = "http://15.164.210.22:5000/sport";

        final JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //s3에서 기사 받아와 배열에 저장
                try {
                    // 예시: 응답으로부터 필요한 데이터를 파싱하여 처리
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String title = jsonObject.getString("title");
                        String content = jsonObject.getString("origin_content");
                        String press = jsonObject.getString("media");
                        String date = jsonObject.getString("date");
                        String img = jsonObject.getString("img");
                        String summary=jsonObject.getString("summary");
                        String key=jsonObject.getString("key");

                        // NewsData 클래스를 사용하여 데이터를 저장하고 리스트에 추가
                        news1_item newsData = new news1_item(id,title,content,press,date,img,summary,key);
                        System.out.println(title);
                        itemList.add(newsData);

                        // 이후에 newsList를 사용하여 원하는 처리를 진행
                        //Adapter
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                        RecyclerView recyclerView=view.findViewById(R.id.news1_sports_hot_recyclerview);
                        recyclerView.setLayoutManager(layoutManager);
                        news1_hot_news_adapter adapter=new news1_hot_news_adapter(itemList);
                        recyclerView.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                1000000,  // 기본 타임아웃 (기본값: 2500ms)
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        request.setShouldCache(false);
        queue.add(request);

        return view;
    }
}
