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
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class news1_hot_news_adapter extends RecyclerView.Adapter<news1_hot_news_adapter.HotNewsViewHolder> {
    private List<news1_item> newsItems;
    RequestQueue queue;
    String email;
    private SharedPreferences preferences;
    public static final String preference = "newkey";

    public news1_hot_news_adapter(List<news1_item> newsItems) {
        this.newsItems = newsItems;
    }

    @NonNull
    @Override
    public HotNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news1_hot_news, parent, false);
        queue= Volley.newRequestQueue(view.getContext());
        preferences=view.getContext().getSharedPreferences(preference,  Context.MODE_PRIVATE);
        email=preferences.getString("email", null);

        return new HotNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotNewsViewHolder holder, int position) {
        news1_item newsItem = newsItems.get(position);
        Glide.with(holder.itemView.getContext()).load(newsItem.getImg()).into(holder.newsImage);
        holder.setItem(newsItem);

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

                request.setShouldCache(false);
                queue.add(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    public static class HotNewsViewHolder extends RecyclerView.ViewHolder {
        public ImageView newsImage;
        public TextView newsTitle;

        public HotNewsViewHolder(View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.news1_hot_item);
            newsTitle = itemView.findViewById(R.id.news1_hot_item_title);
        }
        public void setItem(news1_item item){
            newsTitle.setText(item.getTitle());
        }
    }
}
