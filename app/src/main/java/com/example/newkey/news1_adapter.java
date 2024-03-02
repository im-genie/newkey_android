package com.example.newkey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class news1_adapter extends RecyclerView.Adapter<news1_adapter.NewsViewHolder> {
    private List<news1_item> newsItems;
    RequestQueue queue;
    String email;
    private SharedPreferences preferences;
    public static final String preference = "newkey";

    public news1_adapter(List<news1_item> newsItems) {
        this.newsItems = newsItems;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news1_recyclerview, parent, false);
        queue=Volley.newRequestQueue(view.getContext());
        preferences=view.getContext().getSharedPreferences("newkey",  Context.MODE_PRIVATE);
        email=preferences.getString("email", null);

        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        news1_item newsItem = newsItems.get(position);
        holder.setItem(newsItem);

        Glide.with(holder.itemView.getContext())
                .load(newsItem.getImg())
                .into(holder.newsImage);

        // 아이템 클릭 리스너 추가
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), news3_activity.class);
                intent.putExtra("id", newsItem.getId());
                intent.putExtra("title", newsItem.getTitle());
                intent.putExtra("content", newsItem.getContent());
                intent.putExtra("publisher", newsItem.getPublisher());
                intent.putExtra("date", newsItem.getDate());
                intent.putExtra("img", newsItem.getImg());
                intent.putExtra("summary", newsItem.getSummary());
                intent.putExtra("key", newsItem.getKey());
                v.getContext().startActivity(intent);

                //클릭 시 사용자 정보 저장
                String flask_url = "http://15.164.210.22:5000/click";

                final StringRequest request=new StringRequest(Request.Method.POST, flask_url, new Response.Listener<String>() {
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
                        params.put("click_news", newsItem.getId());

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
        });
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView newsTitle, newsPublisher, newsTime;
        public ImageView newsImage;

        public NewsViewHolder(View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.news1_item_title);
            newsPublisher = itemView.findViewById(R.id.news1_item_name);
            newsTime = itemView.findViewById(R.id.news1_item_time);
            newsImage = itemView.findViewById(R.id.news1_item_image);
        }
        public void setItem(news1_item item){
            newsTitle.setText(item.getTitle());
            newsPublisher.setText(item.getPublisher());
            newsTime.setText(item.getDate());
        }
    }
}
